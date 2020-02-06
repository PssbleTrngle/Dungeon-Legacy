package possibletriangle.dungeon.common;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryBuilder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.BreakableBlock;
import possibletriangle.dungeon.common.block.MetadataBlock;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.content.Palettes;
import possibletriangle.dungeon.common.block.Type;
import possibletriangle.dungeon.common.data.CanBreak;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.DungeonWorldType;
import possibletriangle.dungeon.common.world.room.HallwayMaze;
import possibletriangle.dungeon.common.world.room.Structures;
import possibletriangle.dungeon.common.world.structure.StructureLoader;
import possibletriangle.dungeon.common.world.room.StructureType;
import possibletriangle.dungeon.common.world.structure.metadata.condition.ConditionType;
import possibletriangle.dungeon.common.world.structure.metadata.condition.FloorCondition;
import possibletriangle.dungeon.common.world.structure.metadata.condition.ModCondition;
import possibletriangle.dungeon.common.world.structure.metadata.condition.PaletteCondition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class CommonProxy {

    public void init(final FMLCommonSetupEvent event) {

        new DungeonWorldType();

    }

    public void reloadRooms(IReloadableResourceManager manager) {

        Structures.clear();

        StructureType.values().stream()
                .map(StructureLoader::new)
                .forEach(manager::addReloadListener);

        Structures.register(new HallwayMaze(), StructureType.HALLWAY);

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {
            new RegistryBuilder<Palette>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "palette"))
                    .setType(Palette.class)
                    .create();

            new RegistryBuilder<StructureType>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "structure_type"))
                    .setType(StructureType.class)
                    .create();

            new RegistryBuilder<ConditionType>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "condition"))
                    .setType(ConditionType.class)
                    .create();
        }

        @SubscribeEvent
        public static void onStructureTypeRegistry(final RegistryEvent.Register<StructureType> event) {
            event.getRegistry().registerAll(
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "room"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "hallway"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "boss"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "base"),
                new StructureType(StructureType.hasSize(2, 5, 4)).setRegistryName("door/small"),
                new StructureType(StructureType.hasSize(2, 5, 5)).setRegistryName("door/big"),
                new StructureType(StructureType.hasSize(5, DungeonSettings.FLOOR_HEIGHT, 15)).setRegistryName("shop"),
                new StructureType(s -> true).setRegistryName("part")
            );
        }

        @SubscribeEvent
        public static void onPalettesRegistry(final RegistryEvent.Register<Palette> event) {
            Palettes.register(event);
        }

        @SubscribeEvent
        public static void onConditionRegistry(final RegistryEvent.Register<ConditionType> event) {
            event.getRegistry().registerAll(
                    new ModCondition().setRegistryName(DungeonMod.MODID, "mod"),
                    new PaletteCondition().setRegistryName(DungeonMod.MODID, "palette"),
                    new FloorCondition().setRegistryName(DungeonMod.MODID, "floor")
            );
        }

        @SubscribeEvent
        public static void onTileRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(
                    MetadataTile::new, MetadataBlock.METADATA_BLOCK)
                    .build(null)
                    .setRegistryName(DungeonMod.MODID, "metadata")
            );
            event.getRegistry().register(TileEntityType.Builder.create(
                    TotemTile::new, TotemBlock.TOTEM)
                    .build(null)
                    .setRegistryName(DungeonMod.MODID, "totem")
            );
        }

        private static final ArrayList<Block> BLOCKS = new ArrayList<>();

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

            Arrays.stream(Type.values())
                    .map(Type::block)
                    .forEach(BLOCKS::add);

            BLOCKS.addAll(Arrays.asList(
                    new BreakableBlock(Blocks.STONE).setRegistryName(DungeonMod.MODID, "porous_stone"),
                    new BreakableBlock(Blocks.GRAVEL).setRegistryName(DungeonMod.MODID, "gravelous_gravel"),
                    new BreakableBlock(Blocks.OAK_PLANKS).setRegistryName(DungeonMod.MODID, "morsh_wood"),

                    new MetadataBlock().setRegistryName(DungeonMod.MODID, "metadata_block"),

                    new TotemBlock().setRegistryName(DungeonMod.MODID, "totem")

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
        }

    }

}
