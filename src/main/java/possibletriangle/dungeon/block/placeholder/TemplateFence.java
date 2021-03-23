package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.FenceBlock;

public class TemplateFence extends FenceBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateFence(TemplateType type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}