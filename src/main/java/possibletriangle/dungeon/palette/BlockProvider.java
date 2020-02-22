package possibletriangle.dungeon.palette;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class BlockProvider extends StateProvider {

    private final Block block;

    public BlockProvider(Block block) {
        this.block = block;
    }

    @Override
    public Block getBlock(int variant, Random random) {
        return block;
    }

    @Override
    public Stream<BlockState> allBlocks() {
        return Stream.of(block.getDefaultState());
    }
}
