package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.Tags;

public class TemplateStairs extends StairsBlock {

    public TemplateStairs(Template template) {
        super(Blocks.STONE.getDefaultState(), TemplateBlock.PROPERTIES);
        setRegistryName("placeholder_" + template.name().toLowerCase());
    }

}