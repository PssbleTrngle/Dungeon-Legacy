package possibletriangle.dungeon.common.block;

import net.minecraft.block.CropsBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.potion.Effects;

public class TemplateCrop extends CropsBlock implements IPlaceholder {

    private final Type type;

    public TemplateCrop(Type type) {
        super(TemplateBlock.PROPERTIES);
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}