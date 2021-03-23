package possibletriangle.dungeon.world.generator;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.fml.RegistryObject;
import possibletriangle.dungeon.block.placeholder.TemplateBlock;
import possibletriangle.dungeon.block.placeholder.TemplateType;
import possibletriangle.dungeon.palette.Palette;
import possibletriangle.dungeon.util.Pair;
import possibletriangle.dungeon.world.Wall;
import possibletriangle.dungeon.world.structure.IStructure;
import possibletriangle.dungeon.world.structure.StructureType;
import possibletriangle.dungeon.world.structure.Structures;
import possibletriangle.dungeon.world.structure.metadata.Part;

import java.util.*;
import java.util.function.Supplier;

public class DungeonChunkGenerator extends ChunkGenerator {

    public static final Codec<DungeonChunkGenerator> CODEC = RecordCodecBuilder.create(record ->
            record.group(
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
                    DimensionSettings.field_236098_b_.fieldOf("settings").forGetter(generator -> generator.settings)
            ).apply(record, record.stable(DungeonChunkGenerator::new))
    );

    private final long seed;
    private final DimensionSettings settings;
    private final PaletteProvider paletteProvider;

    public static Optional<DungeonSettings> getSettings(IWorld world) {
        return Optional.of(new DungeonSettings());
    }

    /**
     * @return The seeded random used to generate this chunk
     */
    public static Random chunkSeed(long worldSeed, ChunkPos pos) {
        /* TODO generate a real seed for a chunk like below but better  */
        //return new Random(worldSeed ^ ((pos.x & pos.z) * 10000));
        return new Random();
    }

    public DungeonChunkGenerator(long seed, DimensionSettings settings) {
        super(new PaletteProvider(seed), settings);
        this.paletteProvider = new PaletteProvider(seed);
        this.seed = seed;
        this.settings = settings;
    }

    public Optional<Pair<Integer, IStructure>> roomAt(BlockPos pos, World world) {

        ChunkPos chunk = new ChunkPos(pos);
        Map<Integer, IStructure> rooms = roomsFor(world, chunk);
        int floor = pos.getY() / (DungeonSettings.FLOOR_HEIGHT + 1);

        int nextFloor = rooms.keySet().stream().filter(f -> f <= floor).max(Comparator.comparingInt(a -> a)).orElse(0);
        IStructure room = rooms.get(nextFloor);
        if (room == null) return Optional.empty();
        return Optional.of(new Pair<>(floor, room));

    }

    @Override
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return CODEC;
    }

    @Override
    public ChunkGenerator func_230349_a_(long seed) {
        return this;
    }

    @Override
    public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {

    }

    @Override
    public void func_230352_b_(IWorld world, StructureManager structures, IChunk ichunk) {

        ChunkPos pos = ichunk.getPos();
        Random random = chunkSeed(seed, pos);
        DungeonSettings settings = getSettings(world).orElseThrow(NullPointerException::new);

        GenerationContext ctx = new GenerationContext(settings, pos, paletteProvider.getPalette(pos));
        DungeonChunk chunk = new DungeonChunk(ichunk, random, ctx);

        roomsFor(world, pos).forEach((floor, room) -> {
            ctx.setFloor(floor);
            ctx.setSize(room);

            int height = room.getSize().getY();

            /* Generate Room and Wall */
            room.generate(chunk, random, ctx, new BlockPos(1, 0, 1));
            Arrays.stream(room.getMeta().getParts()).forEach(part ->
                    partFor(part, random).ifPresent(structure ->
                            structure.generate(chunk, random, ctx, part.randomPos(random))
                    )
            );
            Wall.generate(chunk, height, random, ctx);

            this.generateCeiling(floor, height, chunk);
        });
    }

    @Override
    public IBlockReader func_230348_a_(int x, int z) {
        return new Blockreader(new BlockState[0]);
    }

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

        Supplier<RegistryObject<StructureType>> sup = () -> {
            if (even || random.nextInt(3) == 0) return StructureType.HALLWAY;
            if (random.nextInt(32) == 0) return StructureType.BASE;
            return StructureType.ROOM;
        };

        return sup.get().orElseGet(StructureType.ROOM);
    }

    /**
     * @return If a structure fits the current requirements, like size or the structures' conditions defined by its .mcmeta file
     */
    private static boolean fits(IStructure structure, GenerationContext ctx) {
        return structure != null && structure.getSize().getY() <= ctx.settings.floors - ctx.getFloor() && structure.getMeta().getPredicate().test(ctx);
    }

    /*
    public static Optional<DungeonChunkGenerator> getGenerator(IWorld world) {
        ChunkGenerator generator = world.getChunkProvider().
        if(generator instanceof DungeonChunkGenerator) return Optional.of((DungeonChunkGenerator) generator);
        return Optional.empty();
    }

    public static Palette paletteAt(ChunkPos pos, IWorld world) {
        return world.;
    }

    private PaletteProvider getPaletteProvider() {
        if(this.biomeProvider instanceof PaletteProvider) return ((PaletteProvider) this.biomeProvider);
        throw new ClassCastException("BiomeProvider used for ChunkGenerator has to extend PaletteProvider.class");
    }
    */

    /**
     * Retrieves a random structure fitting the current requirements.
     *
     * @param ctx The context containing the ChunkPos and the floor
     * @return The found structure
     */
    private static IStructure roomFor(Random random, GenerationContext ctx) {
        IStructure structure;
        StructureType type = typeFor(ctx, random);

        do {
            structure = Structures.random(type, random);
        } while (!fits(structure, ctx));

        return structure;
    }

    /**
     * Retrieves all rooms for a specific ChunkPos.
     * Can be called at any time and will always return the same rooms
     *
     * @param world The world
     * @param pos   The position of the Chunk
     * @return A HashMap containing the rooms with their floor as the key
     */
    public Map<Integer, IStructure> roomsFor(IWorld world, ChunkPos pos) {
        return getSettings(world).map(settings -> {
            Random random = chunkSeed(seed, pos);
            Map<Integer, IStructure> rooms = new HashMap<>();
            GenerationContext ctx = new GenerationContext(settings, pos, paletteProvider.getPalette(pos));

            for (int floor = 0; floor < settings.floors; floor++) {
                ctx.setFloor(floor);

                IStructure room = roomFor(random, ctx);
                rooms.put(floor, room);

                /* If the room is higher than 1 floor, skip the next floors to not override it */
                int height = room.getSize().getY();
                if (height > 1) floor += height - 1;
            }

            return rooms;
        }).orElseGet(Maps::newHashMap);
    }

    public static Optional<IStructure> partFor(Part part, Random random) {
        for (int i = 0; i < 20; i++) {
            IStructure structure = Structures.random(StructureType.PART.get(), random);
            if (part.test(structure)) return Optional.of(structure);
        }
        return Optional.empty();
    }

    /**
     * Generates the ceiling
     *
     * @param floor  the current floor
     * @param height the amount of floors this room takes
     * @param chunk  the DungeonChunk instance
     */
    private void generateCeiling(int floor, int height, DungeonChunk chunk) {
        DungeonSettings settings = new DungeonSettings();

        boolean solidCeiling = (floor + height - 1) < settings.floors - 1 || settings.hasCeiling;
        BlockState ceiling = (solidCeiling ? TemplateType.FLOOR.getBlock() : Blocks.BARRIER).getDefaultState();

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                BlockPos p = new BlockPos(x, (DungeonSettings.FLOOR_HEIGHT + 1) * (height - 1) + DungeonSettings.FLOOR_HEIGHT, z);
                if (!chunk.getBlockState(p).isSolid())
                    chunk.setBlockState(p, ceiling);
            }
    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }

}
