package possibletriangle.dungeon.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import possibletriangle.dungeon.Dungeon;

public class EntityCurse extends EntityMob {

    public static final int TRACKING_DISTANCE = 64, ACTIVATION_DISTANCE = 6;
    public static final double SPEED = 0.25D;

    public EntityCurse(World worldIn) {
        super(worldIn);
        this.setSize(1F, 1.7F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, TRACKING_DISTANCE));
        this.targetTasks.addTask(1, new EntityAIFollowCurse(this));
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {

        if(player.getEntityBoundingBox().intersects(this.getEntityBoundingBox())) {
            player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:wither"), 20 * 20, 1, false, true));
        }

        super.onCollideWithPlayer(player);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(world.isRemote)
            for (int i = 0; i < 5; i++) {
                this.world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, this.posX - 0.5 +  this.rand.nextDouble() * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ - 0.5 + this.rand.nextDouble() * (double)this.width, 0.1D, 0.1D, 0.1D);
            }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100D);
    }

    @Override
    public boolean isEntityUndead() {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return source != DamageSource.OUT_OF_WORLD;
    }
}
