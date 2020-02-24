package possibletriangle.dungeon.palette;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PropertyProvider {

    private final BiFunction<Random,IProperty<?>,Optional<Comparable>> value;
    public final String key;

    public PropertyProvider(String key, BiFunction<Random,IProperty<?>,Optional<Comparable>> value) {
        this.value = value;
        this.key = key;
    }

    private Optional<IProperty<?>> findProperty(Block block) {
        return block.getDefaultState().getProperties().stream()
                .filter(p -> p.getName().equalsIgnoreCase(this.key))
                .findFirst();
    }

    public BlockState apply(BlockState in, Random r) {
        return (BlockState) findProperty(
                in.getBlock()).map(p -> this.value.apply(r, p).map(v -> in.with((IProperty) p, v))
        ).flatMap(Function.identity()).orElse(in);
    }

}
