package possibletriangle.dungeon;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.block.Type;
import possibletriangle.dungeon.common.world.room.Room;
import possibletriangle.dungeon.common.world.room.RoomHallway;
import possibletriangle.dungeon.common.world.room.RoomStructure;
import possibletriangle.dungeon.common.world.wall.Wall;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Arrays;
import java.util.function.Supplier;

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
        proxy.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}
}
