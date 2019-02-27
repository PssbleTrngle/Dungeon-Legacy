package possibletriangle.dungeon.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import possibletriangle.dungeon.entity.ai.EntityAIFollowCurse;
import possibletriangle.dungeon.entity.ai.PathNavigateCurse;

public abstract class EntityCurse extends EntityMob {

    private static final DataParameter<Boolean> ACTIVE = EntityDataManager.createKey(EntityBlaze.class, DataSerializers.BOOLEAN);

    public static final int TRACKING_DISTANCE = 64, ACTIVATION_DISTANCE = 6;

    public abstract float getSpeed();

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
    protected PathNavigate createNavigator(World world) {
        return new PathNavigateCurse(this, world);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {

        if(player.getEntityBoundingBox().intersects(this.getEntityBoundingBox())) {
            //player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:wither"), 20 * 20, 1, false, true));
        }

        //super.onCollideWithPlayer(player);
    }

    @Override
    protected void jump() {
        super.jump();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        this.dataManager.set(ACTIVE, getAttackTarget() != null);

        if(world.isRemote) {
            EnumParticleTypes particle = EnumParticleTypes.SUSPENDED_DEPTH;
            for (int i = 0; i < 2; i++)
                this.world.spawnParticle(particle, this.posX - 0.5 +  this.rand.nextDouble() * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ - 0.5 + this.rand.nextDouble() * (double)this.width, 0.1D, 0.1D, 0.1D);
            }


    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getSpeed());
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(TRACKING_DISTANCE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return true;
    }

    @Override
    public boolean isEntityUndead() {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return source != DamageSource.OUT_OF_WORLD;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ACTIVE, false);
    }

}


