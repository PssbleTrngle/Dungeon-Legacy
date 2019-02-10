package possibletriangle.dungeon.pallete;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import possibletriangle.dungeon.generator.RandomCollection;

import java.util.Random;

public class PaletteStonebrick extends Pallete {

    @Override
    public String getName() {
        return "stonebrick";
    }

    @Override
    public double weight() {
        return 1;
    }

    @Override
    public Replacer forType(Type type) {

        Replacer r = new Replacer();

        switch(type) {
            case FLUID_HARMFUL:
                r.add(Blocks.LAVA);
                break;
            case FLUID_SAVE:
                r.add(Blocks.WATER);
                break;

            case RUNE:
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
                break;
            case GEM:
                r.add(Blocks.DIAMOND_BLOCK, 1, 1);
                r.add(Blocks.GOLD_BLOCK, 1, 1);
                r.add(Blocks.LAPIS_BLOCK, 1, 0.7);
                break;

            case FLOOR:
            case WALL:
            case PILLAR:
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0, 0.1);
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT), 0, 1);
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0, 0.3);
                break;

            case STAIRS:
            case STAIRS_WALL:
                r.add(Blocks.STONE_BRICK_STAIRS);
                break;
            case SLAB:
                r.add(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockHalfStoneSlab.VARIANT, BlockHalfStoneSlab.EnumType.SMOOTHBRICK));
                break;

            case GRASS:
                r.add(Blocks.GRASS);
                break;
            case LOG:
                r.add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK));
                break;
            case LOG2:
                r.add(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK));
                break;
            case LEAVES:
                r.add(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK));
                break;
            case LEAVES2:
                r.add(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK));
                break;
            case PLANT:
                r.add(Blocks.RED_FLOWER);
                break;

            case BARS:
                r.add(Blocks.IRON_BARS);
                break;
            case LAMP:
                r.add("quark:lit_lamp", 1);
                r.add(Blocks.LIT_REDSTONE_LAMP);
                break;
            case TORCH:
                r.add(Blocks.TORCH);
                break;
            case LADDER:
                r.add(Blocks.VINE);
                break;

        }

        return r;

    }
}
