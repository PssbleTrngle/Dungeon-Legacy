package possibletriangle.dungeon.common.block.tile.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.common.block.placeholder.PlaceholderChest;
import possibletriangle.dungeon.common.block.placeholder.PlaceholderTrappedChest;

public class ChestRenderer extends ChestTileEntityRenderer<ChestTileEntity> {

    private ChestTileEntity tile;

	public static ResourceLocation forceNormal, forceDouble;

	@Override
	public void render(ChestTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
		this.tile = tile;
		super.render(tile, x, y, z, partialTicks, destroyStage);
    }
    
    @Override
	protected void bindTexture(ResourceLocation location) {
		boolean isDouble = location.getPath().contains("double");

		if(tile != null && tile.hasWorld()) {
			if(location.getPath().contains("normal")) {
				Block block = tile.getBlockState().getBlock();
				if(block instanceof PlaceholderChest) {
					PlaceholderChest vblock = (PlaceholderChest) block;
					location = isDouble ? vblock.modelDouble : vblock.modelNormal;
				} else if(block instanceof PlaceholderTrappedChest) {
					PlaceholderTrappedChest vblock = (PlaceholderTrappedChest) block;
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