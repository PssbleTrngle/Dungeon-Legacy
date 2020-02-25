package possibletriangle.dungeon.common.block.tile;

public class PlaceholderChestTile extends ChestTileEntity {

    @ObjectHolder("dungeon:obelisk")
    public static final TileEntityType<PlaceholderChestTile> TYPE = null;

    public PlaceholderChestTile() {
        super(TYPE);
    }

    public PlaceholderChestTile(TileEntityType<? extends PlaceholderChestTile> type) {
        super(type);
    }

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
	}

}