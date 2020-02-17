package possibletriangle.dungeon.common.block.placeholder;

import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;

public class TemplateStairs extends StairsBlock implements IPlaceholder {

    private final Type type;

    public TemplateStairs(Type type) {
        super(Blocks.STONE.getDefaultState(), TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}