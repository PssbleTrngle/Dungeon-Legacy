package possibletriangle.dungeon.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import possibletriangle.dungeon.common.world.room.StateProvider;

import java.util.Arrays;

public class Variant implements StateProvider {

    private final BlockState[] values;

    public Variant(BlockState... values) {
        this.values = values;
    }

    public Variant(Block... values) {
        this(Arrays.stream(values).map(Block::getDefaultState).toArray(BlockState[]::new));
    }

    public BlockState apply(Integer variant) {
        return values[Math.max(variant, 0) % values.length];
    }

}
