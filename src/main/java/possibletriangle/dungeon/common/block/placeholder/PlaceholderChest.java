package possibletriangle.dungeon.common.block.placeholder;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

import java.util.function.Function;

@ObjectHolder(DungeonMod.ID)
public class PlaceholderChest extends ChestBlock  implements IPlaceholder {

    private final Type type;
	public final ResourceLocation modelNormal, modelDouble;

    public PlaceholderChest(Type type) {
        super(TemplateBlock.PROPERTIES());
        this.type = type;

        final String name = "placeholder_" + type.name().toLowerCase();
        setRegistryName(name);
		this.modelNormal = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + name + ".png");
		this.modelDouble = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + name + "_double.png");
    }

    @Override
    public Type getType() {
        return this.type;
    }

	@Override
	public TileEntity createNewTileEntity(@Nullable IBlockReader world) {
		return new PlaceholderChestTile();
    }
	
    @Override
	@OnlyIn(Dist.CLIENT)
	public void modifyItem(Item.Properties props) {
		props.setTEISR(() -> () -> new ItemStackTileEntityRenderer() {
			private final TileEntity tile = createNewTileEntity(null);
			
			@Override
			public void renderByItem(ItemStack itemStackIn) {
				PlaceholderChestRender.forceNormal = modelNormal;
				PlaceholderChestRender.forceDouble = modelDouble;
				TileEntityRendererDispatcher.instance.renderAsItem(tile);
			}
		});
	}

}