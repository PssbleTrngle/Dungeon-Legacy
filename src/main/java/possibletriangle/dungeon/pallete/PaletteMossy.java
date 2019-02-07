package possibletriangle.dungeon.pallete;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import possibletriangle.dungeon.generator.RandomCollection;

import java.util.Random;

public class PaletteMossy extends PaletteStonebrick {

    @Override
    public double weight() {
        return 0.8;
    }

    @Override
    public Replacer forType(Type type) {

        Replacer r = new Replacer();

        switch(type) {

            case GEM:
                r.add(Blocks.EMERALD_BLOCK);
                r.add(Blocks.GOLD_BLOCK);
                break;

            case FLOOR:
                r.add(Blocks.MOSSY_COBBLESTONE, 0, 0.6);
                r.add(Blocks.COBBLESTONE, 0, 0.3);
                r.add(Blocks.STONE, 0, 0.2);
                break;
            case WALL:
            case PILLAR:
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0, 1);
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT), 0, 0.3);
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0, 0.3);
                break;
            case LOG:
                r.add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE), 1, 0.3);
                break;
            case LOG2:
                r.add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH), 1, 0.3);
                break;
            case LEAVES:
                r.add(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE), 1, 0.3);
                break;
            case LEAVES2:
                r.add(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH), 1, 0.3);
                break;
            case PLANT:
                r.add(Blocks.YELLOW_FLOWER.getDefaultState());
                break;

            default:
                return super.forType(type);

        }

        return r;

    }
}
