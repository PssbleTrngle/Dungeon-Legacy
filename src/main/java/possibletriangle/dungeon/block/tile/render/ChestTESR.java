package possibletriangle.dungeon.block.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.block.placeholder.PlaceholderChest;
import possibletriangle.dungeon.block.placeholder.PlaceholderTrappedChest;
import possibletriangle.dungeon.block.tile.MetadataTile;
import possibletriangle.dungeon.block.tile.TrappedChestTile;

import static net.minecraft.client.renderer.Atlases.CHEST_ATLAS;

public class ChestTESR extends ChestTileEntityRenderer<ChestTileEntity> {

	public static final RenderMaterial SINGLE = new RenderMaterial(CHEST_ATLAS, new ResourceLocation("entity/chest/placeholder"));
	public static final RenderMaterial LEFT = new RenderMaterial(CHEST_ATLAS, new ResourceLocation("entity/chest/placeholder_left"));
	public static final RenderMaterial RIGHT = new RenderMaterial(CHEST_ATLAS, new ResourceLocation("entity/chest/placeholder_right"));

	public static final RenderMaterial SINGLE_TRAPPED = new RenderMaterial(CHEST_ATLAS, new ResourceLocation("entity/chest/placeholder_trapped"));
	public static final RenderMaterial LEFT_TRAPPED = new RenderMaterial(CHEST_ATLAS, new ResourceLocation("entity/chest/placeholder_trapped_left"));
	public static final RenderMaterial RIGHT_TRAPPED = new RenderMaterial(CHEST_ATLAS, new ResourceLocation("entity/chest/placeholder_trapped_right"));

	public ChestTESR(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected RenderMaterial getMaterial(ChestTileEntity tile, ChestType type) {
		if(tile instanceof TrappedChestTile) return getMaterial(type, SINGLE_TRAPPED, LEFT_TRAPPED, RIGHT_TRAPPED);
		else return getMaterial(type, SINGLE, LEFT, RIGHT);
	}

	private static RenderMaterial getMaterial(ChestType type, RenderMaterial single, RenderMaterial left, RenderMaterial right) {
		switch (type) {
			case LEFT: return left;
			case RIGHT: return right;
			default: return single;
		}
	}

}