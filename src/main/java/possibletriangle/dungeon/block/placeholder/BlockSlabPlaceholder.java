package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockSlabPlaceholder extends BlockSlab implements IPlaceholder {

    private final Pallete.Type type;
    private final boolean doble;

    public BlockSlabPlaceholder(Pallete.Type type, boolean doble) {
        super(Material.ROCK);

        String id = type.name().toLowerCase();
        this.type = type;
        this.doble = doble;
        ResourceLocation name = new ResourceLocation(Dungeon.MODID, "placeholder_" + id + (doble ? "_double" : ""));

        setRegistryName(name);
        setUnlocalizedName(name.toString());
    }

    @Override
    public boolean isDouble() {
        return doble;
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return TYPE;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return Type.NONE;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF, TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF) == EnumBlockHalf.TOP ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HALF, meta > 0 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return getUnlocalizedName();
    }

    @Override
    public Pallete.Type getType() {
        return type;
    }

    public enum Type implements IStringSerializable {
        NONE;

        @Override
        public String getName() {
            return "default";
        }

        @Override
        public String toString() {
            return "default";
        }
    }

    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("variant", Type.class);

}
