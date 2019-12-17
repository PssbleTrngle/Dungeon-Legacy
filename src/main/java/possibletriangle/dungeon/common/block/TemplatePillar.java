package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.MODID)
public class TemplatePillar extends RotatedPillarBlock implements IPlaceholder {

    private final Type type;

    public TemplatePillar(Type type) {
        super(TemplateBlock.PROPERTIES);
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
