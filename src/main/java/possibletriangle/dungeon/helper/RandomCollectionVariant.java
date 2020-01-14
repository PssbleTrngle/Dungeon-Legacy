package possibletriangle.dungeon.helper;

import java.util.Arrays;
import java.util.function.Function;

public class RandomCollectionVariant<T> extends RandomCollection<Function<Integer,T>> {

    @SuppressWarnings("unchecked")
    public RandomCollectionVariant(T... ts) {
        super(Arrays.stream(ts)
                .map(t -> (Function<Integer,T>) i -> t)
                .toArray(Function[]::new)
        );
    }

}
