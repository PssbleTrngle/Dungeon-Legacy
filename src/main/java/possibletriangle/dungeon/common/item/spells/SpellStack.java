package possibletriangle.common.item.spells;

public final class SpellStack implements INBTSerializable<NBTCompound> {

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
        return Math.clamp(this.power, 1, getSpell().maxPower());
    }

    public SpellStack(int power, Spell spell) {
        this.power = power;
        this.spell = spell;
    }

    public SpellStack(CompoundNBT nbt) {
        this.deserializeNBT(nbt);
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

    
}