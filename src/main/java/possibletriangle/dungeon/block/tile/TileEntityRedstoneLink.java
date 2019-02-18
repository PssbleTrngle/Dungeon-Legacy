package possibletriangle.dungeon.block.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.BlockRedstoneLink;
import possibletriangle.dungeon.block.ModBlocks;

public class TileEntityRedstoneLink extends TileEntity implements ITickable {

    private int blocksAway = 3;
    private EnumFacing direction;

    public TileEntityRedstoneLink(EnumFacing direction) {
        this.direction = direction;
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        direction = EnumFacing.byName(compound.getString("direction"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if(direction != null)
            compound.setString("direction", direction.getName());
        return compound;
    }

    @Override
    public void update() {
        if(!world.isRemote && direction != null) {
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
