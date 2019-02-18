package possibletriangle.dungeon.entity;

import net.minecraft.world.World;

public class EntityCurseFast extends EntityCurse {

    public EntityCurseFast(World world) {
        super(world);
    }

    @Override
    public float getSpeed() {
        return 0.5F;
    }

}
