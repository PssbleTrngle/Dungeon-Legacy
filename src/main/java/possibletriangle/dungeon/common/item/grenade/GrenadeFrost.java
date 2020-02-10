package possibletriangle.dungeon.common.item.grenade;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class GrenadeFrost extends GrenadeItem {

    @Override
    public void affect(LivingEntity entity) {
        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20 * 6, 2, false, true));
    }

    @Override
    public IParticleData getParticle() {
        return ParticleTypes.CLOUD;
    }
}
