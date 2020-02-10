package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class RedstoneSenderBlock extends RedstoneLinkBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public RedstoneSenderBlock() {
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        boolean powered = isPowered(world, pos);
        if(powered != state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, powered));
            this.updateReceivers(world, state.with(POWERED, powered), pos);
        }
    }

    private boolean isPowered(World world, BlockPos pos) {
        return world.isBlockPowered(pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction dir = context.getNearestLookingDirection().getOpposite();
        return getDefaultState().with(POWERED, isPowered(context.getWorld(), context.getPos())).with(FACING, dir);
    }

    private void updateReceivers(IWorld world, BlockState state, BlockPos pos) {
        boolean powered = state.get(POWERED);
        Direction facing = state.get(FACING);

        for(int i = 0; i < DISTANCE; i++) {
            Vec3i v = facing.getDirectionVec();
            BlockPos p = pos.add(v.getX() * i, v.getY() * i, v.getZ() * i);
            BlockState s = world.getBlockState(p);
            if(s.getBlock() instanceof RedstoneReceiverBlock) {
                world.setBlockState(p, s.with(POWERED, powered), 3);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        updateReceivers(world, state, pos);
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosionIn) {
        this.onPlayerDestroy(world, pos, world.getBlockState(pos));
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) {
        updateReceivers(world, state.with(POWERED, false), pos);
    }
}
