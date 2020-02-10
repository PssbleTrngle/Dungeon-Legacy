package possibletriangle.dungeon.common.item.grenade;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class GrenadeSmoke extends GrenadeItem {

    @Override
    public void affect(LivingEntity entity) {
        entity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 20 * 5, 0, false, false));
    }

    @Override
    public IParticleData getParticle() {
        return ParticleTypes.LARGE_SMOKE;
    }
}
