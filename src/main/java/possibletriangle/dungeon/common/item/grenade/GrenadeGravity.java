package possibletriangle.dungeon.common.item.grenade;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import possibletriangle.dungeon.DungeonMod;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.OptionalInt;

@Mod.EventBusSubscriber
public class GrenadeGravity extends GrenadeItem {

    /*
     * A bit workaround, as it could cause conflicts if another mod tries to register to the exact same id
     */
    private static final DataParameter<OptionalInt> COOLDOWN = new DataParameter<>(101, DataSerializers.OPTIONAL_VARINT);

    @SubscribeEvent
    public static void tick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity e = event.getEntityLiving();

        OptionalInt cooldown = e.getDataManager().get(COOLDOWN);
        cooldown.ifPresent(c -> {
            if(c > 0) {
                if(c == 10) {
                    e.setMotion(e.getMotion().x, 0, e.getMotion().z);
                    e.setNoGravity(true);
                }
                e.getDataManager().set(COOLDOWN, OptionalInt.of(c - 1));
            }
            else {
                e.removePotionEffect(Effects.LEVITATION);
                //e.addPotionEffect(new EffectInstance(Effects.LEVITATION, 4 * 20, 256, false, true));
                e.setNoGravity(false);
                e.getDataManager().set(COOLDOWN, OptionalInt.empty());
            }
        });
    }

    @SubscribeEvent
    public static void register(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof LivingEntity) {
            event.getEntity().getDataManager().register(COOLDOWN, OptionalInt.empty());
        }
    }

    @Override
    public void affect(LivingEntity entity) {
        entity.addPotionEffect(new EffectInstance(Effects.LEVITATION, 2, 30, false, false));
        entity.getDataManager().set(COOLDOWN, OptionalInt.of(20));
    }

    @Override
    public IParticleData getParticle() {
        return ParticleTypes.SNEEZE;
    }
}
