package possibletriangle.dungeon.common.data.model.item;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;

public class ItemBlocks extends ItemModelProvider {

    public ItemBlocks(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private void create(ResourceLocation block) {
        getBuilder("item/" + block.getPath())
                .parent(getExistingFile(modLoc("block/" + block.getPath())));
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

    }

    @Override
    public String getName() {
        return "Block Items";
    }
}
