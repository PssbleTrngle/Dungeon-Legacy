package possibletriangle.dungeon.common.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.potion.Effects;

public class TemplateFlower extends FlowerBlock implements IPlaceholder {

    private final Type type;

    public TemplateFlower(Type type) {
        super(Effects.HASTE, 1, TemplateBlock.PROPERTIES);
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}