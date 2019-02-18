package possibletriangle.dungeon.entity;

import net.minecraft.world.World;

public class EntityCurseSlow extends EntityCurse {

    public EntityCurseSlow(World world) {
        super(world);
    }

    @Override
    public float getSpeed() {
        return 0.25F;
    }

}
