package possibletriangle.dungeon.pallete;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.Random;

public class PalleteNether extends Pallete {

    @Override
    public String getName() {
        return "nether";
    }

    @Override
    public double weight() {
        return 0.2;
    }

    @Override
    public Replacer forType(Type type) {

        Replacer r = new Replacer();

        switch(type) {

            case FLUID_HARMFUL:
            case FLUID_SAVE:
                r.add(Blocks.LAVA);
                break;

            case RUNE:
                r.add(Blocks.MAGMA);
                break;
            case GEM:
                r.add(Blocks.GOLD_BLOCK);
                r.add(Blocks.REDSTONE_BLOCK);
                break;

            case FLOOR:
                r.add(Blocks.RED_NETHER_BRICK);
                break;
            case WALL:
            case PILLAR:
                r.add(Blocks.NETHER_BRICK);
                break;

            case STAIRS:
                r.add("quark:red_nether_brick_stairs", 1);
                r.add(Blocks.NETHER_BRICK_STAIRS);
                break;
            case STAIRS_WALL:
                r.add(Blocks.NETHER_BRICK_STAIRS);
                break;
            case SLAB:
                r.add("quark:red_nether_brick_slab", 1);
                r.add(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockHalfStoneSlab.VARIANT, BlockHalfStoneSlab.EnumType.NETHERBRICK));
                break;

            case GRASS:
                r.add(Blocks.MYCELIUM);
                r.add(Blocks.SOUL_SAND);
                break;
            case LOG:
            case LOG2:
                r.add(Blocks.BONE_BLOCK);
                break;
            case LEAVES:
            case LEAVES2:
                r.add(Blocks.GLOWSTONE);
                break;
            case PLANT:
                r.add(Blocks.NETHER_WART.getDefaultState().withProperty(BlockNetherWart.AGE, 0));
                r.add(Blocks.NETHER_WART.getDefaultState().withProperty(BlockNetherWart.AGE, 1));
                r.add(Blocks.NETHER_WART.getDefaultState().withProperty(BlockNetherWart.AGE, 2));
                r.add(Blocks.NETHER_WART.getDefaultState().withProperty(BlockNetherWart.AGE, 3));
                break;

            case BARS:
                r.add(Blocks.NETHER_BRICK_FENCE);
                break;
            case LAMP:
                r.add("quark:blaze_lantern", 1);
                r.add(Blocks.GLOWSTONE);
                break;
            case TORCH:
                r.add(Blocks.REDSTONE_TORCH);
                break;
            case LADDER:
                r.add(Blocks.LADDER);
                break;

        }

        return r;

    }
}
