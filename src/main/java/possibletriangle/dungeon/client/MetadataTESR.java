package possibletriangle.dungeon.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.math.AxisAlignedBB;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class MetadataTESR extends TileEntityRenderer<MetadataTile> {

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

            double animation = Math.sin(System.currentTimeMillis() / 6000F * Math.PI);

            AxisAlignedBB inner = new AxisAlignedBB(
                posX, posY, posZ,
                part.size.minX + (part.size.maxX - part.size.minX) * animation,
                part.size.minY + (part.size.maxY - part.size.minY) * animation,
                part.size.minZ + (part.size.maxZ - part.size.minZ) * animation
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