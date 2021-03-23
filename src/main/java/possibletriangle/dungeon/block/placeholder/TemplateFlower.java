package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.FlowerBlock;
import net.minecraft.potion.Effects;

public class TemplateFlower extends FlowerBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateFlower(TemplateType type) {
        super(Effects.HASTE, 1, TemplateBlock.PROPERTIES().doesNotBlockMovement());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}