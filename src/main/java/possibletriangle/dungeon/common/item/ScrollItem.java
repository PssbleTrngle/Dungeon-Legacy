package possibletriangle.dungeon.common.item.grenade;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ScrollItem extends Item {
    
    @ObjectHolder("dungeon:scroll")
    public static final Item SCROLL = null;

    public ScrollItem() {
        super(new Properties().group(DungeonMod.GROUP).maxStackSize(1));
    }
    
    public static void setSpell(ItemStack stack, SpellStack spell) {
        stack.getOrCreateTag().set("spell", spell.serializeNBT());
    }

    public static SpellStack getSpell(ItemStack stack) {
        return new SpellStack(stack.getOrCreateTag().getCompound("spell"));
    }

}
