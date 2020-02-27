package possibletriangle.dungeon.world.structure.metadata.condition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.world.generator.GenerationContext;

import java.util.Optional;
import java.util.function.BiPredicate;

public class GenerationCondition extends Condition<GenerationContext> {

    private ConditionType type;

    public GenerationCondition(String[] allow, String[] reject, String[] required, ResourceLocation type) {
        super(allow, reject, required);
        setType(type);
    }

    public GenerationCondition(CompoundNBT nbt) {
        this(new String[0], new String[0], new String[0], null);
        this.deserializeNBT(nbt);
    }

    @Override
    protected BiPredicate<String, GenerationContext> getPredicate() {
        return Optional.ofNullable((BiPredicate<String, GenerationContext>) this.type).orElseGet(() -> (s, ctx) -> true);
    }

    public void setType(ResourceLocation name) {
        ConditionType type = GameRegistry.findRegistry(ConditionType.class).getValue(name);
        if(type != null) this.type = type;
        else DungeonMod.LOGGER.error("Condition Type {} could not be found", name.toString());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("type", this.type.getRegistryName().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        if(nbt.contains("type")) setType(new ResourceLocation(nbt.getString("type")));
    }

}
