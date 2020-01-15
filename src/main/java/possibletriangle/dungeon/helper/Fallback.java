package possibletriangle.dungeon.helper;

import java.util.function.Function;
import java.util.function.Supplier;

public class Fallback implements StateProvider {

    private final StateProvider[] providers;

    public Fallback(Supplier<BlockState>... suppliers) {
        this.providers = Arrays.steam(suppliers).map(s -> (StateProvider) i -> s);
    }

    public Fallback(StateProvider... providers) {
        this.providers = providers;
    }

    @Override
    public T apply(Integer variant) {
        for(Function<Integer,T> provider : providers) {
            T t = provider.apply(variant);
            if(t != null) return t;
        }

        return null;
    }

}
