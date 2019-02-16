package possibletriangle.dungeon.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import possibletriangle.dungeon.Dungeon;

import java.util.function.Predicate;

public class EntityAIFollowCurse extends EntityAINearestAttackableTarget<EntityPlayer> {

    public EntityAIFollowCurse(EntityCreature entity) {
        super(entity, EntityPlayer.class, 1, true, false, (player) -> (player.getDistance(entity) <= EntityCurse.ACTIVATION_DISTANCE));
    }

}
