package possibletriangle.dungeon.helper;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private double total = 0;

    public RandomCollection(E... eeeees) {
        for(E eeeee : eeeees)
            add(1, eeeee);
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next(Random random) {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public int size() {
        return map.values().size();
    }

    public E[] all() {

        return (E[]) map.values().toArray();

    }

}