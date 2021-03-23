package possibletriangle.dungeon.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.data.blockstate.Obelisks;
import possibletriangle.dungeon.data.blockstate.Others;
import possibletriangle.dungeon.data.blockstate.Placeholders;
import possibletriangle.dungeon.data.blockstate.RedstoneLinks;
import possibletriangle.dungeon.data.model.item.ItemBlocks;
import possibletriangle.dungeon.data.model.item.Scrolls;
import possibletriangle.dungeon.data.loot.CanBreak;
import possibletriangle.dungeon.data.loot.CanPlaceOn;
import possibletriangle.dungeon.data.loot.DungeonLoot;
import possibletriangle.dungeon.data.loot.HideFlags;
import possibletriangle.dungeon.data.model.item.SimpleItems;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> FUNCTIONS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, DungeonMod.ID);

     public static final LootFunctionType CAN_BREAK =  registerFunction("can_break", new CanBreak.Serializer());
     public static final LootFunctionType CAN_PLACE_ON = registerFunction("can_place_on", new CanPlaceOn.Serializer());
     public static final LootFunctionType HIDE_FLAGS = registerFunction("hide_flags", new HideFlags.Serializer());

     private static LootFunctionType registerFunction(String name, LootFunction.Serializer<?> serializer) {
         return Registry.register(Registry.field_239694_aZ_, new ResourceLocation(DungeonMod.ID, name), new LootFunctionType(serializer));
     }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper filehelper = event.getExistingFileHelper();

        generator.addProvider(new DungeonLoot(generator));

        generator.addProvider(new Placeholders(generator, filehelper));
        generator.addProvider(new Others(generator, filehelper));
        generator.addProvider(new RedstoneLinks(generator, filehelper));
        generator.addProvider(new Obelisks(generator, filehelper));

        generator.addProvider(new ItemBlocks(generator, filehelper));
        generator.addProvider(new SimpleItems(generator, filehelper));
        generator.addProvider(new Scrolls(generator, filehelper));
    }
}
