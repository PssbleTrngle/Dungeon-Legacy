package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.ChestBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.tile.ChestTile;

import javax.annotation.Nullable;

public class PlaceholderChest extends ChestBlock implements IPlaceholder {

    private final TemplateType type;

    public PlaceholderChest(TemplateType type) {
        this(type, ChestTile.TYPE);
    }

    public PlaceholderChest(TemplateType type, RegistryObject<TileEntityType<? extends ChestTile>> tile) {
        super(TemplateBlock.PROPERTIES(), tile::get);
        this.type = type;
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }

	@Override
	public TileEntity createNewTileEntity(@Nullable IBlockReader world) {
		return new ChestTile();
    }

}