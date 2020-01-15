package possibletriangle.dungeon.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryBuilder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.Type;
import possibletriangle.dungeon.common.world.DungeonWorldType;
import possibletriangle.dungeon.helper.BlockCollection;
import possibletriangle.dungeon.helper.Variant;
import possibletriangle.dungeon.helper.Fallback;

import java.util.Arrays;
import java.util.Objects;

public class CommonProxy {

    public void init(final FMLCommonSetupEvent event) {

        new DungeonWorldType();

    }

    reloadRooms() {

        Room.clear();

        IResourceManager manager = event.getServer().getResourceManager();

        StructureLoader.reload(manager);
        Room.register(new HallwayMaze(), Structures.Type.HALLWAY);

        DungeonMod.LOGGER.info("Loaded {} structures", Room.count());

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {
            new RegistryBuilder<Palette>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "palette"))
                    .setType(Palette.class)
                    .create();
        }

        @SubscribeEvent
        public static void onPalettesRegistry(final RegistryEvent.Register<Palette> event) {
            event.getRegistry().registerAll(

                    /* ------------------   STONE  ------------------ */
                    new Palette(1F, () -> Biomes.PLAINS, () -> null).setRegistryName(DungeonMod.MODID, "stone")
                            .put(new BlockCollection()
                                        .add(Blocks.STONE, 1F)
                                        .add(Blocks.ANDERSITE, 0.7F)
                                .for(Type.FLOOR, Type.PILLAR)
                            .put(Blocks.COBBLESTONE).for(Type.PAHT)
                            .put(new BlockCollection()
                                        .add(Blocks.STONE_BRICKS, 1F)
                                        .add(Blocks.MOSSY_STONE_BRICKS, 0.1F)
                                        .add(Blocks.CRACKED_STONE_BRICKS, 0.1F))
                                .for(Type.WALL)
                            .put(new BlockCollection()
                                        .add(Blocks.DIAMOND_BLOCK, 1.5F)
                                        .add(Blocks.GOLD_BLOCK, 1.5F)
                                        .add(Blocks.EMERALD_BLOCK, 1F)
                                        .add(Blocks.LAPIS_BLOCK, 0.1F))
                                .for(Type.GEM)
                            .put(Blocks.CHISELED_STONE_BRICKS).for(Type.RUNE)
                            .put(Blocks.REDSTONE_LAMP).for(Type.LAMP)
                            .put(new Fallback(() -> null, () -> Blocks.STONE_SLAB)).for(Type.SLAB) /* TODO remvove if working */
                            .put(Blocks.STONE_STAIRS).for(Type.STAIRS)
                            .put(Blocks.MOSSY_STONE_BRICK_SLAB).for(Type.SLAB_WALL)
                            .put(Blocks.MOSSY_STONE_BRICK_STAIRS).for(Type.STAIRS_WALL)
                            .put(Blocks.GRASS_BLOCK).for(Type.GRASS)
                            .put(Blocks.GLASS).for(Type.GLASS)
                            .put(Blocks.GRAVEL).for(Type.FALLING)
                            .put(new Variant(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.DARK_OAK_PLANKS)).for(Type.PLANKS)
                            .put(new Variant(Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.DARK_OAK_LOG)).for(Type.LOG)
                            .put(new Variant(Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES)).for(Type.LEAVES)
                            .put(new Variant(Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS, Blocks.BEETROOTS)).for(Type.CROP)
                            .put(Blocks.LEVER).for(Type.LEVER)
                            .put(Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.AZURE_BLUET).for(Type.PLANT)
                            .put(Blocks.STONE_BUTTON).for(Type.BUTTON)
                            .put(Blocks.POLISHED_ANDESITE).for(Type.SEAL),

                    /* ------------------   NATURE  ------------------ */
                    new Palette(1F, () -> Biomes.FOREST).setRegistryName(DungeonMod.MODID, "nature")
                            .put(Blocks.DARK_OAK_LOG).for(Type.PILLAR)
                            .put(new BlockCollection()
                                        .add(Blocks.STONE, 1F)
                                        .add(Blocks.MOSSY_COBBLESTONE, 0.5F))
                                .for(Type.FLOOR)
                            .put(new BlockCollection()
                                        .add(Blocks.DIRT, 1F)
                                        .add(Blocks.COARSE_DIRT, 1F)
                                        .add(Blocks.GRASS_PATH, 1F))
                                .for(Type.PATH)
                            .put(new BlockCollection()
                                        .add(Blocks.STONE_BRICKS, 1F)
                                        .add(Blocks.MOSSY_STONE_BRICKS, 1F)
                                        .add(Blocks.CRACKED_STONE_BRICKS, 0.6F))
                                .for(Type.WALL)
                            .put(new BlockCollection()
                                        .add(Blocks.GOLD_BLOCK, 1F)
                                        .add(Blocks.EMERALD_BLOCK, 2F))
                                .for(Type.GEM),

                    /* ------------------   NETHER  ------------------ */
                    new Palette(0.1F, () -> Biomes.NETHER).setRegistryName(DungeonMod.MODID, "nether")
                            .put(new BlockCollection()
                                        .add(Blocks.NETHERRACK, 1F)
                                        .add(Blocks.SOUL_SAND, 0.1F))
                                .for(Type.FLOOR)
                            .put(new BlockCollection()
                                        .add(Blocks.SOUL_SAND, 1F)
                                        .add(Blocks.GRAVEL, 2F))
                                .for(Type.PATH)
                            .put(new BlockCollection()
                                        .add(Blocks.NETHER_BRICKS, 1F)
                                        .add(Blocks.RED_NETHER_BRICKS, 0.2F))
                                .for(Type.WALL, Type.PILLAR)
                            .put(new BlockCollection()
                                        .add(Blocks.GOLD_BLOCK, 2F)
                                        .add(Blocks.QUARTZ_BLOCK, 1F)
                                        .add(Blocks.OBSIDIAN, 1F))
                                .for(Type.GEM)
                            .put(Blocks.MAGMA_BLOCK).for(Type.RUNE)
                            .put(Blocks.GLOWSTONE).for(Type.LAMP)
                            .put(Blocks.RED_STAINED_GLASS).for(Type.GLASS)
                            .put(Blocks.NETHER_BRICK_SLAB).for(Type.SLAB, Type.SLAB_WALL)
                            .put(Blocks.NETHER_BRICK_STAIRS).for(Type.STAIRS, Type.STAIRS_WALL)
                            .put(Blocks.NETHER_WART).for(Type.CROP)
                            .put(Blocks.RED_CONCRETE_POWDER).for(Type.FALLING)
                            .put(Blocks.RED_MUSHROOM_BLOCK).for(Type.LEAVES)
                            .put(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM).for(Type.PLANT)
                            .put(Blocks.MUSHROOM_STEM).for(Type.LOG),

                    /* ------------------  PRISMARINE  ------------------ */
                    new Palette(0.1F, () -> Biomes.OCEAN).setRegistryName(DungeonMod.MODID, "prismarine")
                            .put(Blocks.PRISMARINE).for(Type.FLOOR)
                            .put(Blocks.DARK_PRISMARINE).for(Type.PATH)
                            .put(Blocks.PRISMARINE_BRICKS).for(Type.WALL)
                            .put(Blocks.GOLD_BLOCK, Blocks.LAPIS_BLOCK).for(Type.GEM)
                            .put(Blocks.PRISMARINE_STAIRS).for(Type.STAIRS)
                            .put(Blocks.PRISMARINE_SLAB).for(Type.SLAB)
                            .put(Blocks.PRISMARINE_BRICK_SLAB).for(Type.SLAB_WALL)
                            .put(Blocks.PRISMARINE_BRICK_STAIRS).for(Type.STAIRS_WALL)
                            .put(Blocks.SAND).for(Type.FALLING)
                            .put(Blocks.CORAL).for(Type.PLANT, Type.CROP),

                    /* ------------------  END  ------------------ */
                    new Palette(0.1F, () -> Biomes.END).setRegistryName(DungeonMod.MODID, "end")
                            .put(Blocks.END_STONE).for(Type.FLOOR)
                            .put(Blocks.END_STONE_BRICKS).for(Type.PATH)
                            .put(Blocks.PURPUR_BLOCK).for(Type.WALL)
                            .put(Blocks.PURPUR_PILLAR).for(Type.PILLAR)
                            .put(Blocks.OBSIDIAN).for(Type.GEM)
                            .put(Blocks.END_STONE_BRICKS_STAIRS).for(Type.STAIRS)
                            .put(Blocks.END_STONE_BRICKS_SLAB).for(Type.SLAB)
                            .put(Blocks.PURPUR_STAIRS).for(Type.SLAB_WALL)
                            .put(Blocks.PURPUR_SLAB).for(Type.STAIRS_WALL)
                            .put(Blocks.BLACK_CONCRETE_POWDER).for(Type.FALLING)
                            .put(Blocks.AIR).for(Type.PLANT, Type.CROP)
                            .put(Blocks.PURPLE_STAINED_GLASS).for(Type.GLASS),

                    /* ------------------  END  ------------------ */
                    new Palette(0.1F, () -> Biomes.MUSHROOM_ISLAND).setRegistryName(DungeonMod.MODID, "mushroom")
                            .put(Blocks.MYCELIUM).for(Type.GRASS)
                            .put(new Variant(Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK)).for(Type.LEAVES)
                            .put(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM).for(Type.PLANT)
                            .put(Blocks.MUSHROOM_STEM).for(Type.LOG)
            );
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            Arrays.stream(Type.values())
                    .map(Type::block)
                    .forEach(event.getRegistry()::register);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            Item.Properties properties = new Item.Properties()
                    .group(DungeonMod.GROUP);

            Arrays.stream(Type.values())
                    .map(type -> new ResourceLocation(DungeonMod.MODID, "placeholder_" + type.name().toLowerCase()))
                    .map(GameRegistry.findRegistry(Block.class)::getValue)
                    .filter(Objects::nonNull)
                    .filter(b -> b.getRegistryName() != null)
                    .map(b -> new BlockItem(b, properties).setRegistryName(b.getRegistryName()))
                    .forEach(event.getRegistry()::register);
        }

    }

}
