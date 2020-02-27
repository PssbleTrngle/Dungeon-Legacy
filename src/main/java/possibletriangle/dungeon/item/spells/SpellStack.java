package possibletriangle.dungeon.item.spells;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class SpellStack implements INBTSerializable<CompoundNBT> {

    private int power;
    private Spell spell;

    public boolean isEmpty() {
        return this.getSpell() == null;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public int getPower() {
        if(isEmpty()) return 0;
        return Math.min(Math.max(this.power, 1),  getSpell().maxPower());
    }

    public SpellStack(Spell spell, int power) {
        this.power = power;
        this.spell = spell;
    }

    public SpellStack(CompoundNBT nbt) {
        this.deserializeNBT(nbt);
    }

    public int getColor() {
        return isEmpty() ? -1 : getSpell().getColor();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("power", getPower());
        if(!isEmpty()) nbt.putString("spell", getSpell().getRegistryName().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.power = nbt.getInt("power");
        this.spell = GameRegistry.findRegistry(Spell.class).getValue(new ResourceLocation(nbt.getString("spell")));
    }

    public String getTranslationKey() {
        return isEmpty() ? "spell.empty" : "spell." + getSpell().getRegistryName().toString().replace(":", ".");
    }
    
}