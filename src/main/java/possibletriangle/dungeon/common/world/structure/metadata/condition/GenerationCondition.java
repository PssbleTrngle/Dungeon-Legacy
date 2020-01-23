package possibletriangle.dungeon.common.world.structure.metadata.condition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.common.world.GenerationContext;

import java.util.Optional;
import java.util.function.BiPredicate;

public class GenerationCondition extends Condition<GenerationContext> {

    private ConditionType type;

    public GenerationCondition(String[] allow, String[] reject, String[] required, ResourceLocation type) {
        super(allow, reject, required);
        setType(type);
    }

    @Override
    protected BiPredicate<String, GenerationContext> getPredicate() {
        return null;
    }

    public void setType(ResourceLocation name) {
        Optional.ofNullable(GameRegistry.findRegistry(ConditionType.class).getValue(name)).ifPresent(
                type -> this.type = type
        );
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
