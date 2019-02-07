package possibletriangle.dungeon.block;

import net.minecraft.block.BlockSponge;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import possibletriangle.dungeon.block.BlockMod;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;

import javax.annotation.Nullable;

public class BlockSpawn extends BlockTE<TileEntitySpawn> {

    public BlockSpawn() {
        super("spawn");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySpawn();
    }
}
