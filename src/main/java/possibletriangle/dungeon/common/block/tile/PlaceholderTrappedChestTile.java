package possibletriangle.dungeon.common.block.tile;

public class PlaceholderTrappedChestTile extends PlaceholderChestTile {

    @ObjectHolder("dungeon:obelisk")
    public static final TileEntityType<PlaceholderTrappedChestTile> TYPE = null;

    public PlaceholderChestTile() {
        super(TYPE);
    }

    @Override
	protected void onOpenOrClose() {
		super.onOpenOrClose();
		this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockState().getBlock());
	}

}