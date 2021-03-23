package possibletriangle.dungeon.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

public class BreakableBlock extends Block {

    public static final RegistryObject<Block> STONE = DungeonMod.registerBlock("porous_stone", () -> new BreakableBlock(Blocks.STONE));
    public static final RegistryObject<Block> GRAVEL = DungeonMod.registerBlock("gravelous_gravel", () -> new BreakableBlock(Blocks.GRAVEL));
    public static final RegistryObject<Block> WOOD = DungeonMod.registerBlock("morsh_wood", () -> new BreakableBlock(Blocks.OAK_PLANKS));

    public final ToolType tool;

    public BreakableBlock(Block parent) {
        super(Block.Properties.from(parent).noDrops());
        this.tool = parent.getDefaultState().getHarvestTool();
    }

}
