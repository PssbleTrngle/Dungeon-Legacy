package possibletriangle.dungeon.common.block;

import net.minecraft.block.*;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;

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