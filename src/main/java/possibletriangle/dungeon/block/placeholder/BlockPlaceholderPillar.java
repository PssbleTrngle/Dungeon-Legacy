package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockPlaceholderPillar extends BlockRotatedPillar implements IPlaceholder {

    private final Pallete.Type type;

    public BlockPlaceholderPillar(Pallete.Type type) {
        super(Material.ROCK);

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
