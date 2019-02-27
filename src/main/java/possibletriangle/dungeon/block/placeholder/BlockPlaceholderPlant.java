package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockPlaceholderPlant extends BlockBush implements IPlaceholder {

    private final Pallete.Type type;

    public BlockPlaceholderPlant(Pallete.Type type) {
        super(Material.PLANTS);

        String id = type.name().toLowerCase();
        this.type = type;
        ResourceLocation name = new ResourceLocation(Dungeon.MODID, "placeholder_" + id);

        setRegistryName(name);
        setUnlocalizedName(name.toString());
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.isNormalCube();
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return state.isSideSolid(world, pos, direction);
    }

    @Override
    public Pallete.Type getType() {
        return type;
    }
}
