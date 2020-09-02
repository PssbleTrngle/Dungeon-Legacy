package possibletriangle.dungeon.world.structure.metadata.condition;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistryEntry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.world.generator.GenerationContext;

import java.util.function.BiPredicate;

import static possibletriangle.dungeon.DungeonMod.CONDITIONS;

public abstract class ConditionType extends ForgeRegistryEntry<ConditionType> implements BiPredicate<String, GenerationContext> {

    public static final RegistryObject<ConditionType> MOD = CONDITIONS.register("mod", ModCondition::new);
    public static final RegistryObject<ConditionType> PALETTE = CONDITIONS.register("palette", PaletteCondition::new);
    public static final RegistryObject<ConditionType> FLOOR = CONDITIONS.register("floor", FloorCondition::new);

}
