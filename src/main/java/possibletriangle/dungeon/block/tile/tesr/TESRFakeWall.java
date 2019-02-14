package possibletriangle.dungeon.block.tile.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;
import possibletriangle.dungeon.block.tile.TileFakeWall;
import possibletriangle.dungeon.helper.Icons;

public class TESRFakeWall extends TileEntitySpecialRenderer<TileFakeWall> {

    @Override
    public void render(TileFakeWall te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(te.visibleFrom.length == 0)
            return;

        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);

        EntityPlayer player = Minecraft.getMinecraft().player;
        Vec3d p = new Vec3d(player.posX, player.posY, player.posZ).subtract(new Vec3d(te.getPos()).addVector(0.5, 0, 0.5));

        boolean visible = false;
        for(EnumFacing f : te.visibleFrom) {
            Vec3i v = f.getDirectionVec();
            visible = visible || (p.x * v.getX() >= 0 && p.y * v.getY() >= 0 && p.z * v.getZ() >= 0);
        }

        if(visible)
            for(EnumFacing f : te.visibleFrom)
                VortexRenderer.drawSide(Icons.VORTEX_GLOBAL, 1, f);

        GlStateManager.popMatrix();

        //Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.CLOUD, te.getPos().getX()+0.5, te.getPos().getY()+4.5, te.getPos().getZ()+0.5, 0, 0, 0);

    }
}
