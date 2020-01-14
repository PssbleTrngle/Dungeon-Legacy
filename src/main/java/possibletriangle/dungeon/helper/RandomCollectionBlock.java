package possibletriangle.dungeon.helper;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import net.minecraft.block.Block;

public class RandomCollectionBlock extends RandomCollectionVariant<BlockState> {

    public RandomCollectionVariant(Block... blocks) {
        super(Arrays.stream(blocks).map(b -> (Function<Integer,T>) b::getDefaultState);
    }

}
