package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.StoneButtonBlock;

public class TemplateButton extends StoneButtonBlock implements IPlaceholder {

    private final Type type;

    public TemplateButton(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}