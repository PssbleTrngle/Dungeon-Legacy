package possibletriangle.dungeon.helper;

public class Variant<T> implements Function<Integer,T> {

    private final T[] values;

    public Variant(T... values) {
        this.values = values;
    }

    public T apply(int variant) {
        return values[Math.max(variant, 0) % values.length];
    }

}
