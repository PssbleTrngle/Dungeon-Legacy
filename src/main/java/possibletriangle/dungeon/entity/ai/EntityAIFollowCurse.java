package possibletriangle.dungeon.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import possibletriangle.dungeon.entity.EntityCurse;

public class EntityAIFollowCurse extends EntityAINearestAttackableTarget<EntityPlayer> {

    public EntityAIFollowCurse(EntityCreature entity) {
        super(entity, EntityPlayer.class, 1, true, false, (player) -> (player != null && player.getDistance(entity) <= EntityCurse.ACTIVATION_DISTANCE));
    }

    @Override
    public boolean shouldContinueExecuting() {
        return true;
    }
}
