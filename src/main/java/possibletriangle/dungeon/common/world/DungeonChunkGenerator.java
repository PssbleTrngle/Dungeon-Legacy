package possibletriangle.dungeon.common.world;

import javafx.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.placeholder.TemplateBlock;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.room.Structures;
import possibletriangle.dungeon.common.world.room.StructureType;
import possibletriangle.dungeon.common.world.structure.metadata.Part;
import possibletriangle.dungeon.common.world.wall.Wall;

import java.util.*;

public class DungeonChunkGenerator extends ChunkGenerator<DungeonSettings> {

    public static Optional<DungeonSettings> getSettings(World world) {
        if(world.getWorldType() instanceof  DungeonWorldType) {
            world.getWorldInfo().getGeneratorOptions();
            return Optional.of(new DungeonSettings());
        }
        return Optional.empty();
    }

    /**
     * @return The seeded random used to generate this chunk
     */
    public static Random chunkSeed(long worldSeed, ChunkPos pos) {
        /* TODO generate a real seed for a chunk like below but better  */
        //return new Random(worldSeed ^ ((pos.x & pos.z) * 10000));
        return new Random();
    }

    public DungeonChunkGenerator(World world, DungeonSettings settings) {
        super(world, new DungeonBiomeProvider(world), settings);
    }

    public static Optional<Pair<Integer,Generateable>> roomAt(BlockPos pos, World world) {

        ChunkPos chunk = new ChunkPos(pos);
        Map<Integer, Generateable> rooms = roomsFor(world, chunk);
        int floor = pos.getY() / (DungeonSettings.FLOOR_HEIGHT + 1);

        int nextFloor = rooms.keySet().stream().filter(f -> f <= floor).max(Comparator.comparingInt(a -> a)).orElse(0);
        Generateable room = rooms.get(nextFloor);
        if(room == null) return Optional.empty();
        return Optional.of(new Pair(floor, room));

    }

    @Override
    public void generateSurface(IChunk chunk) {}

    @Override
    public int getGroundHeight() {
        return 0;
    }

    /**
     * @param ctx The context containing the ChunkPos and the floor
     * @return The Room type used for the current chunk and floor
     */
    private static StructureType typeFor(GenerationContext ctx, Random random) {
        boolean even = ctx.pos.x % 2 == ctx.pos.z % 2;
        if(even || random.nextInt(3) == 0) return StructureType.HALLWAY;
        if(random.nextInt(32) == 0) return StructureType.BASE;
        return StructureType.ROOM;
    }

    /**
     * @return If a structure fits the current requirements, like size or the structures' conditions defined by its .mcmeta file
     */
    private static boolean fits(Generateable structure, GenerationContext ctx) {
        return structure != null && structure.getSize().getY() <= ctx.settings.floors - ctx.getFloor() && structure.getMeta().getPredicate().test(ctx);
    }

    /**
     * Retrieves a random structure fitting the current requirements.
     * @param ctx The context containing the ChunkPos and the floor
     * @return The found structure
     */
    private static Generateable roomFor(Random random, GenerationContext ctx) {
        Generateable structure;
        StructureType type = typeFor(ctx, random);

        do {
            structure = Structures.random(type, random);
        } while(!fits(structure, ctx));
        
        return structure;
    }

    public static Palette paletteFor(ChunkPos pos, long seed) {
        Random random = chunkSeed(seed, pos);
        return Palette.random(random);
    }
    
    public static Map<Integer,Generateable> roomsFor(World world, ChunkPos pos) {
        /* Get settings from world */
        return getSettings(world).map(settings -> roomsFor(settings, pos, world.getSeed())).orElse(new HashMap<>());
    }

    /**
     * Retrieves all rooms for a specific ChunkPos.
     * Can be called at any time and will always return the same rooms
     * @param settings The settings of the dungeon
     * @param pos The position of the Chunk
     * @param seed The worlds seed
     * @return A HashMap containing the rooms with their floor as the key
     */
    public static Map<Integer,Generateable> roomsFor(DungeonSettings settings, ChunkPos pos, long seed) {

        Random random = chunkSeed(seed, pos);
        Map<Integer,Generateable> rooms = new HashMap<>();
        GenerationContext ctx = new GenerationContext(settings, pos, paletteFor(pos, seed));

        for(int floor = 0; floor < settings.floors; floor++) {
            ctx.setFloor(floor);

            Generateable room = roomFor(random, ctx);
            rooms.put(floor, room);

            /* If the room is higher than 1 floor, skip the next floors to not override it */
            int height = room.getSize().getY();
            if(height > 1) floor += height - 1;
        }

        return rooms;
    }

    public static Optional<Generateable> partFor(Part part, Random random) {
        for(int i = 0; i < 20; i++) {
            Generateable structure = Structures.random(StructureType.PART, random);
            if(part.test(structure) || true) return Optional.of(structure);
        }
        return Optional.empty();
    }

    @Override
    public void makeBase(IWorld world, IChunk ichunk) {

        ChunkPos pos = ichunk.getPos();
        Random random = chunkSeed(world.getSeed(), pos);
        DungeonSettings settings = getSettings();

        GenerationContext ctx = new GenerationContext(settings, pos, paletteFor(pos, seed));
        DungeonChunk chunk = new DungeonChunk(ichunk, random, ctx);
        
        roomsFor(settings, pos, world.getSeed()).forEach((floor, room) -> {
            ctx.setFloor(floor);
            ctx.setSize(room);
            
            int height = room.getSize().getY();

            /* Generate Room and Wall */
            room.generate(chunk, random, ctx, new BlockPos(1, 0, 1));
            Arrays.stream(room.getMeta().getParts()).forEach(part ->
                partFor(part, random).ifPresent(structure ->
                    structure.generate(chunk, random, ctx, part.getPos(random))
                )
            );
            Wall.generate(chunk, height, random, ctx);

            this.generateCeiling(floor, height, chunk);
        });
    }

    /**
     * Generates the ceiling
     * @param floor the current floor
     * @param height the amount of floors this room takes
     * @param chunk the DungeonChunk instance
     */
    private void generateCeiling(int floor, int height, DungeonChunk chunk) {
        DungeonSettings settings = getSettings();

        boolean solidCeiling = (floor + height - 1) < settings.floors - 1 || settings.hasCeiling;
        BlockState ceiling = (solidCeiling ? TemplateBlock.FLOOR : Blocks.BARRIER).getDefaultState();

        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++) {
                BlockPos p = new BlockPos(x, (DungeonSettings.FLOOR_HEIGHT + 1) * (height - 1) + DungeonSettings.FLOOR_HEIGHT, z);
                if(!chunk.getBlockState(p).isSolid())
                    chunk.setBlockState(p, ceiling);
            }
    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }

    @Override
    public void decorate(WorldGenRegion region) {}
}
