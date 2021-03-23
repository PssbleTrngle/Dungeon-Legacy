package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;

public class TemplateStairs extends StairsBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateStairs(TemplateType type) {
        super(Blocks.STONE.getDefaultState(), TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}