package possibletriangle.dungeon.common.block.placeholder;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class TemplateFarmland extends FarmlandBlock implements IPlaceholder {

    private final Type type;

    public TemplateFarmland(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
        setDefaultState(super.getDefaultState().with(MOISTURE, 7));
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {}

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {}

    @Override
    public Type getType() {
        return this.type;
    }

}