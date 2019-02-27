package possibletriangle.dungeon.pallete.objects;

import net.minecraft.block.*;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.pallete.Replacer;

public class PalletePrismarine extends Pallete {

    @Override
    public Biome getBiome(int variant) {
        return Biomes.OCEAN;
    }

    public PalletePrismarine() {
        super("prismarine");

        addMob(new ResourceLocation("minecraft", "zombie"), 1);
        addMob(new ResourceLocation("minecraft", "skeleton"), 0.8);

        if(Loader.isModLoaded("quark")) addMob(new ResourceLocation("quark", "pirate"), 0.4);
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
                r.add("thermalfoundation:fluid_cryotheum", 1, 1);
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
                r.add("biomesoplenty:gem_block:5");
                r.add("biomesoplenty:gem_block:6");
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

            case FARMLAND:
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

            case CROP:
                r.add(Blocks.AIR);
                break;

        }

        return r;

    }
}
