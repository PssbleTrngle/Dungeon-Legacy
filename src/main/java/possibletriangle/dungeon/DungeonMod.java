package possibletriangle.dungeon;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.dungeon.common.CommonProxy;
import possibletriangle.dungeon.common.DungeonCommand;
import possibletriangle.dungeon.common.block.TemplateBlock;

@Mod(DungeonMod.MODID)
public class DungeonMod {

    public static final ItemGroup GROUP = new ItemGroup("dungeon") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(TemplateBlock.WALL, 1);
        }
    };

    public static final String MODID = "dungeon";

    public static CommonProxy proxy = DistExecutor.runForDist(() -> CommonProxy::new, () -> CommonProxy::new);

    public static final Logger LOGGER = LogManager.getLogger();

    public DungeonMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("The dungeon is taking over");
        proxy.init(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerWillStart(final FMLServerAboutToStartEvent event) {
        proxy.reloadRooms(event.getServer().getResourceManager());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerStarting(final FMLServerStartingEvent  event) {
        DungeonCommand.register(event.getCommandDispatcher());
    }
}
