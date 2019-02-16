package possibletriangle.dungeon.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderMagmaCube;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.entity.EntityCurse;
import possibletriangle.dungeon.entity.model.ModelCurse;

import javax.annotation.Nullable;

public class RenderEntityCurse extends RenderLiving<EntityCurse> {

    private static ResourceLocation texture = new ResourceLocation(Dungeon.MODID, "textures/entity/" + "curse.png");

    public RenderEntityCurse(RenderManager manager) {
        super(manager, ModelCurse.INSTANCE, 0.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityCurse entity) {
        return texture;
    }

}
