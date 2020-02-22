package possibletriangle.dungeon.palette;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.dispenser.IPosition;
import net.minecraft.state.IProperty;
import org.w3c.dom.Element;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
            return findProperty(in.getBlock()).map(p -> this.value.apply(r, p).map(v -> in.with(p, v))
        ).flatMap(Function.identity()).orElse(in);
    }

}
