package possibletriangle.dungeon.common.block.placeholder;

import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.ID)
public class TemplateGrass extends TemplateBlock {

    public TemplateGrass(Type type) {
        super(type);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction direction, IPlantable plant) {
        return true;
    }

}
