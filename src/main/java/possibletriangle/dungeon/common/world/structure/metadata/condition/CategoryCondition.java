package possibletriangle.dungeon.common.world.structure.metadata.condition;

import java.util.Arrays;
import java.util.function.BiPredicate;

public class CategoryCondition extends Condition<String[]> {

    public CategoryCondition(String[] allow, String[] reject, String[] required) {
        super(allow, reject, required);
    }

    @Override
    protected BiPredicate<String, String[]> getPredicate() {
        return (cat, given) -> Arrays.asList(given).contains(cat);
    }
}
