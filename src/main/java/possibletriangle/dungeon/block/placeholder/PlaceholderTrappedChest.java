package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.TrappedChestBlock;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.tile.TrappedChestTile;
import possibletriangle.dungeon.block.tile.render.ChestTESR;

import javax.annotation.Nullable;

@ObjectHolder(DungeonMod.ID)
public class PlaceholderTrappedChest extends TrappedChestBlock implements IPlaceholder {

    private final Type type;
	public final ResourceLocation modelNormal, modelDouble;

    public PlaceholderTrappedChest(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;

        final String name = "placeholder_" + type.name().toLowerCase();
        setRegistryName(name);
		this.modelNormal = new ResourceLocation(DungeonMod.ID, "textures/model/chest/" + name + ".png");
		this.modelDouble = new ResourceLocation(DungeonMod.ID, "textures/model/chest/" + name + "_double.png");
    }

    @Override
    public Type getType() {
        return this.type;
    }

	@Override
	public TileEntity createNewTileEntity(@Nullable IBlockReader world) {
		return new TrappedChestTile();
    }
	
    @Override
	@OnlyIn(Dist.CLIENT)
	public void modifyItem(Item.Properties props) {
		props.setTEISR(() -> () -> new ItemStackTileEntityRenderer() {
			private final TileEntity tile = createNewTileEntity(null);
			
			@Override
			public void renderByItem(ItemStack itemStackIn) {
				ChestTESR.forceNormal = modelNormal;
				ChestTESR.forceDouble = modelDouble;
				TileEntityRendererDispatcher.instance.renderAsItem(tile);
			}
		});
	}

}