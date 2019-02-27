package possibletriangle.dungeon.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.block.BlockRedstoneLink;
import possibletriangle.dungeon.block.ModBlocks;

public class TileEntityRedstoneLink extends TileEntity implements ITickable {

    private EnumFacing direction;

    public TileEntityRedstoneLink(EnumFacing direction) {
        this.direction = direction;
    }

    public TileEntityRedstoneLink() {
        this(EnumFacing.UP);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        direction = EnumFacing.byName(compound.getString("direction"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        if(direction != null)
            compound.setString("direction", direction.getName());
        return compound;
    }

    @Override
    public void update() {

        if(direction == null) {
            IBlockState state = world.getBlockState(getPos());
            if(state.getBlock() instanceof BlockRedstoneLink)
            direction = state.getValue(BlockRedstoneLink.DIRECTION);
        }

        if(!world.isRemote && direction != null) {

            int blocksAway = 3;
            BlockPos toCheck = pos.offset(direction, blocksAway);
            int power = world.getRedstonePower(toCheck, direction);
            IBlockState state = world.getBlockState(getPos());

            if(state.getValue(BlockRedstoneLink.POWER) != power) {
                world.setBlockState(pos, state.withProperty(BlockRedstoneLink.POWER, power), 2);
                world.notifyNeighborsOfStateChange(pos, ModBlocks.REDSTONE_LINK, false);
            }
        }

    }
}
