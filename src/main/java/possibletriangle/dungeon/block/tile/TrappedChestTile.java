package possibletriangle.dungeon.block.tile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.placeholder.PlaceholderChest;

public class TrappedChestTile extends ChestTile {

    public static final RegistryObject<TileEntityType<? extends ChestTile>> TYPE = DungeonMod.TILES.register("trapped_chest", () -> TileEntityType.Builder.create(
            TrappedChestTile::new, PlaceholderChest.TRAPPED_CHEST.get()).build(null)
    );

    public TrappedChestTile() {
        super(TYPE.get());
    }

    @Override
    protected void onOpenOrClose() {
        super.onOpenOrClose();
        if (this.world != null) this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockState().getBlock());
    }

}