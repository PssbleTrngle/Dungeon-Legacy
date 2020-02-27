package possibletriangle.dungeon.palette;


import possibletriangle.dungeon.palette.providers.IStateProvider;
import possibletriangle.dungeon.palette.providers.StateProvider;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StateProviderSupplier {
    private float weight;
    private final Supplier<Optional<Stream<IStateProvider>>> supplier;
    private PropertyProvider[] properties = null;

    public StateProviderSupplier(Supplier<Optional<Stream<IStateProvider>>> supplier) {
        this.supplier = supplier;
    }

    float getWeight() {
        return weight;
    }

    void setWeight(float weight) {
        this.weight = weight;
    }

    void setProperties(PropertyProvider[] properties) {
        this.properties = properties;
    }

    public Optional<Stream<IStateProvider>> supply() {
        return this.supplier.get().map(s -> s.peek(p -> {
            if(p instanceof StateProvider) ((StateProvider) p).setProperties(properties);
        }));
    }

    public <T> Optional<Stream<T>> map(Function<IStateProvider,T> consumer) {
        return this.supply().map(s -> s.map(consumer));
    }

    public void forEach(Consumer<IStateProvider> consumer) {
        this.supply().ifPresent(s -> s.forEach(consumer));
    }
}