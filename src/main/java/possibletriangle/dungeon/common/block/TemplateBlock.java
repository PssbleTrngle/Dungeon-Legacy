package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.MODID)
public class TemplateBlock extends Block {

    @ObjectHolder("placeholder_floor")
    public static final Block FLOOR = null;

    @ObjectHolder("placeholder_wall")
    public static final Block WALL = null;

    static final Properties PROPERTIES =
            Properties.create(Material.ROCK)
                .sound(SoundType.STONE)
                .hardnessAndResistance(1000.0F)
                .noDrops();

    public TemplateBlock(Template template) {
        super(PROPERTIES);
        setRegistryName("placeholder_" + template.name().toLowerCase());
    }

}
