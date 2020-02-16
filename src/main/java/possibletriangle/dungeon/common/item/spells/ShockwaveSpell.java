package possibletriangle.dungeon.common.item.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class ShockwaveSpell extends Spell {

    @Override
    public int maxPower() {
        return 3;
    }

    @Override
    public int getCooldown() {
        return 20 * 3;
    }

    @Override
    public int getCharge() {
        return 20 * 2;
    }

    @Override
    public int getColor() {
        return new Color(101, 123, 154).getRGB();
    }

    @Override
    public void use(UseContext context) {

        double radius = 2 + context.stack.getPower();
        context.inRange(radius, LivingEntity.class).forEach(hit -> {
            Vec3d vec = hit.getPositionVector().subtract(context.pos);
            hit.move(MoverType.SELF, vec.scale(3));
        });

        context.world.spawnParticle(ParticleTypes.EXPLOSION, context.pos.x, context.pos.y, context.pos.z, 1, 0, 0, 0, 1);

    }
}
