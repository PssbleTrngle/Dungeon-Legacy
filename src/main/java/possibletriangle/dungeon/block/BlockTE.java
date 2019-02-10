package possibletriangle.dungeon.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;

import javax.annotation.Nullable;

public abstract class BlockTE<TE extends TileEntity> extends BlockMod implements ITileEntityProvider {

    public BlockTE(String id, Material mat) {
        super(id, mat);
    }

    public TE getTE(BlockPos pos, IBlockAccess world) {

        if(pos == null || world == null)
            return null;

        TileEntity te = world.getTileEntity(pos);
        if(te == null) return null;
        return (TE) te;

    }
}
