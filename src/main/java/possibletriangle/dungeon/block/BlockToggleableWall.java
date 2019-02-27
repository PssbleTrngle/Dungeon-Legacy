package possibletriangle.dungeon.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockToggleableWall extends BlockMod {

    public final boolean DEFAULT = false;
    public static final PropertyBool COLLISION = PropertyBool.create("collision");
    private static final AxisAlignedBB SMALL_AABB = new AxisAlignedBB(0.3D, 0.3D, 0.3D, 0.7D, 0.7D, 0.7D);


    public BlockToggleableWall() {
        super("toggleable_block", Material.STRUCTURE_VOID);

        setDefaultState(this.blockState.getBaseState().withProperty(COLLISION, DEFAULT));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COLLISION);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(COLLISION, DEFAULT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLLISION) ? 0 : 1;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return blockState.getValue(COLLISION) ? super.getCollisionBoundingBox(blockState, worldIn, pos) : NULL_AABB;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        update(worldIn, pos, state, null);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        update(worldIn, pos, state, worldIn.getBlockState(fromPos));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(true) return;
        super.updateTick(worldIn, pos, state, rand);
        update(worldIn, pos, state, null);
    }

    private void update(World world, BlockPos pos, IBlockState state, @Nullable IBlockState neighbor) {
        if (!world.isRemote) {
            boolean previous = state.getValue(COLLISION);
            boolean next = (world.isBlockPowered(pos)
                    || !(neighbor != null && neighbor.getProperties().containsKey(COLLISION) && neighbor.getValue(COLLISION)) == DEFAULT);

            if(previous != next) {
                world.setBlockState(pos, state.withProperty(COLLISION, next), 2);
                world.notifyNeighborsOfStateChange(pos, state.getBlock(), false);
            }
        }
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return state.getValue(COLLISION) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(COLLISION);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(COLLISION) ? Block.FULL_BLOCK_AABB : SMALL_AABB;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

}
