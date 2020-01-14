package possibletriangle.dungeon.common.world;

import net.minecraft.block.Block;
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
import possibletriangle.dungeon.common.world.room.Room;
import possibletriangle.dungeon.common.world.wall.Wall;

import java.util.Random;

public class DungeonChunkGenerator extends ChunkGenerator<DungeonSettings> {

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

    private static Room roomFor(Random random, ChunkPos pos) {
        boolean hallway = pos.x % 2 == pos.z % 2;
        if(hallway) return Room.random(Room.Type.HALLWAY, random);
        return Room.random(Room.Type.ROOM, random);
    }

    private static Room roomFor(Random random, GenerationContext ctx) {
        Room room;
        do {
            room = roomFor(random, ctx.pos);
        } while(room == null || room.getSize(settings).getY() > ctx.settings.floors - ctx.floor || !room.getMeta().predicate.test(ctx));
        return room;
    }

    private static Map<Integer,Room> roomsFor(DungeonSettings settings, ChunkPos pos, long seed) {

        Random random = chunkSeed(seed, pos);
        Map<Integer,Room> rooms = new HashMap<>();

        for(int floor = 0; floor < settings.floors; floor++) {
            GenerationContext ctx = new GenerationContext(floor, settings, pos);

            Room room = roomFor(random, ctx);
            Vec3i size = room.getSize(settings);
            rooms.put(floor, room);

            /* If the room is heigher than 1 floor, skip the next floors to not override it */
            int height = size.getY();
            if(height > 1) floor += height - 1;
        }

        return rooms;
    }

    @Override
    public void makeBase(IWorld world, IChunk ichunk) {

        ChunkPos pos = getPos();
        Random random = chunkSeed(world.getSeed(), pos);
        DungeonSettings settings = getSettings();

        DungeonChunk chunk = new DungeonChunk(ichunk, random, settings);
        
        roomsFor(settings, pos, world.getSeed()).forEach((room, floor) -> {
            GenerationContext ctx = new GenerationContext(floor, settings, pos);
            chunk.setFloor(floor);
            
            Vec3i size = room.getSize(settings);

            /* Generate Room and Wall */
            room.generate(chunk, random, ctx);
            Wall.generate(chunk, size.getY(), random, settings);

            this.generateCeiling(random);
        })
    }

    /** 
     * Generate Ceiling
     */
    private generateCeiling(Random random) {

        DungeonSettings settings = getSettings();

        boolean solidCeiling = floor < settings.floors - 1 || settings.hasCeiling;
        BlockState ceiling = (solidCeiling ? TemplateBlock.FLOOR : Blocks.BARRIER).getDefaultState();

        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++) {
                BlockPos p = new BlockPos(x, (settings.floorHeight + 1) * (size.getY() - 1) + settings.floorHeight, z);
                chunk.setBlockState(p, ceiling);
            }
    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
