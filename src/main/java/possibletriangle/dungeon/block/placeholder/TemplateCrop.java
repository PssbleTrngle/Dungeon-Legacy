package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class TemplateCrop extends CropsBlock implements IPlaceholder {

    private final TemplateType type;

    public TemplateCrop(TemplateType type) {
        super(TemplateBlock.PROPERTIES().doesNotBlockMovement());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() instanceof FarmlandBlock;
    }

}