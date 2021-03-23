package possibletriangle.dungeon.item.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistryEntry;
import possibletriangle.dungeon.DungeonMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class Spell extends ForgeRegistryEntry<Spell> {
    
    public static final RegistryObject<Spell> SHOCKWAVE = DungeonMod.SPELLS.register("shockwave", ShockwaveSpell::new);
    public static final RegistryObject<Spell> BLACK_HOLE = DungeonMod.SPELLS.register("blackhole", BlackholeSpell::new);

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
        public final Vector3d pos;
        public final ServerWorld world;
        public final LivingEntity user;
        @Nullable
        private final LivingEntity hit;
        public final SpellStack stack;

        public Optional<LivingEntity> getHit() {
            return Optional.ofNullable(hit);
        }

        public UseContext(Vector3d pos, ServerWorld world, LivingEntity user, @Nullable LivingEntity hit, SpellStack stack) {
            this.pos = pos;
            this.world = world;
            this.user = user;
            this.hit = hit;
            this.stack = stack;
        }

        public UseContext(Vector3d pos, ServerWorld world, PlayerEntity user, SpellStack stack) {
            this(pos, world, user, null, stack);
        }

        public <T extends LivingEntity> List<T> inRange(double radius, Class<T> affects) {
            return world.getEntitiesWithinAABB(affects, new AxisAlignedBB(pos, pos).grow(radius), e -> e.getDistanceSq(pos) <= radius);
        }

    }

}