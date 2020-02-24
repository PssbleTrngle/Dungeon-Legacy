package possibletriangle.dungeon.helper;

import java.util.NavigableMap;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.Collection;
import java.util.function.Predicate;

public class RandomCollection<T> {

    private final NavigableMap<Float, T> map = new TreeMap<>();
    private float total = 0;

    public RandomCollection(T... ts) {
        this.addAll(ts);
    }

    public RandomCollection<T> add(T t, float weight) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, t);
        return this;
    }

    public RandomCollection<T> addAll(RandomCollection<T> other) {
        other.map.forEach((weight, value) -> this.add(value, weight));
        return this;
    }

    @SafeVarargs
    public final RandomCollection<T> addAll(T... ts) {
        for(T t : ts)
            add(t, 1);
        return this;
    }

    public Optional<T> next(Random random) {
        if(this.total == 0) return Optional.empty();
        float value = random.nextFloat() * total;
        return Optional.of(map.higherEntry(value).getValue());
    }

    public int size() {
        return map.values().size();
    }

    public boolean empty() {
        return this.size() == 0;
    }

    public Collection<T> all() {
        return map.values();
    }

    public RandomCollection<T> filter(Predicate<T> by) {
        RandomCollection<T> filtered = new RandomCollection<>();
        map.forEach((weight, value) -> {
            if(by.test(value)) filtered.add(value, weight);
        });
        return filtered;
    }

    public void clear() {
        this.map.clear();
    }

}
