package possibletriangle.dungeon.common.block;

import net.minecraft.block.CropsBlock;
import net.minecraft.block.FarmlandBlock;

public class TemplateFarmland extends FarmlandBlock implements IPlaceholder {

    private final Type type;

    public TemplateFarmland(Type type) {
        super(TemplateBlock.PROPERTIES);
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}