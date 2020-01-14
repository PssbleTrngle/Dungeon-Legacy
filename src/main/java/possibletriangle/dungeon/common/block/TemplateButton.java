package possibletriangle.dungeon.common.block;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.StoneButtonBlock;

public class TemplateButton extends StoneButtonBlock implements IPlaceholder {

    private final Type type;

    public TemplateButton(Type type) {
        super(TemplateBlock.PROPERTIES);
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}