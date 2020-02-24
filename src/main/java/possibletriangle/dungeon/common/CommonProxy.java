package possibletriangle.dungeon.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryBuilder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.*;
import possibletriangle.dungeon.common.block.placeholder.Type;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.block.tile.ObeliskTile;
import possibletriangle.dungeon.common.entity.GrenadeEntity;
import possibletriangle.dungeon.common.item.ScrollItem;
import possibletriangle.dungeon.common.item.grenade.GrenadeFrost;
import possibletriangle.dungeon.common.item.grenade.GrenadeGravity;
import possibletriangle.dungeon.common.item.grenade.GrenadeSmoke;
import possibletriangle.dungeon.common.item.spells.BlackholeSpell;
import possibletriangle.dungeon.common.item.spells.ShockwaveSpell;
import possibletriangle.dungeon.common.item.spells.Spell;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.DungeonWorldType;
import possibletriangle.dungeon.common.world.room.HallwayMaze;
import possibletriangle.dungeon.common.world.room.StructureType;
import possibletriangle.dungeon.common.world.room.Structures;
import possibletriangle.dungeon.common.world.structure.StructureLoader;
import possibletriangle.dungeon.common.world.structure.metadata.condition.ConditionType;
import possibletriangle.dungeon.common.world.structure.metadata.condition.FloorCondition;
import possibletriangle.dungeon.common.world.structure.metadata.condition.ModCondition;
import possibletriangle.dungeon.common.world.structure.metadata.condition.PaletteCondition;
import possibletriangle.dungeon.palette.PaletteLoader;

import java.util.ArrayList;
import java.util.Arrays;

public class CommonProxy {

    public void init(final FMLCommonSetupEvent event) {
        new DungeonWorldType();
    }

    public void clientSetup(FMLClientSetupEvent event) {}

    public void reload(MinecraftServer server) {

        IReloadableResourceManager manager = server.getResourceManager();

        Structures.clear();
        StructureType.values().stream()
                .map(StructureLoader::new)
                .forEach(manager::addReloadListener);
        Structures.register(new HallwayMaze(), StructureType.HALLWAY);

        manager.addReloadListener(new PaletteLoader(server.getNetworkTagManager()));

    }

    public void openMetaTile(MetadataTile tile) {}

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void blockColors(ColorHandlerEvent.Block event) {
            event.getBlockColors().register((s,w,p,i) -> {
                if(i != 1) return -1;
                return ObeliskBlock.getTE(w, p).map(ObeliskTile::getColor).orElseGet(() -> ObeliskBlock.State.INVALID.color);
            }, ObeliskBlock.OBELISK);
        }

        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void itemColors(ColorHandlerEvent.Item event) {
            event.getItemColors().register((s,i) ->
                i == 1 ? ScrollItem.getSpell(s).getColor() : -1
            , ScrollItem.SCROLL);
        }

        @SubscribeEvent
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {

            new RegistryBuilder<StructureType>()
                    .setName(new ResourceLocation(DungeonMod.ID, "structure_type"))
                    .setType(StructureType.class)
                    .create();

            new RegistryBuilder<ConditionType>()
                    .setName(new ResourceLocation(DungeonMod.ID, "condition"))
                    .setType(ConditionType.class)
                    .create();

            new RegistryBuilder<Spell>()
                    .setName(new ResourceLocation(DungeonMod.ID, "spell"))
                    .setType(Spell.class)
                    .create();
        }

        @SubscribeEvent
        public static void onStructureTypeRegistry(final RegistryEvent.Register<StructureType> event) {
            event.getRegistry().registerAll(
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.ID, "room"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.ID, "hallway"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.ID, "boss"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.ID, "base"),
                new StructureType(StructureType.hasSize(2, 5, 4)).setRegistryName("door/small"),
                new StructureType(StructureType.hasSize(2, 5, 5)).setRegistryName("door/big"),
                new StructureType(StructureType.hasSize(5, DungeonSettings.FLOOR_HEIGHT, 15)).setRegistryName("shop"),
                new StructureType(s -> true).setRegistryName("part")
            );
        }

        @SubscribeEvent
        public static void onConditionRegistry(final RegistryEvent.Register<ConditionType> event) {
            event.getRegistry().registerAll(
                    new ModCondition().setRegistryName(DungeonMod.ID, "mod"),
                    new PaletteCondition().setRegistryName(DungeonMod.ID, "palette"),
                    new FloorCondition().setRegistryName(DungeonMod.ID, "floor")
            );
        }

        @SubscribeEvent
        public static void onTileRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(
                    MetadataTile::new, MetadataBlock.METADATA_BLOCK)
                    .build(null)
                    .setRegistryName(DungeonMod.ID, "metadata")
            );
            event.getRegistry().register(TileEntityType.Builder.create(
                    ObeliskTile::new, ObeliskBlock.OBELISK)
                    .build(null)
                    .setRegistryName(DungeonMod.ID, "obelisk")
            );
        }

        private static final ArrayList<Block> BLOCKS = new ArrayList<>();

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

            Type.values()
                    .map(Type::block)
                    .map(o -> (Block) o)
                    .forEach(BLOCKS::add);

            BLOCKS.addAll(Arrays.asList(
                    new BreakableBlock(Blocks.STONE).setRegistryName(DungeonMod.ID, "porous_stone"),
                    new BreakableBlock(Blocks.GRAVEL).setRegistryName(DungeonMod.ID, "gravelous_gravel"),
                    new BreakableBlock(Blocks.OAK_PLANKS).setRegistryName(DungeonMod.ID, "morsh_wood"),

                    new MetadataBlock().setRegistryName(DungeonMod.ID, "metadata_block"),

                    new ObeliskBlock().setRegistryName(DungeonMod.ID, "obelisk"),

                    new RedstoneReceiverBlock().setRegistryName(DungeonMod.ID, "redstone_receiver"),
                    new RedstoneSenderBlock().setRegistryName(DungeonMod.ID, "redstone_sender")

                    //new LogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)),
                    //new Block(Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)),
                    //new LeavesBlock(Block.Properties.create(Material.WOOD, MaterialColor.FOLIAGE).hardnessAndResistance(0.2F).sound(SoundType.PLANT).tickRandomly())
            ));

            BLOCKS.forEach(event.getRegistry()::register);

        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            Item.Properties properties = new Item.Properties().group(DungeonMod.GROUP);
            BLOCKS.stream()
                    .filter(b -> b.getRegistryName() != null)
                    .map(b -> new BlockItem(b, properties).setRegistryName(b.getRegistryName()))
                    .forEach(event.getRegistry()::register);

            event.getRegistry().registerAll(
                    new GrenadeSmoke().setRegistryName(DungeonMod.ID, "smoke_grenade"),
                    new GrenadeFrost().setRegistryName(DungeonMod.ID, "frost_grenade"),
                    new GrenadeGravity().setRegistryName(DungeonMod.ID, "gravity_grenade"),

                    new ScrollItem().setRegistryName(DungeonMod.ID, "scroll")
            );
        }

        @SubscribeEvent
        public static void onSpellRegistry(final RegistryEvent.Register<Spell> event) {
            event.getRegistry().registerAll(
                new ShockwaveSpell().setRegistryName(DungeonMod.ID, "shockwave"),
                new BlackholeSpell().setRegistryName(DungeonMod.ID, "blackhole")
            );
        }

        @SubscribeEvent
        public static void onEntityTypeRegistry(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(
                    EntityType.Builder.<GrenadeEntity>create(GrenadeEntity::new, EntityClassification.MISC).size(0.25F, 0.25F)
                            .build("grenade").setRegistryName(DungeonMod.ID, "grenade")
            );
        }

    }

}
