package possibletriangle.dungeon.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.MODID)
public class TemplateTransparent extends TemplateBlock {

    private final boolean hideNeighboors;

    public TemplateTransparent(Type type, boolean hideNeighboors) {
        super(type);
        this.hideNeighboors = hideNeighboors;
    }

    public TemplateTransparent(Type type) {
        this(type, false);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    public boolean causesSuffocation(BlockState p_220060_1_, IBlockReader p_220060_2_, BlockPos p_220060_3_) {
        return false;
    }

    public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState next, Direction direction) {
        if (!hideNeighboors) return super.isSideInvisible(state, next, direction);
        return next.getBlock() == this || super.isSideInvisible(state, next, direction);
    }

}
