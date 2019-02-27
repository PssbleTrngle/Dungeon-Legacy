package possibletriangle.dungeon.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;

public class BlockMod extends Block {

    public BlockMod(String id,Material mat) {
        super(mat);

        ResourceLocation name = new ResourceLocation(Dungeon.MODID, id);

        setRegistryName(name);
        setUnlocalizedName(name.toString());
    }

}
