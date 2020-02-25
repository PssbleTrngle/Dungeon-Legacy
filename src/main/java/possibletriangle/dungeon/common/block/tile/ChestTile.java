package possibletriangle.dungeon.common.block.tile;

import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;

public class ChestTile extends ChestTileEntity {

    @ObjectHolder("dungeon:chest")
    public static final TileEntityType<ChestTile> TYPE = null;

    public ChestTile() {
        super(TYPE);
    }

    public ChestTile(TileEntityType<? extends ChestTile> type) {
        super(type);
    }

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
	}

}