package possibletriangle.dungeon.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import possibletriangle.dungeon.common.world.room.StateProvider;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public class Fallback implements StateProvider {

    private final StateProvider[] providers;

    @SafeVarargs
    public Fallback(Supplier<BlockState>... suppliers) {
        this(Arrays.stream(suppliers).map(s -> (StateProvider) i -> s.get()).toArray(StateProvider[]::new));
    }
    public Fallback(StateProvider... providers) {
        this.providers = providers;
    }

    @Nullable
    @Override
    public BlockState apply(Integer variant) {
        for(StateProvider provider : providers) {
            BlockState state = provider.apply(variant);
            if(state != null) return state;
        }

        return null;
    }

}
