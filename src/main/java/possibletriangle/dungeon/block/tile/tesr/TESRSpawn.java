package possibletriangle.dungeon.block.tile.tesr;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.Vec3d;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;
import possibletriangle.dungeon.helper.Icons;

public class TESRSpawn extends TileEntitySpecialRenderer<TileEntitySpawn> {

    @Override
    public void render(TileEntitySpawn te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        if(new Vec3d(x, y, z).lengthVector() > 20)
            return;

        setLightmapDisabled(true);

        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);

        GlStateManager.translate(te.getOffset()[0], te.getOffset()[1], te.getOffset()[2]);
        VortexRenderer.render(te.vortexSize, 0.1, te.global ? Icons.VORTEX_GLOBAL : Icons.VORTEX);

        GlStateManager.popMatrix();

    }



}
