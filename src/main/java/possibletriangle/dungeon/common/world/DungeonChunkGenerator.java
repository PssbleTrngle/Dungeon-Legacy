package possibletriangle.dungeon.common.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.room.Room;
import possibletriangle.dungeon.common.world.wall.Wall;

import java.util.Random;

public class DungeonChunkGenerator extends ChunkGenerator<DungeonSettings> {

    public static Random chunkSeed(long worldSeed, int x, int z) {
        return new Random(worldSeed ^ ((x & z) * 10000));
    }

    public DungeonChunkGenerator(World world, DungeonSettings settings) {
        super(world, new DungeonBiomeProvider(world.getSeed()), settings);
    }

    @Override
    public void generateSurface(IChunk chunk) {}

    @Override
    public int getGroundHeight() {
        return 0;
    }

    private Room roomFor(Random random, ChunkPos pos) {
        boolean hallway = pos.x % 2 == pos.z % 2;
        if(hallway) return Room.random(Room.Type.HALLWAY, random);
        return Room.random(Room.Type.ROOM, random);
    }

    private Room roomFor(Random random, ChunkPos pos, DungeonSettings settings, int maxFloors) {
        Room room;
        do {
            room = roomFor(random, pos);
        } while(room == null || room.getSize(settings).getY() > maxFloors);
        return room;
    }

    @Override
    public void makeBase(IWorld world, IChunk ichunk) {

        /* TODO generate a real seed for a chunk like below but better  */
        Random random = chunkSeed(world.getSeed(), ichunk.getPos().x, ichunk.getPos().z);
        int floorHeight = getSettings().floorHeight;

        DungeonChunk chunk = new DungeonChunk(ichunk, random, getSettings());

        for(int floor = 0; floor < getSettings().floors; floor++) {
            chunk.setFloor(floor);

            Room room = roomFor(random, ichunk.getPos(), getSettings(), getSettings().floors - floor);
            Vec3i size = room.getSize(getSettings());
            if(size.getY() == 0) {
                DungeonMod.LOGGER.info("Error: {} has a zero height", room.getRegistryName());
            }

            room.generate(chunk, floor, random, this.getSettings());
            Wall.generate(chunk, size.getY(), random, this.getSettings());

            /* If the room is heigher than 1 floor, skip the next floors to not override it */
            int height = size.getY();
            if(height > 1) floor += height - 1;

            boolean solidCeiling =   floor < getSettings().floors - 1 || getSettings().hasCeiling;
            Block ceiling = solidCeiling ? TemplateBlock.FLOOR : Blocks.BARRIER;
            for(int x = 0; x < 16; x++)
                for(int z = 0; z < 16; z++)
                    chunk.setBlockState(new BlockPos(x, (floorHeight + 1) * (size.getY() - 1) + floorHeight, z), ceiling.getDefaultState());
        }
    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
