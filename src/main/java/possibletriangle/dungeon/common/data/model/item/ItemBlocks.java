package possibletriangle.dungeon.common.data.model.item;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.ObeliskBlock;

import java.util.HashMap;

public class ItemBlocks extends ItemModelProvider {

    public ItemBlocks(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private void create(ResourceLocation block) {
        withExistingParent("item/" + block.getPath(), modLoc("block/" + block.getPath()));
    }

    private boolean exists(ResourceLocation block) {
        return existingFileHelper.exists(block, ResourcePackType.CLIENT_RESOURCES, ".json", "models/block");
    }

    @Override
    protected void registerModels() {

        GameRegistry.findRegistry(Block.class).getKeys()
                .stream()
                .filter(k -> k.getNamespace().equals(modid))
                .filter(this::exists)
                .forEach(this::create);

        new HashMap<Block, String>() {{
            put(ObeliskBlock.OBELISK, "_unclaimed");
        }}.forEach((block, suffix) -> {
            ResourceLocation r = block.getRegistryName();
            assert r != null;
            withExistingParent(
                    "item/" + block.getRegistryName().getPath(),
                    new ResourceLocation(r.getNamespace(), "block/" + r.getPath() + suffix)
            );
        });

    }

    @Override
    public String getName() {
        return "Block Items";
    }
}
