package possibletriangle.dungeon;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import possibletriangle.dungeon.block.ObeliskBlock;
import possibletriangle.dungeon.block.tile.ChestTile;
import possibletriangle.dungeon.block.tile.MetadataTile;
import possibletriangle.dungeon.block.tile.ObeliskTile;
import possibletriangle.dungeon.block.tile.render.ChestTESR;
import possibletriangle.dungeon.block.tile.render.MetadataTESR;
import possibletriangle.dungeon.entity.GrenadeEntity;
import possibletriangle.dungeon.item.ScrollItem;
import possibletriangle.dungeon.item.spells.Spell;
import possibletriangle.dungeon.palette.PaletteLoader;
import possibletriangle.dungeon.proxy.ClientProxy;
import possibletriangle.dungeon.block.placeholder.TemplateBlock;
import possibletriangle.dungeon.command.DungeonCommand;
import possibletriangle.dungeon.proxy.CommonProxy;
import possibletriangle.dungeon.world.DungeonWorldType;
import possibletriangle.dungeon.world.generator.DungeonSettings;
import possibletriangle.dungeon.world.structure.StructureLoader;
import possibletriangle.dungeon.world.structure.StructureType;
import possibletriangle.dungeon.world.structure.Structures;
import possibletriangle.dungeon.world.structure.metadata.condition.ConditionType;
import possibletriangle.dungeon.world.structure.rooms.HallwayMaze;

import java.util.stream.Stream;

@Mod(DungeonMod.ID)
public class DungeonMod {

    public static final String ID = "dungeon";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ID);

    public static final DeferredRegister<StructureType> STRUCTURES = DeferredRegister.create(StructureType.class, ID);
    public static final DeferredRegister<ConditionType> CONDITIONS = DeferredRegister.create(ConditionType.class, ID);
    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(Spell.class, ID);

    public static final ItemGroup GROUP = new ItemGroup("dungeon") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(TemplateBlock.WALL, 1);
        }
    };

    public static final Logger LOGGER = LogManager.getLogger();

    public DungeonMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("The dungeon is taking over");
        IEventBus BUS = FMLJavaModLoadingContext.get().getModEventBus();
        Stream.of(ITEMS, BLOCKS, TILES, ENTITIES, STRUCTURES, CONDITIONS, SPELLS).forEach(r -> r.register(BUS));
        new DungeonWorldType();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt) {
        ItemRenderer i = Minecraft.getInstance().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(GrenadeEntity.class, manager -> new SpriteRenderer<>(manager, i));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(MetadataTile.class, new MetadataTESR());
        ClientRegistry.bindTileEntityRenderer(ChestTile.class, new ChestTESR());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void blockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((s, w, p, i) -> {
            if (i != 1) return -1;
            return ObeliskBlock.getTE(w, p).map(ObeliskTile::getColor).orElseGet(() -> ObeliskBlock.State.INVALID.color);
        }, ObeliskBlock.OBELISK);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((s, i) -> i == 1 ? ScrollItem.getSpell(s).getColor() : -1, ScrollItem.SCROLL);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerWillStart(final FMLServerAboutToStartEvent event) {

        IReloadableResourceManager manager = (IReloadableResourceManager) event.getServer().getDataPackRegistries().func_240970_h_();

        Structures.clear();
        StructureType.values().stream()
                .map(StructureLoader::new)
                .forEach(manager::addReloadListener);
        Structures.register(new HallwayMaze(), StructureType.HALLWAY);

        manager.addReloadListener(new PaletteLoader(event.getServer().getNetworkTagManager()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerStarting(final RegisterCommandsEvent event) {
        DungeonCommand.register(event.getDispatcher());
    }
}
