package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.FenceBlock;

public class TemplateFence extends FenceBlock implements IPlaceholder {

    private final Type type;

    public TemplateFence(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}