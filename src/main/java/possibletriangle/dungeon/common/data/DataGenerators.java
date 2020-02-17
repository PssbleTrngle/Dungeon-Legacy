package possibletriangle.dungeon.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import possibletriangle.dungeon.common.data.blockstate.Placeholders;
import possibletriangle.dungeon.common.data.model.item.ItemBlocks;
import possibletriangle.dungeon.common.data.model.item.Scrolls;
import possibletriangle.dungeon.common.data.loot.CanBreak;
import possibletriangle.dungeon.common.data.loot.CanPlaceOn;
import possibletriangle.dungeon.common.data.loot.DungeonLoot;
import possibletriangle.dungeon.common.data.loot.HideFlags;
import possibletriangle.dungeon.common.data.model.item.SimpleItems;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    static {
        LootFunctionManager.registerFunction(new CanBreak.Serializer());
        LootFunctionManager.registerFunction(new CanPlaceOn.Serializer());
        LootFunctionManager.registerFunction(new HideFlags.Serializer());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper filehelper = event.getExistingFileHelper();

        generator.addProvider(new DungeonLoot(generator));
        generator.addProvider(new ItemBlocks(generator, filehelper));
        generator.addProvider(new SimpleItems(generator, filehelper));
        generator.addProvider(new Scrolls(generator, filehelper));

        generator.addProvider(new Placeholders(generator, filehelper));
    }
}
