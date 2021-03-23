package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.SlabBlock;

public class TemplateSlab extends SlabBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateSlab(TemplateType type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}