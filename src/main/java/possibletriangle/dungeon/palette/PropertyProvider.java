package possibletriangle.dungeon.palette;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.state.Property;

import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PropertyProvider {

    private final BiFunction<Random, Property<?>,Optional<Comparable>> value;
    public final String key;

    public PropertyProvider(String key, BiFunction<Random,Property<?>,Optional<Comparable>> value) {
        this.value = value;
        this.key = key;
    }

    private Optional<Property<?>> findProperty(Block block) {
        return block.getDefaultState().func_235904_r_().stream()
                .filter(p -> p.getName().equalsIgnoreCase(this.key))
                .findFirst();
    }

    public BlockState apply(BlockState in, Random r) {
        return findProperty(
                in.getBlock()).map(p -> this.value.apply(r, p).map(v -> in.with(p, v))
        ).flatMap(Function.identity()).orElse(in);
    }

}
