package possibletriangle.dungeon.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class RenderTileSparkChanger extends TileEntityRenderer<MetadataTile> {

	@Override
	public void render(@Nonnull MetadataTile tile, double x, double y, double z, float ticks, int digProgress) {
		GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);

        StructureMetadata meta = tile.getMeta();

        this.drawBox(tile.getBounds());

        Arrays.stream(meta.getParts()).forEach(part -> {

            double posX = part.pos.minX;
            double posY = part.pos.minY;
            double posZ = part.pos.minZ;

            AxisAlignedBB outer = new AxisAlignedBB(
                posX, posY, posZ,
                part.pos.maxX + part.size.maxX,
                part.pos.maxY + part.size.maxY,
                part.pos.maxZ + part.size.maxZ
            );

            float f = Math.sin(System.currentTimeMillis() / 6000 * Math.PI);

            AxisAlignedBB inner = new AxisAlignedBB(
                posX, posY, posZ,
                part.size.minX + (part.size.maxX - part.size.minX) * f,
                part.size.minY + (part.size.maxY - part.size.minY) * f,
                part.size.minZ + (part.size.maxZ - part.size.minZ) * f
            );
            
            this.drawBox(outer);
            this.drawBox(inner);
        });
        
        GlStateManager.popMatrix();

    }

    private void drawBox(AxisAlignedBB box) {
        GlStateManager.pushMatrix();



        GlStateManager.popMatrix();
    }

}