package possibletriangle.dungeon.pallete.objects;

import net.minecraft.block.*;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.pallete.Replacer;

public class PalleteMossy extends Pallete {

    @Override
    public Biome getBiome(int variant) {
        return Biomes.JUNGLE;
    }

    public PalleteMossy() {
        super("mossy");

        addMob(new ResourceLocation("minecraft", "spider"), 1);
        addMob(new ResourceLocation("minecraft", "cave_spider"), 2);
        addMob(new ResourceLocation("minecraft", "slime"), 0.5);
    }

    @Override
    public double weight() {
        return 0.8;
    }

    @Override
    public Replacer forType(Type type, int variant) {

        Replacer r = new Replacer();

        switch(type) {

            case GEM:
                r.add(Blocks.EMERALD_BLOCK);
                r.add(Blocks.GOLD_BLOCK);
                r.add("biomesoplenty:gem_block:2");
                r.add("biomesoplenty:gem_block:5");
                r.add("biomesoplenty:gem_block:7");
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

        }

        return r;

    }
}
