package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.SlabBlock;

public class TemplateSlab extends SlabBlock implements IPlaceholder {

    private final Type type;

    public TemplateSlab(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}