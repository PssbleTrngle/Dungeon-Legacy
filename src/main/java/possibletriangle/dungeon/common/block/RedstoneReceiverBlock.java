package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class RedstoneReceiverBlock extends RedstoneLinkBlock {

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {

        for(Direction d : Direction.values())
            for(int i = 0; i < DISTANCE; i++) {
                Vec3i v = d.getDirectionVec();
                BlockPos pos = context.getPos().add(v.getX() * i, v.getY() * i, v.getZ() * i);
                BlockState state = context.getWorld().getBlockState(pos);
                if(state.getBlock() instanceof RedstoneSenderBlock) {
                    return this.getDefaultState().with(POWERED, state.get(POWERED));
                }
            }

        return super.getStateForPlacement(context);
    }

    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return state.get(POWERED) ? 15 : 0;
    }
}
