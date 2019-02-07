package possibletriangle.dungeon.block.tile.tesr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;

public class TESRSpawn extends TileEntitySpecialRenderer<TileEntitySpawn> {

    @Override
    public void render(TileEntitySpawn te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();

        

        GlStateManager.popMatrix();

    }

    public void renderOrb() {



    }

}
