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

    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecated")
    public boolean causesSuffocation(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecated")
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @SuppressWarnings("deprecated")
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState next, Direction direction) {
        if (!hideNeighboors) return super.isSideInvisible(state, next, direction);
        return next.getBlock() == this || super.isSideInvisible(state, next, direction);
    }

}
