package possibletriangle.dungeon.common.block.placeholder;

import net.minecraft.block.LeverBlock;

public class TemplateLever extends LeverBlock implements IPlaceholder {

    private final Type type;

    public TemplateLever(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}