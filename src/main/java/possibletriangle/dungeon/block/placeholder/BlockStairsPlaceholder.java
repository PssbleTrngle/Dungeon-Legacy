package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockStairsPlaceholder extends BlockStairs implements IPlaceholder {

    private final Pallete.Type type;

    public BlockStairsPlaceholder(Pallete.Type type, IBlockState modelState) {
        super(modelState);

        String id = type.name().toLowerCase();
        this.type = type;
        ResourceLocation name = new ResourceLocation(Dungeon.MODID, "placeholder_" + id);

        setRegistryName(name);
        setUnlocalizedName(name.toString());
    }

    @Override
    public Pallete.Type getType() {
        return type;
    }
}
