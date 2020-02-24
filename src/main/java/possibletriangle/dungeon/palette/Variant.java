package possibletriangle.dungeon.palette;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class Variant extends StateProviderParent {

    private final IStateProvider[] variants;

    public Variant(IStateProvider[] variants) {
        this.variants = Arrays.stream(variants).filter(IStateProvider::isValid).toArray(IStateProvider[]::new);
    }

    @Override
    public boolean isValid() {
        return variants.length > 0;
    }

    @Override
    public IStateProvider get(int variant, Random random) {
        return this.variants[Math.max(0, variant) % this.variants.length];
    }

    @Override
    public Stream<BlockState> allBlocks() {
        return Arrays.stream(variants)
                .map(IStateProvider::allBlocks)
                .flatMap(Function.identity());
    }
}
