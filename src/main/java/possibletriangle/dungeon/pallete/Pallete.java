package possibletriangle.dungeon.pallete;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;
import possibletriangle.dungeon.generator.RandomCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Pallete {

    public String[] requiredMods() {
        return new String[0];
    }

    public static Pallete random(Random r) {
        return LIST.next(r);
    }

    public abstract String getName();

    public Pallete() {

        boolean b = true;
        for (String mod : requiredMods())
            b = b && Loader.isModLoaded(mod);

        if (b) {
            LIST.add(weight(), this);
            MAP.put(new ResourceLocation(getName()), this);
        }
    }

    public static final RandomCollection<Pallete> LIST = new RandomCollection<>();
    public static final HashMap<ResourceLocation, Pallete> MAP = new HashMap<>();

    public abstract Replacer forType(Type type);

    public final IBlockState get(Type type, IBlockState parent, Random r) {

        IBlockState state = forType(type).get(r);

        for(IProperty prop : parent.getPropertyKeys())
            if(state.getPropertyKeys().contains(prop))
                state = state.withProperty(prop, parent.getValue(prop));

        return state;

    }

    public enum Type {
        FLOOR, WALL, LAMP, RUNE, GEM,
        PILLAR,
        STAIRS, STAIRS_WALL, SLAB,
        TORCH, BARS, LADDER,
        PLANT, GRASS, LOG, LOG2, LEAVES, LEAVES2,
        FLUID_HARMFUL, FLUID_SAVE
    }

    public abstract double weight();

}
