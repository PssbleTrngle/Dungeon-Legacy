package possibletriangle.dungeon.common.item.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class Spell extends ForgeRegistryEntry<Spell> {

    public int getRange() {
        return 5;
    }

    public abstract int maxPower();

    public abstract int getCooldown();

    /**
     * The amount of time it takes to cast this spell in ticks
     */
    public abstract int getCharge();

    public abstract void use(UseContext context);

    public abstract int getColor();

    public static class UseContext {
        public final Vec3d pos;
        public final ServerWorld world;
        public final LivingEntity user;
        public final Optional<LivingEntity> hit;
        public final SpellStack stack;

        public UseContext(Vec3d pos, ServerWorld world, LivingEntity user, Optional<LivingEntity> hit, SpellStack stack) {
            this.pos = pos;
            this.world = world;
            this.user = user;
            this.hit = hit;
            this.stack = stack;
        }

        public UseContext(Vec3d pos, ServerWorld world, PlayerEntity user, SpellStack stack) {
            this(pos, world, user, Optional.empty(), stack);
        }

        public UseContext(Vec3d pos, ServerWorld world, PlayerEntity user, @Nullable LivingEntity hit, SpellStack stack) {
            this(pos, world, user, Optional.ofNullable(hit), stack);
        }

        public <T extends LivingEntity> List<T> inRange(double radius, Class<T> affects) {
            return world.getEntitiesWithinAABB(affects, new AxisAlignedBB(pos, pos).grow(radius), e -> e.getDistanceSq(pos) <= radius);
        }

    }

}