package possibletriangle.dungeon.helper;

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

}
