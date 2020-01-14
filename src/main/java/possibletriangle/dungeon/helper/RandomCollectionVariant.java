package possibletriangle.dungeon.helper;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollectionVariant<T> extends RandomCollection<Function<Integer,T>> {

    public RandomCollectionVariant(T... ts) {
        super(Arrays.stream(ts).map(t -> (Function<Integer,T>) i -> t));
    }

}
