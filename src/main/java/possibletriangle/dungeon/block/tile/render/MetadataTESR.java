package possibletriangle.dungeon.block.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.block.tile.MetadataTile;
import possibletriangle.dungeon.world.structure.metadata.Part;
import possibletriangle.dungeon.world.structure.metadata.StructureMetadata;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.stream.Stream;

public class MetadataTESR extends TileEntityRenderer<MetadataTile> {

    public MetadataTESR(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    private static final int[] COLORS = Stream.of(
            new Color(255, 51, 54),
            new Color(90, 247, 123),
            new Color(5, 163, 255),
            new Color(255, 194, 51),
            new Color(126, 57, 233)
    ).mapToInt(Color::getRGB).toArray();

    public static int getColor(int index) {
        return COLORS[index % COLORS.length];
    }

    @Override
    public void render(MetadataTile tile, float ticks, MatrixStack matrizes, IRenderTypeBuffer buffer, int light, int overlay) {
        matrizes.push();

        GlStateManager.disableTexture();
        GlStateManager.enableBlend();

        StructureMetadata meta = tile.getMeta();

        tile.getBounds().ifPresent(this::drawBox);

        for (int i = 0; i < meta.getParts().length; i++) {
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
            double animation = Math.sin(deg / 180F * Math.PI) / 2 + 0.5;

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

        GlStateManager.lineWidth(1.0F);
        GlStateManager.enableTexture();
        GlStateManager.enableDepthTest();
        GlStateManager.depthMask(true);
        matrizes.pop();
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