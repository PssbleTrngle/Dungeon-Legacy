package possibletriangle.dungeon.pallete.objects;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.pallete.Replacer;

public class PalleteEnd extends Pallete {

    @Override
    public Biome getBiome(int variant) {
        return Biomes.SKY;
    }

    public PalleteEnd() {
        super("end");

        addMob(new ResourceLocation("minecraft", "endermite"), 1);
        addMob(new ResourceLocation("minecraft", "enderman"), 0.5);
        addMob(new ResourceLocation("minecraft", "shulker"), 0.1);
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
                r.add("biomesoplenty:gem_block:0", 1);
                r.add("biomesoplenty:gem_block:4", 1);
                r.add("thermalfoundation:storage:6", 1);
                break;

            case FLOOR:
                if(variant % 2 == 0) r.add(Blocks.END_BRICKS);
                if(variant % 2 == 1) r.add("quark:duskbound_block");
                break;
            case WALL:
                if(variant % 2 == 0) r.add(Blocks.PURPUR_BLOCK);
                if(variant % 2 == 1) r.add("quark:biotite_block");
                break;
            case PILLAR:
                if(variant % 2 == 0) r.add(Blocks.PURPUR_PILLAR);
                if(variant % 2 == 1) r.add("quark:duskbound_block_wall");
                break;

            case STAIRS:
                if(variant % 2 == 0) r.add("quark:end_bricks_stairs", 1);
                if(variant % 2 == 1) r.add("quark:duskbound_block_stairs", 1);
            case STAIRS_WALL:
                r.add(Blocks.PURPUR_STAIRS);
                if(variant % 2 == 1) r.add("quark:biotite_stairs", 1);
                break;
            case SLAB:
                if(variant % 2 == 0) r.add("quark:end_bricks_slab", 1);
                if(variant % 2 == 1) r.add("quark:duskbound_block_slab", 1);
                r.add(Blocks.PURPUR_SLAB);
                break;

            case GRASS:
            case DIRT:
            case FARMLAND:
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
                r.add(Blocks.AIR, 0, 1);
            case CROP:
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
