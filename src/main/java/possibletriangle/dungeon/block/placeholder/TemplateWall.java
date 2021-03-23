package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.WallBlock;

public class TemplateWall extends WallBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateWall(TemplateType type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

}