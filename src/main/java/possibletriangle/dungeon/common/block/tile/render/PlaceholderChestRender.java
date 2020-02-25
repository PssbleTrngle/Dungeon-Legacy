package possibletriangle.dungeon.common.block.tile.render;

public class PlaceholderChestRender extends ChestTileEntityRenderer<ChestTileEntity> {

    private ChestTileEntity tile;

	public static ResourceLocation forceNormal, forceDouble;

	@Override
	public void render(ChestTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
		tile = tile;
		super.render(tileEntityIn, x, y, z, partialTicks, destroyStage);
    }
    
    @Override
	protected void bindTexture(ResourceLocation location) {
		boolean isDouble = location.getPath().contains("double");

		if(tile != null && tile.hasWorld()) {
			if(location.getPath().contains("normal")) {
				Block block = tile.getBlockState().getBlock();
				if(block instanceof VariantChestBlock) {
					VariantChestBlock vblock = (VariantChestBlock) block;
					location = isDouble ? vblock.modelDouble : vblock.modelNormal;
				} else if(block instanceof VariantTrappedChestBlock) {
					VariantTrappedChestBlock vblock = (VariantTrappedChestBlock) block;
					location = isDouble ? vblock.modelDouble : vblock.modelNormal;
				}
			}
		}
		else {
			ResourceLocation forced = isDouble ? forceDouble : forceNormal;
			if(forced != null)
				location = forced;
		}

		super.bindTexture(location);
	}

}