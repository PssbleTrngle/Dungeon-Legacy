package possibletriangle.dungeon.common.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.room.Room;

import java.util.Random;

public class DungeonChunkGenerator extends ChunkGenerator<DungeonSettings> {

    public DungeonChunkGenerator(World world, BiomeProvider biomes, DungeonSettings settings) {
        super(world, biomes, settings);
    }

    @Override
    public void generateSurface(IChunk chunk) {

    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    private Room roomFor(Random random, ChunkPos pos) {
        boolean hallway = pos.x % 2 == pos.z % 2;

        if(hallway)
            return Room.random(Room.Type.HALLWAY, random);

        return Room.random(Room.Type.ROOM, random);
    }

    @Override
    public void makeBase(IWorld world, IChunk ichunk) {

        long seed = world.getSeed() ^ (ichunk.getPos().x & ichunk.getPos().z);
        Random random = new Random(seed);

        DungeonChunk chunk = new DungeonChunk(ichunk, random, getSettings(), new PlacementSettings().setRotation(Rotation.func_222466_a(random)));

        for(int floor = 0; floor < getSettings().floors; floor++) {
            chunk.setFloor(floor);

            Room room = roomFor(random, ichunk.getPos());
            room.generate(chunk, floor, random, this.getSettings());
            Room.random(Room.Type.WALL, random).generate(chunk, floor, random, this.getSettings());

            boolean solidCeiling = floor < getSettings().floors - 1 || getSettings().hasCeiling;
            Block ceiling = solidCeiling ? TemplateBlock.FLOOR : Blocks.BARRIER;
            for(int x = 0; x < 16; x++)
                for(int z = 0; z < 16; z++)
                    chunk.setBlockState(new BlockPos(x, getSettings().floorHeight, z), ceiling.getDefaultState());
        }
    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
