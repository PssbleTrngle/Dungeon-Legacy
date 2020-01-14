package possibletriangle.dungeon.helper;

public class Fallback<T> implements Function<Integer,T> {

    private final Function<Integer,T>[] suppliers;

    public Fallback(Function<Integer,T>... suppliers) {
        this.suppliers = suppliers;
    }

    public T apply(int variant) {
        for(Function<Integer,T> supplier : suppliers) {
            T t = suppliers.apply(variant);
            if(t != null) return t;
        }

        return null;
    }

}
