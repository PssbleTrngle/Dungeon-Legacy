package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class TemplateCrop extends CropsBlock implements IPlaceholder {

    private final Type type;

    public TemplateCrop(Type type) {
        super(TemplateBlock.PROPERTIES().doesNotBlockMovement());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() instanceof FarmlandBlock;
    }

}