package possibletriangle.dungeon.pallete;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import possibletriangle.dungeon.generator.RandomCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Pallete extends Biome {

    private final RandomCollection<ResourceLocation> MOBS = new RandomCollection<>();

    public ResourceLocation mob(Random random) {
        if(MOBS.size() == 0) return null;
        return MOBS.next(random);
    }

    void addMob(ResourceLocation name, double weight) {
        MOBS.add(weight, name);
    }

    public String[] requiredMods() {
        return new String[0];
    }

    public static Pallete random(Random r) {
        return LIST.next(r);
    }

    public Pallete(String name, float temp) {
        super(new BiomeProperties(name).setTemperature(temp));

        boolean b = true;
        for (String mod : requiredMods())
            b = b && Loader.isModLoaded(mod);

        if (b) {
            LIST.add(weight(), this);
            MAP.put(new ResourceLocation(this.getBiomeName()), this);
        }
    }

    public static Block[] allBlocksFor(Type type) {

        ArrayList<Block> list = new ArrayList<>();

        for(ResourceLocation key : MAP.keySet()) {
            for (int variant = 0; variant < MAP.get(key).variantCount(); variant++) {
                Replacer r = MAP.get(key).forType(type, variant);
                for (IBlockState state : r.states())
                    if (!list.contains(state.getBlock()))
                        list.add(state.getBlock());
            }
        }

        return list.toArray(new Block[0]);

    }

    public String fallback() {
        return "stonebrick";
    }

    public static final RandomCollection<Pallete> LIST = new RandomCollection<>();
    public static final HashMap<ResourceLocation, Pallete> MAP = new HashMap<>();

    public abstract Replacer forType(Type type, int variant);

    public final IBlockState get(Type type, IBlockState parent, Random r, int variant) {

        IBlockState state = forType(type, variant).get(r);
        if(forType(type, variant).isEmpty() && MAP.get(new ResourceLocation(fallback())) != null)
            state = MAP.get(new ResourceLocation(fallback())).forType(type, variant).get(r);

        for(IProperty prop : parent.getPropertyKeys())
            if(state.getPropertyKeys().contains(prop))
                state = state.withProperty(prop, parent.getValue(prop));

        return state;

    }

    public enum Type {
        FLOOR, WALL, LAMP, RUNE, GEM, PILLAR, KEY_STONE,
        STAIRS, STAIRS_WALL, SLAB,
        TORCH, BARS, LADDER,
        PLANT, GRASS, DIRT, LOG, LOG2, LEAVES, LEAVES2,
        FLUID_HARMFUL, FLUID_SAVE,
        PLANKS, SLAB_PLANKS, STAIRS_PLANKS
    }

    public abstract double weight();

    public abstract int variantCount();

    public static IBlockState stairs(BlockPlanks.EnumType wood) {

        switch (wood) {
            case OAK:
                return (Blocks.OAK_STAIRS.getDefaultState());
            case BIRCH:
                return (Blocks.BIRCH_STAIRS.getDefaultState());
            case JUNGLE:
                return (Blocks.JUNGLE_STAIRS.getDefaultState());
            case DARK_OAK:
                return (Blocks.DARK_OAK_STAIRS.getDefaultState());
            case SPRUCE:
                return (Blocks.SPRUCE_STAIRS.getDefaultState());
            case ACACIA:
                return (Blocks.ACACIA_STAIRS.getDefaultState());
            default:
                return Blocks.AIR.getDefaultState();
        }
    }

}
