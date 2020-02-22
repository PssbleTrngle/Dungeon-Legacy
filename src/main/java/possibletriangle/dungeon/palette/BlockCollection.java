package possibletriangle.dungeon.palette;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class BlockCollection extends StateProviderParent {

    private final RandomCollection<IStateProvider> collection = new RandomCollection<>();

    public void add(IStateProvider provider, float weight) {
        this.collection.add(provider, weight);
    }

    @Override
    public boolean isValid() {
        return !collection.empty();
    }

    @Override
    public IStateProvider get(int variant, Random random) {
        return this.collection.next(random).orElseThrow(() -> new NullPointerException("Trying to access empty collection"));
    }

    @Override
    public Stream<BlockState> allBlocks() {
        return this.collection.all().stream().map(IStateProvider::allBlocks).flatMap(Function.identity());
    }
}
