package possibletriangle.dungeon.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;

import javax.annotation.Nullable;

public class DungeonChunk {

    private final IChunk chunk;

    public DungeonChunk(IChunk chunk) {
        this.chunk = chunk;
    }

    public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
        return chunk.setBlockState(pos, state, isMoving);
    }

    public BlockState setBlockState(BlockPos pos, BlockState state) {
        return setBlockState(pos, state, false);
    }

    public BlockState getBlockState(BlockPos pos) {
        return chunk.getBlockState(pos);
    }
}
