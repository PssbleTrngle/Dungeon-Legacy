package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.BlockTorch;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockPlaceholderTorch extends BlockTorch implements IPlaceholder {

    private final Pallete.Type type;

    public BlockPlaceholderTorch(Pallete.Type type) {

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
