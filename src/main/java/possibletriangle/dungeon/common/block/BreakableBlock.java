package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.MODID)
public class BreakableBlock extends Block {

    @ObjectHolder("porous_stone")
    public static final Block STONE = null;

    @ObjectHolder("gravelous_gravel")
    public static final Block GRAVEL = null;

    @ObjectHolder("morsh_wood")
    public static final Block WOOD = null;

    public final ToolType tool;

    public BreakableBlock(Block parent) {
        super(Block.Properties.from(parent).noDrops());
        this.tool = tool;
    }

}
