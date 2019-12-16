package possibletriangle.dungeon.common.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;

public class TemplateSlab extends SlabBlock {

    public TemplateSlab(Template template) {
        super(TemplateBlock.PROPERTIES);
        setRegistryName("placeholder_" + template.name().toLowerCase());
    }

}