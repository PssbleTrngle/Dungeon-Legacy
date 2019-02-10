package possibletriangle.dungeon.block.tile.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.helper.Icons;

public class VortexRenderer {

    public static void render(double size, double speed, TextureAtlasSprite sprite) {
        renderStatic((int) (System.currentTimeMillis() * speed % 1000 / 1000.0 * 360), size, sprite);
    }

    public static void renderStatic(int deg, double size, TextureAtlasSprite sprite) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate(-1, 0, 0);

        double core = size - 0.7;
        renderOnce(deg, core, 1, sprite);
        renderOnce(deg, core + 0.7 * (System.currentTimeMillis() / 1000D % 1), 0.5F, sprite);
        renderOnce(deg, size, 0.3F, sprite);

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();

    }

    public static void renderOnce(int deg, double size, float opacity, TextureAtlasSprite sprite) {

        GlStateManager.pushMatrix();

        GlStateManager.translate(0.5, 0.5, -0.5);
        GlStateManager.rotate(deg, 0.5F, 0.75F, 1);
        GlStateManager.translate(-size / 2, -size / 2, -size / 2);

        GlStateManager.color(1F, 1, 1, opacity);
        drawFakeBlock(sprite, size);

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();

    }

    public static void drawFakeBlock(TextureAtlasSprite texture, double size) {
        if (texture == null)
            return;

        double minX = 0;
        double minY = 0;
        double minZ = 0;

        double maxX = minX + size;
        double maxY = minY + size;
        double maxZ = minZ + size;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        float texMinU = texture.getMinU();
        float texMinV = texture.getMinV();
        float texMaxU = texture.getMaxU();
        float texMaxV = texture.getMaxV();

        wr.pos(minX, minY, minZ).tex(texMinU, texMinV).endVertex();
        wr.pos(maxX, minY, minZ).tex(texMaxU, texMinV).endVertex();
        wr.pos(maxX, minY, maxZ).tex(texMaxU, texMaxV).endVertex();
        wr.pos(minX, minY, maxZ).tex(texMinU, texMaxV).endVertex();

        wr.pos(minX, maxY, maxZ).tex(texMinU, texMaxV).endVertex();
        wr.pos(maxX, maxY, maxZ).tex(texMaxU, texMaxV).endVertex();
        wr.pos(maxX, maxY, minZ).tex(texMaxU, texMinV).endVertex();
        wr.pos(minX, maxY, minZ).tex(texMinU, texMinV).endVertex();

        wr.pos(maxX, minY, minZ).tex(texMinU, texMaxV).endVertex();
        wr.pos(minX, minY, minZ).tex(texMaxU, texMaxV).endVertex();
        wr.pos(minX, maxY, minZ).tex(texMaxU, texMinV).endVertex();
        wr.pos(maxX, maxY, minZ).tex(texMinU, texMinV).endVertex();

        wr.pos(minX, minY, maxZ).tex(texMinU, texMaxV).endVertex();
        wr.pos(maxX, minY, maxZ).tex(texMaxU, texMaxV).endVertex();
        wr.pos(maxX, maxY, maxZ).tex(texMaxU, texMinV).endVertex();
        wr.pos(minX, maxY, maxZ).tex(texMinU, texMinV).endVertex();

        wr.pos(minX, minY, minZ).tex(texMinU, texMaxV).endVertex();
        wr.pos(minX, minY, maxZ).tex(texMaxU, texMaxV).endVertex();
        wr.pos(minX, maxY, maxZ).tex(texMaxU, texMinV).endVertex();
        wr.pos(minX, maxY, minZ).tex(texMinU, texMinV).endVertex();

        wr.pos(maxX, minY, maxZ).tex(texMinU, texMaxV).endVertex();
        wr.pos(maxX, minY, minZ).tex(texMaxU, texMaxV).endVertex();
        wr.pos(maxX, maxY, minZ).tex(texMaxU, texMinV).endVertex();
        wr.pos(maxX, maxY, maxZ).tex(texMinU, texMinV).endVertex();

        tessellator.draw();
    }

}