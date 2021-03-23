package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.FallingBlock;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

public class TemplateFalling extends FallingBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateFalling(TemplateType type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }
}
