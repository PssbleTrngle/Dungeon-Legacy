package possibletriangle.dungeon;

import com.sun.jmx.remote.internal.IIOPProxy;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.dungeon.common.CommonProxy;
import possibletriangle.dungeon.common.block.Template;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.room.Room;
import possibletriangle.dungeon.common.world.room.RoomDev;

import java.util.Arrays;

@Mod(DungeonMod.MODID)
public class DungeonMod {

    public static final String MODID = "dungeon";

    public static CommonProxy proxy = DistExecutor.runForDist(() -> CommonProxy::new, () -> CommonProxy::new);

    private static final Logger LOGGER = LogManager.getLogger();

    public DungeonMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("The dungeon is taking over");
        proxy.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {
            new RegistryBuilder<Room>()
                    .setName(new ResourceLocation(MODID, "room"))
                    .setType(Room.class)
                    .create();
        }

        @SubscribeEvent
        public static void onRoomsRegistry(final RegistryEvent.Register<Room> event) {
            event.getRegistry().register(new RoomDev().setRegistryName("dev"));
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            Arrays.stream(Template.values())
                    .map(Template::block)
                    .forEach(event.getRegistry()::register);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            Item.Properties properties = new Item.Properties()
                    .group(ItemGroup.BUILDING_BLOCKS);

            Arrays.stream(Template.values())
                    .map(Template::block)
                    .map(b -> new BlockItem(b, properties).setRegistryName(b.getRegistryName()))
                    .forEach(event.getRegistry()::register);
        }

    }
}
