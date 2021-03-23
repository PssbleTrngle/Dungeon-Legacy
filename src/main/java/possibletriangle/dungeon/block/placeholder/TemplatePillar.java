package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.ID)
public class TemplatePillar extends RotatedPillarBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplatePillar(TemplateType type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }
}
