package possibletriangle.dungeon.palette;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public abstract class StateProvider implements IStateProvider {

    private PropertyProvider[] properties = new PropertyProvider[0];

    StateProvider setProperties(PropertyProvider[] properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public final BlockState apply(BlockState in, int variant, Random random) {
        return Arrays.stream(properties).reduce(applyChildren(in, variant, random), (s, p) -> p.apply(s, random), (a, b) -> a);
    }

    protected BlockState applyChildren(BlockState in, int variant, Random random) {
        return in;
    }

}
