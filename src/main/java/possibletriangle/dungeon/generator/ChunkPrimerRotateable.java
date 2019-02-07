package possibletriangle.dungeon.generator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;

public class ChunkPrimerRotateable extends ChunkPrimer {

    private final Rotation rotation;

    public ChunkPrimerRotateable(Rotation rotation) {
        this.rotation = rotation;
    }

    public static BlockPos rotate(BlockPos pos, Rotation rotation, double[] center) {

        int parts_pi = 0;
        switch(rotation) {
            case CLOCKWISE_90:
                parts_pi = 1;
                break;
            case CLOCKWISE_180:
                parts_pi = 2;
                break;
            case COUNTERCLOCKWISE_90:
                parts_pi = -1;
                break;
        }

        int[] origin = {pos.getX(), pos.getZ()};
        double[] point2 = {origin[0] - center[0], origin[1] - center[1]};
        double[] point3 = new double[2];

        int cos = (int) Math.cos(Math.PI/2*parts_pi);
        int sin = (int) Math.sin(Math.PI/2*parts_pi);

        point3[0] = point2[0] * cos - point2[1] * sin;
        point3[1] = point2[0] * sin + point2[1] * cos;

        point3[0] += center[0];
        point3[1] += center[1];

        return new BlockPos((int) point3[0], pos.getY(), (int) point3[1]);

    }

    @Override
    public void setBlockState(int x, int y, int z, IBlockState state) {

        BlockPos pos = rotate(new BlockPos(x, y, z), rotation, new double[] {15.0 / 2, 15.0 / 2});

        /*
        Dungeon.LOGGER.info("Test: {}/{} -> {}/{}", x, z, pos.getX(), pos.getZ());
        if(getBlockState(pos.getX(), pos.getY(), pos.getZ()) != ModBlocks.AIR.getDefaultState())
            Dungeon.LOGGER.info("Double: {}/{}", pos.getX(), pos.getZ());
        */

        super.setBlockState(pos.getX(), pos.getY(), pos.getZ(), state.withRotation(rotation));
    }

    public IBlockState getBlockStateWithRotation(int x, int y, int z) {
        BlockPos pos = rotate(new BlockPos(x, y, z), rotation, new double[] {15.0 / 2, 15.0 / 2});
        return getBlockState(pos.getX(), pos.getY(), pos.getZ());
    }

}
