package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockPlaceholderLeaves extends BlockPlaceholder {

    public BlockPlaceholderLeaves(Pallete.Type type) {
        super(type);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean causesSuffocation(IBlockState state)
    {
        return false;
    }

}
