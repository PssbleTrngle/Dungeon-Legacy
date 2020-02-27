package possibletriangle.dungeon.block.tile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TrappedChestTile extends ChestTile {

    @ObjectHolder("dungeon:trapped_chest")
    public static final TileEntityType<TrappedChestTile> TYPE = null;

    public TrappedChestTile() {
        super(TYPE);
    }

    @Override
	protected void onOpenOrClose() {
		super.onOpenOrClose();
		this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockState().getBlock());
	}

}