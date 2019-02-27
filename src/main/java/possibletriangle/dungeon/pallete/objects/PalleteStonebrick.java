package possibletriangle.dungeon.pallete.objects;

import net.minecraft.block.*;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.pallete.Replacer;

public class PalleteStonebrick extends Pallete {

    @Override
    public Biome getBiome(int variant) {
        return Biomes.PLAINS;
    }

    public PalleteStonebrick() {
        super("stonebrick");

        addMob(new ResourceLocation("minecraft", "zombie"), 1);
        addMob(new ResourceLocation("minecraft", "skeleton"), 0.8);
        addMob(new ResourceLocation("minecraft", "creeper"), 0.1);
    }

    @Override
    public double weight() {
        return 1;
    }

    @Override
    public Replacer forType(Type type, int variant) {

        Replacer r = new Replacer();

        BlockPlanks.EnumType wood = BlockPlanks.EnumType.OAK;
        if(variant % 3 == 1) wood = BlockPlanks.EnumType.DARK_OAK;
        if(variant % 3 == 2) wood = BlockPlanks.EnumType.SPRUCE;

        switch(type) {
            case FLUID_HARMFUL:
                r.add(Blocks.LAVA);
                break;
            case FLUID_SAVE:
                r.add(Blocks.WATER);
                break;

            case RUNE:
            case KEY_STONE:
                r.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));
                break;
            case GEM:
                r.add(Blocks.DIAMOND_BLOCK, 0, 1);
                r.add(Blocks.GOLD_BLOCK, 0, 1);
                r.add(Blocks.LAPIS_BLOCK, 0, 0.7);
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
                if(variant % 2 == 0)  r.add("biomesoplenty:grass:7", 1);
                else r.add(Blocks.GRASS, 1);
                r.add(Blocks.GRASS);
                break;
            case DIRT:
                r.add(Blocks.DIRT);
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
                for(int meta = 0; meta < 8; meta++)
                    r.add(Blocks.RED_FLOWER.getStateFromMeta(meta));
                r.add(Blocks.YELLOW_FLOWER.getDefaultState());
                r.add(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN), 0, 4);
                r.add(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), 0, 4);
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
                for(int age = 0; age < 7; age++) {
                    if(variant % 4 == 0) r.add(Blocks.WHEAT.getDefaultState().withProperty(BlockCrops.AGE, age));
                    if(variant % 4 == 1) r.add(Blocks.POTATOES.getDefaultState().withProperty(BlockCrops.AGE, age));
                    if(variant % 4 == 2) r.add(Blocks.CARROTS.getDefaultState().withProperty(BlockCrops.AGE, age));
                    if(variant % 4 == 3 && age <= 3) r.add(Blocks.BEETROOTS.getDefaultState().withProperty(BlockBeetroot.BEETROOT_AGE, age));
                }
                break;

            case FARMLAND:
                r.add(Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7));
                break;

        }

        return r;

    }
}
