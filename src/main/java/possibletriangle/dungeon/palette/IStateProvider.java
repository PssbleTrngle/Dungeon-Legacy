package possibletriangle.dungeon.palette;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Random;
import java.util.stream.Stream;

public interface IStateProvider {

    Block getBlock(int variant, Random random);

    default BlockState apply(BlockState in, int variant, Random random)  {
        return in;
    }

    /**
     * Used to figure out which blocks are used for the seal
     */
    default Stream<BlockState> allBlocks() {
        return Stream.of();
    }

    default boolean isValid() {
        return true;
    }

}
