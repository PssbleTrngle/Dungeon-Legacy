package possibletriangle.dungeon.common.block;

import net.minecraft.block.LeverBlock;
import net.minecraft.block.PressurePlateBlock;

public class TemplatePlate extends PressurePlateBlock implements IPlaceholder {

    private final Type type;

    public TemplatePlate(Type type) {
        super(Sensitivity.MOBS, TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}