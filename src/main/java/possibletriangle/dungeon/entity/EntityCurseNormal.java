package possibletriangle.dungeon.entity;

import net.minecraft.world.World;

public class EntityCurseNormal extends EntityCurse {

    public EntityCurseNormal(World world) {
        super(world);
    }

    @Override
    public float getSpeed() {
        return 0.35F;
    }

}
