package possibletriangle.dungeon.helper;

import java.util.function.Function;

public class Fallback<T> implements Function<Integer,T> {

    private final Function<Integer,T>[] suppliers;

    public Fallback(Function<Integer,T>... suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public T apply(Integer variant) {
        for(Function<Integer,T> supplier : suppliers) {
            T t = supplier.apply(variant);
            if(t != null) return t;
        }

        return null;
    }

}
