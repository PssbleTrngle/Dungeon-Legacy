package possibletriangle.dungeon.data.model.item;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;

public class SimpleItems extends ItemModelProvider {

    public SimpleItems(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private void create(ResourceLocation item) {
        withExistingParent("item/" + item.getPath(), mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/" + item.getPath()));
    }

    private boolean exists(ResourceLocation item) {
        return existingFileHelper.exists(item, ResourcePackType.CLIENT_RESOURCES, ".png", "textures/item");
    }

    @Override
    protected void registerModels() {

        GameRegistry.findRegistry(Item.class).getKeys()
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
