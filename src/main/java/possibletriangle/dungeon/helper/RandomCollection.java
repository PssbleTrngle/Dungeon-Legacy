package possibletriangle.dungeon.helper;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<T> {

    private final NavigableMap<Double, T> map = new TreeMap<>();
    private double total = 0;

    @SafeVarargs
    public RandomCollection(T... ts) {
        for(T t : ts)
            add(t, 1);
    }

    public RandomCollection<T> add(T t, float weight) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, t);
        return this;
    }

    public T next(Random random) {
        if(this.total == 0) return null;
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public int size() {
        return map.values().size();
    }

    public T[] all() {
        return (T[]) map.values().toArray();
    }

}
