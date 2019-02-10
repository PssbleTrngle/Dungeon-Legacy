package possibletriangle.dungeon.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.pallete.Pallete;

public class BlockMod extends Block {

    private final ResourceLocation name;

    public BlockMod(String id,Material mat) {
        super(mat);

        this.name = new ResourceLocation(Dungeon.MODID, id);

        setRegistryName(name);
        setUnlocalizedName(name.toString());
    }

}
