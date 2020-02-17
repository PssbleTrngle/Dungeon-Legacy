package possibletriangle.dungeon.common.world.structure.metadata.condition;

import net.minecraftforge.fml.ModList;
import possibletriangle.dungeon.common.world.GenerationContext;

public class ModCondition extends ConditionType {

    @Override
    public boolean test(String modid, GenerationContext ctx) {
        return ModList.get().isLoaded(modid);
    }

}
