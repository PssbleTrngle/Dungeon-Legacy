package possibletriangle.dungeon.common.world.room;

import jdk.nashorn.internal.ir.BlockStatement;
import net.minecraft.block.BlockState;

import java.util.function.Function;

public interface StateProvider extends Function<Integer, BlockState> {}
