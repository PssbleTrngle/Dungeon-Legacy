package possibletriangle.dungeon.block.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class BaseTile extends TileEntity {

    public BaseTile(TileEntityType<?> type) {
        super(type);
    }

    public final SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    public final CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

}
