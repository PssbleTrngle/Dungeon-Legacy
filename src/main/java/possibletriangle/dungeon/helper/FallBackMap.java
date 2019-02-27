package possibletriangle.dungeon.helper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

public class FallBackMap<K, V> implements Iterable<K> {

    private final HashMap<K, V> MAP = new HashMap<>();
    private final HashMap<K, V> FALLBACK = new HashMap<>();
    private final Predicate<V> predicate;

    public FallBackMap() {
        this(null);
    }

    public FallBackMap(@Nullable Predicate<V> predicate) {
        this.predicate = predicate;
    }

    public V put(K key, V value, V fallback) {

        FALLBACK.put(key, fallback);
        return MAP.put(key, value);

    }

    public V put(K key, V value) {
        return put(key, value, value);
    }

    public V fallback(K key) {
        return FALLBACK.get(key);
    }

    public V get(K key) {

        if(predicate == null || predicate.test(MAP.get(key)))
            return MAP.get(key);

        return fallback(key);

    }

    @Override
    public Iterator<K> iterator() {
        return MAP.keySet().iterator();
    }

}
