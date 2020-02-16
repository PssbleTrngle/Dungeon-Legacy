package possibletriangle.dungeon.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.item.grenade.GrenadeItem;

import javax.annotation.Nonnull;
import java.util.Optional;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class GrenadeEntity extends ThrowableEntity implements IRendersAsItem {

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(GrenadeEntity.class, DataSerializers.ITEMSTACK);

    @ObjectHolder("dungeon:grenade")
    public static final EntityType<GrenadeEntity> TYPE = null;

    public GrenadeEntity(EntityType<GrenadeEntity> type, World world) {
        super(type, world);
    }

    public GrenadeEntity(World world, LivingEntity user) {
        super(TYPE, user, world);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    public Optional<GrenadeItem> getGrenade() {
        ItemStack i = getItem();
        if(i.getItem() instanceof GrenadeItem) return Optional.of((GrenadeItem) i.getItem());
        return Optional.empty();
    }

    public ItemStack getItem() {
        ItemStack stack = this.getDataManager().get(ITEM);
        if (stack.getItem() instanceof GrenadeItem) {
            return stack;
        } else {
            if (this.world != null)
                DungeonMod.LOGGER.error("GrenadeEntity entity {} has no or invalid model", this.getEntityId());
            return ItemStack.EMPTY;
        }
    }

    public void setItem(ItemStack stack) {
        this.getDataManager().set(ITEM, stack.copy());
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (world instanceof ServerWorld) getGrenade().ifPresent(item -> {
            double radius = 3;
            Vec3d vec = result.getHitVec();
            AxisAlignedBB box = new AxisAlignedBB(vec, vec).grow(radius);
            world.getEntitiesWithinAABB(LivingEntity.class, box, item::affects).forEach(item::affect);
            ((ServerWorld) world).spawnParticle(item.getParticle(), vec.x, vec.y, vec.z, 100, 0.4, 0.2, 0.4, 0.15);
        });
    }
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        ItemStack stack = ItemStack.read(compound.getCompound("stack"));
        if (stack.isEmpty()) {
            this.remove();
        } else {
            this.setItem(stack);
        }

    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        ItemStack stack = this.getItem();
        if (!stack.isEmpty()) {
            compound.put("stack", stack.write(new CompoundNBT()));
        }

    }

    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }
}
