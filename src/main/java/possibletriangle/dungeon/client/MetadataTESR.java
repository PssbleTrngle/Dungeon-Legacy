package possibletriangle.dungeon.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.world.structure.metadata.Part;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import javax.annotation.Nonnull;
import java.awt.*;

public class MetadataTESR extends TileEntityRenderer<MetadataTile> {

    private static final int[] COLORS = {
      new Color(255, 51, 54).getRGB(),
      new Color(90, 247, 123).getRGB(),
      new Color(5, 163, 255).getRGB(),
      new Color(255, 194, 51).getRGB(),
      new Color(126, 57, 233).getRGB()
    };

    public static int getColor(int index) {
        return COLORS[index % COLORS.length];
    }

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

        for(int i = 0; i < meta.getParts().length; i++) {
            Part part = meta.getParts()[i];
            AxisAlignedBB pos = part.getPos();
            AxisAlignedBB size = part.getSize();

            int color = getColor(i);

            double posX = pos.minX;
            double posY = pos.minY;
            double posZ = pos.minZ;

            AxisAlignedBB outer = new AxisAlignedBB(
                posX, posY, posZ,
                pos.maxX + size.maxX,
                pos.maxY + size.maxY,
                pos.maxZ + size.maxZ
            ).offset(tile.getOffset());

            int deg = ((int) System.currentTimeMillis()) / 10 % 360;
            double animation = Math.sin(deg / 180F  * Math.PI) / 2 + 0.5;

            AxisAlignedBB inner = new AxisAlignedBB(
                0, 0, 0,
                    Math.round(size.minX + (size.maxX - size.minX) * animation),
                    Math.round(size.minY + (size.maxY - size.minY) * animation),
                    Math.round(size.minZ + (size.maxZ - size.minZ) * animation)
            ).offset(tile.getOffset()).offset(new BlockPos(posX, posY, posZ));

            GlStateManager.lineWidth(2.0F);
            this.drawBox(outer, color);
            GlStateManager.lineWidth(1.0F);
            this.drawBox(inner, color);
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
        this.drawBox(box, 0xFFFFFF);
    }

    private void drawBox(AxisAlignedBB box, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        double x1 = box.minX;
        double y1 = box.minY;
        double z1 = box.minZ;
        double x2 = box.maxX;
        double y2 = box.maxY;
        double z2 = box.maxZ;

        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = ((color >> 0) & 0xFF) / 255F;

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