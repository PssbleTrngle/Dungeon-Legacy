package possibletriangle.dungeon.pallete;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.template.BlockRotationProcessor;
import possibletriangle.dungeon.generator.RandomCollection;

import java.util.Random;

public class PalettePrismarine extends Pallete {

    public PalettePrismarine() {
        super("prismarine", 0F);
    }

    @Override
    public double weight() {
        return 0.2;
    }

    @Override
    public Replacer forType(Type type, int variant) {

        Replacer r = new Replacer();

        switch(type) {

            case FLUID_HARMFUL:
            case FLUID_SAVE:
                r.add(Blocks.WATER);
                break;

            case RUNE:
            case KEY_STONE:
                r.add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK));
                break;
            case GEM:
                r.add(Blocks.LAPIS_BLOCK);
                r.add(Blocks.GOLD_BLOCK);
                break;

            case FLOOR:
                r.add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.ROUGH));
                break;
            case WALL:
            case PILLAR:
                r.add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS));
                break;

            case STAIRS_WALL:
                r.add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.ROUGH));
                break;
            case STAIRS:
                r.add("quark:prismarine_stairs", 1);
            case SLAB:
                r.add(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS));
                break;

            case GRASS:
            case DIRT:
                r.add(Blocks.SAND);
                r.add(Blocks.GRAVEL);
                break;
            case LOG:
            case LOG2:
                r.add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE));
                break;
            case LEAVES:
            case LEAVES2:
                r.add(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE));
                break;
            case PLANT:
                r.add(Blocks.AIR);
                break;

            case BARS:
                r.add(Blocks.IRON_BARS);
                break;
            case LAMP:
                r.add(Blocks.SEA_LANTERN);
                break;
            case TORCH:
                r.add(Blocks.AIR);
                break;
            case LADDER:
                r.add(Blocks.VINE);
                break;

        }

        return r;

    }
}
