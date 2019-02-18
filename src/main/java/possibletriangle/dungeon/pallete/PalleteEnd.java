package possibletriangle.dungeon.pallete;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import possibletriangle.dungeon.generator.RandomCollection;

import java.util.Random;

public class PalleteEnd extends Pallete {

    public PalleteEnd() {
        super("end", 0F);
    }

    @Override
    public double weight() {
        return 0.1;
    }

    @Override
    public Replacer forType(Type type, int variant) {

        Replacer r = new Replacer();

        switch(type) {

            case FLUID_HARMFUL:
                r.add("thermalfoundation:fluid_ender", 1);
                r.add(Blocks.LAVA);
                break;
            case FLUID_SAVE:
                r.add(Blocks.AIR);
                break;

            case RUNE:
            case KEY_STONE:
                r.add("quark:duskbound_block");
                r.add(Blocks.PURPUR_BLOCK);
                break;
            case GEM:
                r.add(Blocks.LAPIS_BLOCK);
                break;

            case FLOOR:
                r.add(Blocks.END_BRICKS);
                break;
            case WALL:
                r.add(Blocks.PURPUR_BLOCK);
                break;
            case PILLAR:
                r.add(Blocks.PURPUR_PILLAR);
                break;

            case STAIRS:
                r.add("quark:end_bricks_stairs", 1);
            case STAIRS_WALL:
                r.add(Blocks.PURPUR_STAIRS);
                break;
            case SLAB:
                r.add("quark:end_bricks_slab", 1);
                r.add(Blocks.PURPUR_SLAB);
                break;

            case GRASS:
            case DIRT:
                r.add("quark:biotite_ore", 0, 0.05);
                r.add(Blocks.END_STONE);
                break;
            case LOG:
            case LOG2:
                r.add(Blocks.CHORUS_PLANT);
                break;
            case LEAVES:
            case LEAVES2:
                r.add("natura:clouds:1");
                r.add(Blocks.OBSIDIAN);
                break;
            case PLANT:
                r.add(Blocks.CHORUS_PLANT, 0, 1);
                r.add(Blocks.CHORUS_FLOWER, 0, 0.5);
                r.add(Blocks.AIR, 0, 1);
                break;

            case BARS:
                r.add(Blocks.IRON_BARS);
                break;
            case LAMP:
                r.add(Blocks.OBSIDIAN);
                r.add("quark:duskbound_lantern", 1);
                break;
            case TORCH:
                r.add(Blocks.END_ROD);
                break;
            case LADDER:
                r.add("quark:iron_ladder", 1);
                r.add(Blocks.LADDER);
                break;

        }

        return r;

    }
}
