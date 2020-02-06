package possibletriangle.dungeon.common.data.loot;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    static {
        LootFunctionManager.registerFunction(new CanBreak.Serializer());
        LootFunctionManager.registerFunction(new CanPlaceOn.Serializer());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new DungeonLoot(generator));
    }
}
