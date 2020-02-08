package possibletriangle.dungeon.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.world.structure.metadata.Part;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import javax.annotation.Nonnull;

public class MetadataTESR extends TileEntityRenderer<MetadataTile> {

	@Override
	public void render(@Nonnull MetadataTile tile, double x, double y, double z, float ticks, int digProgress) {
		GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);

        GlStateManager.disableFog();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        this.setLightmapDisabled(true);

        StructureMetadata meta = tile.getMeta();

        tile.getBounds().ifPresent(this::drawBox);

        GlStateManager.lineWidth(2.0F);
        for(Part part : meta.getParts()) {

            double posX = part.pos.minX;
            double posY = part.pos.minY;
            double posZ = part.pos.minZ;

            AxisAlignedBB outer = new AxisAlignedBB(
                posX, posY, posZ,
                part.pos.maxX + part.size.maxX,
                part.pos.maxY + part.size.maxY,
                part.pos.maxZ + part.size.maxZ
            ).offset(tile.getOffset());

            double animation = Math.sin(System.currentTimeMillis() / 6000F * Math.PI);

            AxisAlignedBB inner = new AxisAlignedBB(
                posX, posY, posZ,
                part.size.minX + (part.size.maxX - part.size.minX + 1) * animation,
                part.size.minY + (part.size.maxY - part.size.minY + 1) * animation,
                part.size.minZ + (part.size.maxZ - part.size.minZ + 1) * animation
            ).offset(tile.getOffset());

            this.drawBox(outer);
            this.drawBox(inner);
        }


        this.setLightmapDisabled(false);
        GlStateManager.lineWidth(1.0F);
        GlStateManager.enableLighting();
        GlStateManager.enableTexture();
        GlStateManager.enableDepthTest();
        GlStateManager.depthMask(true);
        GlStateManager.enableFog();
        GlStateManager.popMatrix();

    }

    private void drawBox(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        double x1 = box.minX;
        double y1 = box.minY;
        double z1 = box.minZ;
        double x2 = box.maxX;
        double y2 = box.maxY;
        double z2 = box.maxZ;

        float r = 1;
        float g = 1;
        float b = 1;

        builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x1, y1, z1).color(r, g, b, 0).endVertex();
        builder.pos(x1, y1, z1).color(r, g, b, 1).endVertex();
        builder.pos(x2, y1, z1).color(r, g, b, 1).endVertex();
        builder.pos(x2, y1, z2).color(r, g, b, 1).endVertex();
        builder.pos(x1, y1, z2).color(r, g, b, 1).endVertex();
        builder.pos(x1, y1, z1).color(r, g, b, 1).endVertex();
        builder.pos(x1, y2, z1).color(r, g, b, 1).endVertex();
        builder.pos(x2, y2, z1).color(r, g, b, 1).endVertex();
        builder.pos(x2, y2, z2).color(r, g, b, 1).endVertex();
        builder.pos(x1, y2, z2).color(r, g, b, 1).endVertex();
        builder.pos(x1, y2, z1).color(r, g, b, 1).endVertex();
        builder.pos(x1, y2, z2).color(r, g, b, 1).endVertex();
        builder.pos(x1, y1, z2).color(r, g, b, 1).endVertex();
        builder.pos(x2, y1, z2).color(r, g, b, 1).endVertex();
        builder.pos(x2, y2, z2).color(r, g, b, 1).endVertex();
        builder.pos(x2, y2, z1).color(r, g, b, 1).endVertex();
        builder.pos(x2, y1, z1).color(r, g, b, 1).endVertex();
        builder.pos(x2, y1, z1).color(r, g, b, 0).endVertex();
        tessellator.draw();

    }

}