package possibletriangle.dungeon.pallete.objects;

import net.minecraft.block.BlockHalfStoneSlab;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.pallete.Replacer;

public class PalleteNether extends Pallete {

    @Override
    public Biome getBiome(int variant) {
        return Biomes.HELL;
    }

    public PalleteNether() {
        super("nether");

        addMob(new ResourceLocation("minecraft", "zombie_pigman"), 1);
        addMob(new ResourceLocation("minecraft", "wither_skeleton"), 0.5);
        addMob(new ResourceLocation("minecraft", "blaze"), 1);
        addMob(new ResourceLocation("minecraft", "magma_cube"), 0.2);

        if(Loader.isModLoaded("quark")) addMob(new ResourceLocation("quark", "wraith"), 0.1);
        if(Loader.isModLoaded("natura")) addMob(new ResourceLocation("natura", "nitrocreeper"), 0.2);
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
                if(variant % 2 == 0) r.add("thermalfoundation:fluid_pyrotheum", 1, 0.2);
                if(variant % 2 == 1) r.add(Blocks.LAVA, 1);
                r.add(Blocks.LAVA);
                break;
            case FLUID_SAVE:
                if(variant % 2 == 0) r.add("biomesoplenty:honey", 1, 1);
                if(variant % 2 == 1) r.add("biomesoplenty:blood", 1, 2);
                r.add(Blocks.LAVA);
                break;

            case RUNE:
            case KEY_STONE:
                r.add(Blocks.MAGMA);
                break;
            case GEM:
                r.add(Blocks.GOLD_BLOCK);
                r.add(Blocks.REDSTONE_BLOCK);
                r.add("biomesoplenty:gem_block:1");
                r.add("biomesoplenty:gem_block:7");
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
                if(variant % 2 == 0) r.add("biomesoplenty:grass:6", 1, 1);
                if(variant % 2 == 1) r.add("biomesoplenty:grass:8", 1, 0.6);
            case DIRT:
                r.add(Blocks.NETHERRACK);
                break;
            case LOG:
            case LOG2:
                if(variant % 2 == 0) r.add("natura:nether_logs:0", 1);
                if(variant % 2 == 1) r.add(Blocks.BONE_BLOCK, 1);
                r.add(Blocks.BONE_BLOCK);
                break;
            case LEAVES:
            case LEAVES2:
                if(variant % 2 == 0) r.add("natura:nether_leaves:0", 1);
                if(variant % 2 == 1) r.add(Blocks.GLOWSTONE, 1);
                r.add(Blocks.GLOWSTONE);
                break;
            case CROP:
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

            case FARMLAND:
                r.add(Blocks.MYCELIUM);
                r.add(Blocks.SOUL_SAND);
                break;

            case PLANT:
                r.add(Blocks.AIR);
                break;

        }

        return r;

    }
}
