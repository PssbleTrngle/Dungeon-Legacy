package possibletriangle.dungeon.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileFakeWall extends TileEntity {

    public EnumFacing[] visibleFrom;

    public TileFakeWall(EnumFacing... visibleFrom) {
        this.visibleFrom = visibleFrom;
    }

    public TileFakeWall() {
        this(EnumFacing.values());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        int[] i = compound.getIntArray("visibleFrom");

        if (i.length > 0) {
            visibleFrom = new EnumFacing[i.length];
            for(int x = 0; x < i.length; x++)
                visibleFrom[x] = EnumFacing.values()[i[x]];
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        int[] i = new int[visibleFrom.length];
        for(int x = 0; x < i.length; x++)
            i[x] = visibleFrom[x].ordinal();

        compound.setIntArray("visibleFrom", i);

        return compound;
    }
}
