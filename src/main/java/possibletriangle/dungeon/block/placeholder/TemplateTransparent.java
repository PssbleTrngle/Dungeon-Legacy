package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.ID)
public class TemplateTransparent extends TemplateBlock {

    private final boolean hideNeighboors;

    public TemplateTransparent(TemplateType type, boolean hideNeighboors) {
        super(type);
        this.hideNeighboors = hideNeighboors;
    }

    public static TemplateTransparent hide(TemplateType type) {
        return new TemplateTransparent(type, true);
    }

    public TemplateTransparent(TemplateType type) {
        this(type, false);
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

}
