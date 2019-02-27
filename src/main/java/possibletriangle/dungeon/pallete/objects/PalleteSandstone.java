package possibletriangle.dungeon.pallete.objects;

import net.minecraft.block.*;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.pallete.Replacer;

public class PalleteSandstone extends Pallete {

    @Override
    public Biome getBiome(int variant) {
        return Biomes.DESERT;
    }

    public PalleteSandstone() {
        super("sandstone");

        addMob(new ResourceLocation("minecraft", "husk"), 1);
        addMob(new ResourceLocation("minecraft", "skeleton"), 0.6);
        addMob(new ResourceLocation("minecraft", "creeper"), 0.1);

        if(Loader.isModLoaded("thermalfoundation")) addMob(new ResourceLocation("thermalfoundation", "basalz"), 0.1);
    }

    @Override
    public double weight() {
        return 1;
    }

    @Override
    public Replacer forType(Type type, int variant) {

        Replacer r = new Replacer();

        BlockPlanks.EnumType wood = BlockPlanks.EnumType.ACACIA;
        BlockPlanks.EnumType wood2 = BlockPlanks.EnumType.DARK_OAK;

        switch(type) {
            case FLUID_HARMFUL:
                r.add(Blocks.LAVA);
                r.add("thermalfoundation:fluid_petrotheum", 1);
                r.add("biomesoplenty:sand", 1);
                break;
            case FLUID_SAVE:
                r.add(Blocks.WATER);
                break;

            case RUNE:
                if(variant % 2 == 0) r.add(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED));
                if(variant % 2 == 1) r.add(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED));
                break;
            case KEY_STONE:
                if(variant % 2 == 0) r.add(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH));
                if(variant % 2 == 1) r.add(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH));
                break;
            case GEM:
                r.add(Blocks.LAPIS_BLOCK );
                r.add(Blocks.GOLD_BLOCK );
                r.add("biomesoplenty:gem_block:3");
                r.add("biomesoplenty:gem_block:2");
                break;

            case PILLAR:
                if(variant % 2 == 0) r.add("quark:sandstone_wall", 1);
                if(variant % 2 == 1) r.add("quark:red_sandstone_wall", 1);
            case FLOOR:
                if(variant % 2 == 0) r.add("quark:sandstone_new:1", 0, 0.2);
                if(variant % 2 == 1) r.add("quark:sandstone_new:3", 0, 0.2);
            case WALL:
                if(variant % 2 == 0) r.add(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT));
                if(variant % 2 == 1) r.add(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.DEFAULT));
                break;

            case STAIRS:
            case STAIRS_WALL:
                if(variant % 2 == 0) r.add(Blocks.SANDSTONE_STAIRS.getDefaultState());
                if(variant % 2 == 1) r.add(Blocks.RED_SANDSTONE_STAIRS.getDefaultState());
                break;
            case SLAB:
                if(variant % 2 == 0) {
                    r.add(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockHalfStoneSlab.VARIANT, BlockHalfStoneSlab.EnumType.SAND));
                    r.add("quark:sandstone_brick_slab", 1, 0.3);
                    r.add("quark:sandstone_smooth_slab", 1, 1);
                }
                if(variant % 2 == 1) {
                    r.add(Blocks.STONE_SLAB2.getDefaultState().withProperty(BlockHalfStoneSlabNew.VARIANT, BlockHalfStoneSlabNew.EnumType.RED_SANDSTONE));
                    r.add("quark:red_sandstone_brick_slab", 1, 0.3);
                    r.add("quark:red_sandstone_smooth_slab", 1, 1);
                }
                break;

            case GRASS:
                r.add(Blocks.GRASS);
                r.add("biomesoplenty:grass:3", 1);
                break;

            case FARMLAND:
            case DIRT:
                r.add(Blocks.DIRT);
                r.add("biomesoplenty:dirt:1", 1);
                break;
            case LOG:
                r.add(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, wood));
                break;
            case LOG2:
                r.add(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, wood2));
                break;
            case LEAVES:
                r.add(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLog.VARIANT, wood));
                break;
            case LEAVES2:
                r.add(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLog.VARIANT, wood2));
                break;
            case PLANT:
                r.add(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
                r.add(Blocks.AIR.getDefaultState());
                r.add("biomesoplenty:plant_1:6", 0, 0.5);
                r.add("biomesoplenty:plant_0:13");
                r.add("biomesoplenty:plant_0:14");
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
                r.add(Blocks.LADDER);
                break;

            case PLANKS:
                r.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, wood));
                break;

            case SLAB_PLANKS:
                r.add(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, wood));
                break;

            case STAIRS_PLANKS:
                r.add(stairs(wood));
                break;

            case CROP:
                r.add("biomesoplenty:plant_0:13", 0, 1.5);
                r.add("biomesoplenty:plant_0:14", 0, 1.5);
                r.add(Blocks.AIR);
                break;

        }

        return r;

    }
}
