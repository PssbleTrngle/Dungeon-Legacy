package possibletriangle.dungeon.palette.providers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.Random;

public abstract class StateProviderParent extends StateProvider {

    public abstract IStateProvider get(int variant, Random random);

    @Override
    protected final BlockState applyChildren(BlockState in, int variant, Random random) {
        return get(variant, random).apply(in, variant, random);
    }

    @Override
    public final Block getBlock(int variant, Random random) {
        return get(variant, random).getBlock(variant, random);
    }
}
