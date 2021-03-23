package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.StoneButtonBlock;

public class TemplateButton extends StoneButtonBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateButton(TemplateType type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}