package possibletriangle.dungeon.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.RegistryBuilder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.BreakableBlock;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.Type;
import possibletriangle.dungeon.common.world.DungeonWorldType;
import possibletriangle.dungeon.common.world.room.HallwayMaze;
import possibletriangle.dungeon.common.world.room.Structures;
import possibletriangle.dungeon.common.world.structure.StructureLoader;
import possibletriangle.dungeon.helper.BlockCollection;
import possibletriangle.dungeon.helper.Variant;
import possibletriangle.dungeon.helper.Fallback;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class CommonProxy {

    public void init(final FMLCommonSetupEvent event) {

        new DungeonWorldType();

    }

    public void reloadRooms(IReloadableResourceManager manager) {

        Structures.clear();

        Arrays.stream(StructureType.values())
                .map(StructureLoader::new)
                .forEach(manager::addReloadListener);

        Structures.register(new HallwayMaze(), StructureTypes.HALLWAY);

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {
            new RegistryBuilder<Palette>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "palette"))
                    .setType(Palette.class)
                    .create();
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {
            new RegistryBuilder<StructureType>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "structure_type"))
                    .setType(StructureType.class)
                    .create();
        }

        @SubscribeEvent
        public static void onStructureTypeRegistry(final RegistryEvent.Register<StructureType> event) {
            event.getRegistry().registerAll(
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "room"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "hallway"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "boss"),
                new StructureType(StructureType::validRoom).setRegistryName(DungeonMod.MODID, "base"),
                new StructureType(s -> true).setRegistryName("door/small"),
                new StructureType(s -> true).setRegistryName("door/big")
            );
        }

        @SubscribeEvent
        public static void onPalettesRegistry(final RegistryEvent.Register<Palette> event) {
            event.getRegistry().registerAll(

                    /* ------------------   STONE  ------------------ */
                    new Palette(1F, () -> Biomes.PLAINS, () -> null).setRegistryName(DungeonMod.MODID, "stone")
                            .put(new BlockCollection()
                                        .add(Blocks.STONE, 1F)
                                        .add(Blocks.ANDESITE, 0.7F))
                                .forTypes(Type.FLOOR, Type.PILLAR)
                            .put(Blocks.COBBLESTONE).forTypes(Type.PATH)
                            .put(new BlockCollection()
                                        .add(Blocks.STONE_BRICKS, 1F)
                                        .add(Blocks.MOSSY_STONE_BRICKS, 0.1F)
                                        .add(Blocks.CRACKED_STONE_BRICKS, 0.1F))
                                .forTypes(Type.WALL)
                            .put(new BlockCollection()
                                        .add(Blocks.DIAMOND_BLOCK, 1.5F)
                                        .add(Blocks.GOLD_BLOCK, 1.5F)
                                        .add(Blocks.EMERALD_BLOCK, 1F)
                                        .add(Blocks.LAPIS_BLOCK, 0.1F))
                                .forTypes(Type.GEM)
                            .put(Blocks.CHISELED_STONE_BRICKS).forTypes(Type.RUNE)
                            .put(Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true)).forTypes(Type.LAMP)
                            .put(Blocks.STONE_SLAB).forTypes(Type.SLAB)
                            .put(Blocks.STONE_STAIRS).forTypes(Type.STAIRS)
                            .put(Blocks.MOSSY_STONE_BRICK_SLAB).forTypes(Type.SLAB_WALL)
                            .put(Blocks.MOSSY_STONE_BRICK_STAIRS).forTypes(Type.STAIRS_WALL)
                            .put(Blocks.GRASS_BLOCK).forTypes(Type.GRASS)
                            .put(Blocks.GLASS).forTypes(Type.GLASS)
                            .put(Blocks.GRAVEL).forTypes(Type.FALLING)
                            .put(new Variant(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.DARK_OAK_PLANKS)).forTypes(Type.PLANKS)
                            .put(new Variant(Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.DARK_OAK_LOG)).forTypes(Type.LOG)
                            .put(new Variant(Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES)).forTypes(Type.LEAVES)
                            .put(new Variant(Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS, Blocks.BEETROOTS)).forTypes(Type.CROP)
                            .put(Blocks.FARMLAND).forTypes(Type.FARMLAND)
                            .put(Blocks.DIRT).forTypes(Type.DIRT)
                            .put(Blocks.STONE_PRESSURE_PLATE).forTypes(Type.PRESSURE_PLATE)
                            .put(Blocks.LAVA).forTypes(Type.FLUID_UNSAFE)
                            .put(Blocks.STONE_BRICK_WALL).forTypes(Type.FENCE)
                            .put(Blocks.LEVER).forTypes(Type.LEVER)
                            .put(Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.AZURE_BLUET).forTypes(Type.PLANT)
                            .put(Blocks.STONE_BUTTON).forTypes(Type.BUTTON)
                            .put(Blocks.POLISHED_ANDESITE).forTypes(Type.SEAL),

                    /* ------------------   NATURE  ------------------ */
                    new Palette(1F, () -> Biomes.FOREST).setRegistryName(DungeonMod.MODID, "nature")
                            .put(Blocks.DARK_OAK_LOG).forTypes(Type.PILLAR)
                            .put(new BlockCollection()
                                        .add(Blocks.STONE, 1F)
                                        .add(Blocks.MOSSY_COBBLESTONE, 0.5F))
                                .forTypes(Type.FLOOR)
                            .put(new BlockCollection()
                                        .add(Blocks.DIRT, 1F)
                                        .add(Blocks.COARSE_DIRT, 1F)
                                        .add(Blocks.GRASS_PATH, 1F))
                                .forTypes(Type.PATH)
                            .put(new BlockCollection()
                                        .add(Blocks.STONE_BRICKS, 1F)
                                        .add(Blocks.MOSSY_STONE_BRICKS, 1F)
                                        .add(Blocks.CRACKED_STONE_BRICKS, 0.6F))
                                .forTypes(Type.WALL)
                            .put(new BlockCollection()
                                        .add(Blocks.GOLD_BLOCK, 1F)
                                        .add(Blocks.EMERALD_BLOCK, 2F))
                                .forTypes(Type.GEM),

                    /* ------------------   NETHER  ------------------ */
                    new Palette(0.2F, () -> Biomes.NETHER).setRegistryName(DungeonMod.MODID, "nether")
                            .put(new BlockCollection()
                                        .add(Blocks.NETHERRACK, 1F)
                                        .add(Blocks.SOUL_SAND, 0.1F))
                                .forTypes(Type.FLOOR)
                            .put(new BlockCollection()
                                        .add(Blocks.SOUL_SAND, 1F)
                                        .add(Blocks.GRAVEL, 2F))
                                .forTypes(Type.PATH)
                            .put(new BlockCollection()
                                        .add(Blocks.NETHER_BRICKS, 1F)
                                        .add(Blocks.RED_NETHER_BRICKS, 0.2F))
                                .forTypes(Type.WALL, Type.PILLAR)
                            .put(new BlockCollection()
                                        .add(Blocks.GOLD_BLOCK, 2F)
                                        .add(Blocks.QUARTZ_BLOCK, 1F)
                                        .add(Blocks.OBSIDIAN, 1F))
                                .forTypes(Type.GEM)
                            .put(Blocks.MAGMA_BLOCK).forTypes(Type.RUNE)
                            .put(Blocks.GLOWSTONE).forTypes(Type.LAMP)
                            .put(Blocks.RED_STAINED_GLASS).forTypes(Type.GLASS)
                            .put(Blocks.NETHER_BRICK_SLAB).forTypes(Type.SLAB, Type.SLAB_WALL)
                            .put(Blocks.NETHER_BRICK_STAIRS).forTypes(Type.STAIRS, Type.STAIRS_WALL)
                            .put(Blocks.NETHER_WART).forTypes(Type.CROP)
                            .put(Blocks.RED_CONCRETE_POWDER).forTypes(Type.FALLING)
                            .put(Blocks.RED_MUSHROOM_BLOCK).forTypes(Type.LEAVES)
                            .put(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM).forTypes(Type.PLANT)
                            .put(Blocks.MUSHROOM_STEM).forTypes(Type.LOG),

                    /* ------------------  PRISMARINE  ------------------ */
                    new Palette(0.2F, () -> Biomes.OCEAN).setRegistryName(DungeonMod.MODID, "prismarine")
                            .put(Blocks.PRISMARINE).forTypes(Type.FLOOR)
                            .put(Blocks.DARK_PRISMARINE).forTypes(Type.PATH)
                            .put(Blocks.PRISMARINE_BRICKS).forTypes(Type.WALL)
                            .put(Blocks.GOLD_BLOCK, Blocks.LAPIS_BLOCK).forTypes(Type.GEM)
                            .put(Blocks.PRISMARINE_STAIRS).forTypes(Type.STAIRS)
                            .put(Blocks.PRISMARINE_SLAB).forTypes(Type.SLAB)
                            .put(Blocks.PRISMARINE_BRICK_SLAB).forTypes(Type.SLAB_WALL)
                            .put(Blocks.PRISMARINE_BRICK_STAIRS).forTypes(Type.STAIRS_WALL)
                            .put(Blocks.SAND).forTypes(Type.FALLING)
                            .put(new Variant(Blocks.BRAIN_CORAL, Blocks.BUBBLE_CORAL, Blocks.FIRE_CORAL, Blocks.TUBE_CORAL, Blocks.FIRE))
                                .forTypes(Type.PLANT, Type.CROP),

                    /* ------------------  END  ------------------ */
                    new Palette(0.2F, () -> Biomes.THE_END).setRegistryName(DungeonMod.MODID, "end")
                            .put(Blocks.END_STONE).forTypes(Type.FLOOR)
                            .put(Blocks.END_STONE_BRICKS).forTypes(Type.PATH)
                            .put(Blocks.PURPUR_BLOCK).forTypes(Type.WALL)
                            .put(Blocks.PURPUR_PILLAR).forTypes(Type.PILLAR)
                            .put(Blocks.OBSIDIAN).forTypes(Type.GEM)
                            .put(Blocks.END_STONE_BRICK_STAIRS).forTypes(Type.STAIRS)
                            .put(Blocks.END_STONE_BRICK_SLAB).forTypes(Type.SLAB)
                            .put(Blocks.PURPUR_STAIRS).forTypes(Type.SLAB_WALL)
                            .put(Blocks.PURPUR_SLAB).forTypes(Type.STAIRS_WALL)
                            .put(Blocks.BLACK_CONCRETE_POWDER).forTypes(Type.FALLING)
                            .put(Blocks.AIR).forTypes(Type.PLANT, Type.CROP)
                            .put(Blocks.PURPLE_STAINED_GLASS).forTypes(Type.GLASS),

                    /* ------------------  MUSHROOM  ------------------ */
                    new Palette(0.2F, () -> Biomes.MUSHROOM_FIELDS).setRegistryName(DungeonMod.MODID, "mushroom")
                            .put(Blocks.MYCELIUM).forTypes(Type.GRASS)
                            .put(new Variant(Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK)).forTypes(Type.LEAVES)
                            .put(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM).forTypes(Type.PLANT)
                            .put(Blocks.MUSHROOM_STEM).forTypes(Type.LOG)
            );
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

            Arrays.stream(Type.values())
                    .map(Type::block)
                    .forEach(event.getRegistry()::register);

            event.getRegistry().registerAll(
                new BreakableBlock(Blocks.STONE).setRegistryName(DungeonMod.MODID, "porous_stone"),
                new BreakableBlock(Blocks.GRAVEL).setRegistryName(DungeonMod.MODID, "gravelous_gravel"),
                new BreakableBlock(Blocks.OAK_PLANKS).setRegistryName(DungeonMod.MODID, "morsh_wood")
            );

        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {

            registerBlockItems(event,
                Arrays.stream(Type.values())
                    .map(type -> type.name().toLowerCase())
                    .map(name -> new ResourceLocation(DungeonMod.MODID, "placeholder_" + name))
            );

            registerBlockItems(event,
                Arrays.stream(new String[]{ "porous_stone", "gravelous_gravel", "morsh_wood" })
                    .map(name -> new ResourceLocation(DungeonMod.MODID, name))
            );

        }

        private static void registerBlockItems(RegistryEvent.Register<Item> event, Stream<ResourceLocation> blocks) {
            Item.Properties properties = new Item.Properties().group(DungeonMod.GROUP);

            blocks
                .map(GameRegistry.findRegistry(Block.class)::getValue)
                .filter(Objects::nonNull)
                .filter(b -> b.getRegistryName() != null)
                .map(b -> new BlockItem(b, properties).setRegistryName(b.getRegistryName()))
                .forEach(event.getRegistry()::register);
        }

    }

}
