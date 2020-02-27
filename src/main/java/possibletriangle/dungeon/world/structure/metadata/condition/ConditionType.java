package possibletriangle.dungeon.world.structure.metadata.condition;

import net.minecraftforge.registries.ForgeRegistryEntry;
import possibletriangle.dungeon.world.generator.GenerationContext;

import java.util.function.BiPredicate;

public abstract class ConditionType extends ForgeRegistryEntry<ConditionType> implements BiPredicate<String, GenerationContext> {

}
