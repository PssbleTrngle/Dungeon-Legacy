package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.PressurePlateBlock;

public class TemplatePlate extends PressurePlateBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplatePlate(TemplateType type) {
        super(Sensitivity.MOBS, TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}