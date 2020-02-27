package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.WallBlock;

public class TemplateWall extends WallBlock implements IPlaceholder {

    private final Type type;

    public TemplateWall(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

}