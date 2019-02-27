package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.material.Material;
import possibletriangle.dungeon.block.BlockMod;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockPlaceholder extends BlockMod implements IPlaceholder {

    private final Pallete.Type type;

    public BlockPlaceholder(Pallete.Type type) {
        super("placeholder_" + type.name().toLowerCase(), Material.ROCK);

        this.type = type;
    }

    @Override
    public Pallete.Type getType() {
        return type;
    }
}
