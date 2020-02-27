package possibletriangle.dungeon.world.structure.metadata.condition;

import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.world.generator.GenerationContext;

public class PaletteCondition extends ConditionType {

    @Override
    public boolean test(String palette, GenerationContext ctx) {
        return new ResourceLocation(palette).equals(ctx.palette.getName());
    }

}
