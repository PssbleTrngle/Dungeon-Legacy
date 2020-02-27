package possibletriangle.dungeon.item.spells;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.Vec3d;

public class BlackholeSpell extends Spell {

    @Override
    public int maxPower() {
        return 2;
    }

    @Override
    public int getCooldown() {
        return 20 * 3;
    }

    @Override
    public int getCharge() {
        return 20 * 4;
    }

    @Override
    public int getColor() {
        return 0x000000;
    }

    @Override
    public void use(UseContext context) {

        double radius = 2 + context.stack.getPower();
        context.inRange(radius, LivingEntity.class).forEach(hit -> {
            Vec3d vec = hit.getPositionVector().subtract(context.pos);
            hit.move(MoverType.SELF, vec.scale(-4));
        });
    }
}
