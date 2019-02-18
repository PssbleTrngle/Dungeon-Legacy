package possibletriangle.dungeon.block.tile.tesr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumParticleTypes;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;
import possibletriangle.dungeon.helper.Icons;

public class TESRSpawn extends TileEntitySpecialRenderer<TileEntitySpawn> {

    @Override
    public void render(TileEntitySpawn te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        setLightmapDisabled(true);

        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);

        GlStateManager.translate(0, te.getOffset()[1], 0);
        VortexRenderer.render(te.vortexSize, 0.1, te.global ? Icons.VORTEX_GLOBAL : Icons.VORTEX);

        GlStateManager.popMatrix();

    }

    public void renderOrb() {



    }

}
