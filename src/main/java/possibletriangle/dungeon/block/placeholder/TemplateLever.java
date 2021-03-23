package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.LeverBlock;

public class TemplateLever extends LeverBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateLever(TemplateType type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}