package possibletriangle.dungeon.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import possibletriangle.dungeon.common.world.room.StateProvider;

import java.util.Arrays;

public class BlockCollection extends RandomCollection<StateProvider> {

    public BlockCollection(StateProvider... providers) {
        super(providers);
    }

    public BlockCollection(Block... blocks) {
        this(Arrays.stream(blocks)
                .map(Block::getDefaultState)
                .toArray(BlockState[]::new)
        );
    }

    public BlockCollection(BlockState... states) {
        this(Arrays.stream(states)
                .map(t -> (StateProvider) i -> t)
                .toArray(StateProvider[]::new)
        );
    }

    @Override
    public BlockCollection add(StateProvider stateProvider, float weight) {
        super.add(stateProvider, weight);
        return this;
    }

    public BlockCollection add(BlockState state, float weight) {
        return this.add(i -> state, weight);
    }

    public BlockCollection add(Block block, float weight) {
        return this.add(block.getDefaultState(), weight);
    }
}
