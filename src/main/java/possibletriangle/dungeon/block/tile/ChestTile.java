package possibletriangle.dungeon.block.tile;

import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.MetadataBlock;
import possibletriangle.dungeon.block.placeholder.PlaceholderChest;

public class ChestTile extends ChestTileEntity {

    public static final RegistryObject<TileEntityType<? extends ChestTile>> TYPE = DungeonMod.TILES.register("chest", () -> TileEntityType.Builder.create(
            ChestTile::new, PlaceholderChest.CHEST.get()).build(null)
    );

    public ChestTile() {
        super(TYPE.get());
    }

    public ChestTile(TileEntityType<? extends ChestTile> type) {
        super(type);
    }

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
	}

}