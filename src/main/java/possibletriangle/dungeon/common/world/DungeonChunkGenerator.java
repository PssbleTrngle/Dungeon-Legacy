package possibletriangle.dungeon.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.room.Structures;
import possibletriangle.dungeon.common.world.wall.Wall;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DungeonChunkGenerator extends ChunkGenerator<DungeonSettings> {

    /**
     * @return The seeded random used to generate this chunk
     */
    public static Random chunkSeed(long worldSeed, ChunkPos pos) {
        /* TODO generate a real seed for a chunk like below but better  */
        return new Random(worldSeed ^ ((pos.x & pos.z) * 10000));
    }

    public DungeonChunkGenerator(World world, DungeonSettings settings) {
        super(world, new DungeonBiomeProvider(world), settings);
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
    private static Structures.Type typeFor(GenerationContext ctx) {
        boolean hallway = ctx.pos.x % 2 == ctx.pos.z % 2;
        if(hallway) return Structures.Type.HALLWAY
        return Structures.Type.ROOM;
    }

    /**
     * @return If a structure fits the current requirements, like size or the structures' conditions defined by its .mcmeta file
     */
    private static boolean fits(Generateable structure, GenerationContext ctx) {
        return structure != null && structure.getSize(settings).getY() <= ctx.settings.floors - ctx.floor && structure.getMeta().predicate.test(ctx);
    }

    /**
     * Retrieves a random structure fitting the current requirements.
     * @param ctx The context containing the ChunkPos and the floor
     * @return The found structure
     */
    private static Generateable roomFor(Random random, DungeonSettings settings, GenerationContext ctx) {
        Generateable structure;
        Structures.Type type = typeFor(ctx);

        do {
            structure = Structures.random(type, random);
        } while(!fits(structure, ctx));
        
        return structure;
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

        for(int floor = 0; floor < settings.floors; floor++) {
            GenerationContext ctx = new GenerationContext(floor, settings, pos);

            Generateable room = roomFor(random, settings, ctx);
            Vec3i size = room.getSize(settings);
            rooms.put(floor, room);

            /* If the room is higher than 1 floor, skip the next floors to not override it */
            int height = size.getY();
            if(height > 1) floor += height - 1;
        }

        return rooms;
    }

    @Override
    public void makeBase(IWorld world, IChunk ichunk) {

        ChunkPos pos = ichunk.getPos();
        Random random = chunkSeed(world.getSeed(), pos);
        DungeonSettings settings = getSettings();

        DungeonChunk chunk = new DungeonChunk(ichunk, random, settings);
        
        roomsFor(settings, pos, world.getSeed()).forEach((floor, room) -> {
            GenerationContext ctx = new GenerationContext(floor, settings, pos);
            chunk.setFloor(floor);
            
            Vec3i size = room.getSize(settings);

            /* Generate Room and Wall */
            room.generate(chunk, random, ctx);
            Wall.generate(chunk, size.getY(), random, settings);

            this.generateCeiling(floor, size.getY(), chunk);
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

        boolean solidCeiling = floor < settings.floors - 1 || settings.hasCeiling;
        BlockState ceiling = (solidCeiling ? TemplateBlock.FLOOR : Blocks.BARRIER).getDefaultState();

        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++) {
                BlockPos p = new BlockPos(x, (settings.floorHeight + 1) * (height - 1) + settings.floorHeight, z);
                chunk.setBlockState(p, ceiling);
            }
    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
