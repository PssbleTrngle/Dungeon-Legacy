package possibletriangle.dungeon.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CurseNodeProcessor extends WalkNodeProcessor {

    @Override
    protected PathNodeType getPathNodeTypeRaw(IBlockAccess world, int x, int y, int z) {


        BlockPos blockpos = new BlockPos(x, y, z);
        IBlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if(block == Blocks.REDSTONE_WIRE || block.getRegistryName().toString().equals("quark:glowstone_dust_block"))
            return PathNodeType.FENCE;

        return super.getPathNodeTypeRaw(world, x, y, z);
    }

    @Override
    public boolean getCanSwim() {
        return false;
    }
}
