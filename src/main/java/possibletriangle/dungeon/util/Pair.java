package possibletriangle.dungeon.util;

/**
 * This class exists because forge seems to not support the javafx.util package
 */
public class Pair<F,S> {

    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public <N> Pair<F,N> withSecond(N newSecond) {
        return new Pair<>(first, newSecond);
    }

    public <N> Pair<N,S> withFirst(N newFirst) {
        return new Pair<>(newFirst, second);
    }

}
