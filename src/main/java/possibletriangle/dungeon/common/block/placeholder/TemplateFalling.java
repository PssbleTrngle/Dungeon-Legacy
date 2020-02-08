package possibletriangle.dungeon.common.block.placeholder;

import net.minecraft.block.FallingBlock;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.MODID)
public class TemplateFalling extends FallingBlock implements IPlaceholder {

    private final Type type;

    public TemplateFalling(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
