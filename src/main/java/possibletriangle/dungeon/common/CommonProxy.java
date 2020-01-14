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

import java.util.Arrays;
import java.util.Objects;

public class CommonProxy {

    public void init(final FMLCommonSetupEvent event) {

        new DungeonWorldType();

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {
            /*
            new RegistryBuilder<Room>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "room"))
                    .setType(Room.class)
                    .create();
             */
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
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.STONE, 1F)
                                            .add(Blocks.COBBLESTONE, 0.5F),
                                    Type.FLOOR, Type.PATH)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.STONE_BRICKS, 1F)
                                            .add(Blocks.MOSSY_STONE_BRICKS, 0.1F)
                                            .add(Blocks.CRACKED_STONE_BRICKS, 0.1F),
                                    Type.WALL)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.DIAMOND_BLOCK, 1.5F)
                                            .add(Blocks.GOLD_BLOCK, 1.5F)
                                            .add(Blocks.EMERALD_BLOCK, 1F)
                                            .add(Blocks.LAPIS_BLOCK, 0.1F),
                                    Type.GEM)
                            .put(Blocks.CHISELED_STONE_BRICKS, Type.RUNE)
                            .put(Blocks.REDSTONE_LAMP, Type.LAMP)
                            .put(Blocks.STONE_SLAB, Type.SLAB)
                            .put(Blocks.STONE_STAIRS, Type.STAIRS)
                            .put(Blocks.OAK_LOG, Type.PILLAR, Type.LOG)
                            .put(Blocks.MOSSY_STONE_BRICK_SLAB, Type.SLAB_WALL)
                            .put(Blocks.MOSSY_STONE_BRICK_STAIRS, Type.STAIRS_WALL)
                            .put(Blocks.GRASS_BLOCK, Type.GRASS)
                            .put(Blocks.OAK_PLANKS, Type.PLANKS)
                            .put(Blocks.GLASS, Type.GLASS)
                            .put(Blocks.GRAVEL, Type.FALLING)
                            .put(Blocks.OAK_LEAVES, Type.LEAVES)
                            .put(new Variant(Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS, Blocks.BEETROOTS), Type.CROP)
                            .put(Blocks.LEVER, Type.LEVER)
                            .put(new BlockCollection(Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.AZURE_BLUET))
                            .put(Blocks.STONE_BUTTON, Type.BUTTON)
                            .put(Blocks.POLISHED_ANDESITE, Type.SEAL),

                    /* ------------------   NETHER  ------------------ */
                    new Palette(1F, () -> Biomes.NETHER).setRegistryName(DungeonMod.MODID, "nether")
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.NETHERRACK, 1F)
                                            .add(Blocks.SOUL_SAND, 0.1F),
                                    Type.FLOOR)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.SOUL_SAND, 1F)
                                            .add(Blocks.GRAVEL, 2F),
                                    Type.PATH)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.NETHER_BRICKS, 1F)
                                            .add(Blocks.RED_NETHER_BRICKS, 0.2F),
                                    Type.WALL, Type.PILLAR)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.GOLD_BLOCK, 2F)
                                            .add(Blocks.QUARTZ_BLOCK, 1F)
                                            .add(Blocks.OBSIDIAN, 1F),
                                    Type.GEM)
                            .put(Blocks.MAGMA_BLOCK, Type.RUNE)
                            .put(Blocks.GLOWSTONE, Type.LAMP)
                            .put(Blocks.NETHER_BRICK_SLAB, Type.SLAB, Type.SLAB_WALL)
                            .put(Blocks.NETHER_BRICK_STAIRS, Type.STAIRS, Type.STAIRS_WALL)
                            .put(Blocks.NETHER_WART, Type.CROP)
                            .put(Blocks.NETHER_WART_BLOCK, Type.LEAVES)
                            .put(Blocks.RED_CONCRETE_POWDER, Type.FALLING)
                            .put(new BlockCollection(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM), Type.PLANT)
                            .put(Blocks.BONE_BLOCK, Type.LOG),

                    /* ------------------   NATURE  ------------------ */
                    new Palette(1F, () -> Biomes.FOREST).setRegistryName(DungeonMod.MODID, "nature")
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.STONE, 1F)
                                            .add(Blocks.MOSSY_COBBLESTONE, 0.5F),
                                    Type.FLOOR)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.DIRT, 1F)
                                            .add(Blocks.COARSE_DIRT, 1F)
                                            .add(Blocks.GRASS_PATH, 1F),
                                    Type.PATH)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.STONE_BRICKS, 1F)
                                            .add(Blocks.MOSSY_STONE_BRICKS, 1F)
                                            .add(Blocks.CRACKED_STONE_BRICKS, 0.6F),
                                    Type.WALL)
                            .put(new BlockCollection(new Block[0])
                                            .add(Blocks.GOLD_BLOCK, 1F)
                                            .add(Blocks.EMERALD_BLOCK, 2F),
                                    Type.GEM)
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
