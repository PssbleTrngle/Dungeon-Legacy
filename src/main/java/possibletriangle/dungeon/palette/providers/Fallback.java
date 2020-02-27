package possibletriangle.dungeon.palette.providers;

import net.minecraft.block.BlockState;

import java.util.*;
import java.util.stream.Stream;

public class Fallback extends StateProviderParent {

    private final Optional<IStateProvider> provider;

    public Fallback(IStateProvider[] blocks) {
        this.provider = Arrays.stream(blocks).filter(IStateProvider::isValid).findFirst();
    }

    @Override
    public boolean isValid() {
        return provider.isPresent();
    }

    @Override
    public IStateProvider get(int variant, Random random) {
        return provider.orElseThrow(() -> new NullPointerException("Trying to access empty Fallback"));
    }

    @Override
    public Stream<BlockState> allBlocks() {
        return provider
                .map(IStateProvider::allBlocks)
                .orElse(Stream.of());
    }
}
