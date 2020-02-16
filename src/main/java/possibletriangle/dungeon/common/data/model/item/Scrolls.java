package possibletriangle.dungeon.common.data.model.item;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.item.ScrollItem;

public class Scrolls extends ItemModelProvider {

    public Scrolls(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private ItemModelBuilder getBase(String path) {
        return getBuilder(path)
                .parent(getExistingFile(new ResourceLocation("minecraft", "item/handheld")))
                .texture("layer0", modLoc("item/scroll"));
    }

    @Override
    protected void registerModels() {

        getBase("item/scroll_empty");

        getBase("item/scroll")
                .texture("layer1", modLoc("item/scroll_symbol"))
                .override().predicate(ScrollItem.MODEL_EMPTY, 1)
                    .model(getExistingFile(modLoc("item/scroll_empty")));

    }



    @Override
    public String getName() {
        return "Scroll";
    }
}
