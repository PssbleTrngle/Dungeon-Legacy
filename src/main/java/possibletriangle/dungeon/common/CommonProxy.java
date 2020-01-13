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
import net.minecraftforge.registries.RegistryBuilder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.Type;
import possibletriangle.dungeon.common.world.DungeonWorldType;
import possibletriangle.dungeon.common.world.room.Room;
import possibletriangle.dungeon.common.world.room.HallwayMaze;
import possibletriangle.dungeon.common.world.room.RoomStructure;
import possibletriangle.dungeon.common.world.wall.Wall;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Arrays;
import java.util.function.Supplier;

public class CommonProxy {

    public void init() {

        new DungeonWorldType();

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onNewRegistry(final RegistryEvent.NewRegistry event) {
            new RegistryBuilder<Room>()
                    .setName(new ResourceLocation(DungeonMod.MODID, "room"))
                    .setType(Room.class)
                    .create();
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
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.INFESTED_STONE, 1F)
                                            .add(() -> Blocks.COBBLESTONE, 0.5F),
                                    Type.FLOOR, Type.PATH)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.STONE_BRICKS, 1F)
                                            .add(() -> Blocks.MOSSY_STONE_BRICKS, 0.1F)
                                            .add(() -> Blocks.CRACKED_STONE_BRICKS, 0.1F),
                                    Type.WALL)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.DIAMOND_BLOCK, 1.5F)
                                            .add(() -> Blocks.GOLD_BLOCK, 1.5F)
                                            .add(() -> Blocks.EMERALD_BLOCK, 1F)
                                            .add(() -> Blocks.LAPIS_BLOCK, 0.1F),
                                    Type.GEM)
                            .put(() -> Blocks.CHISELED_STONE_BRICKS, Type.RUNE)
                            .put(() -> Blocks.GLOWSTONE, Type.LAMP)
                            .put(() -> Blocks.STONE_SLAB, Type.SLAB)
                            .put(() -> Blocks.STONE_STAIRS, Type.STAIRS)
                            .put(() -> Blocks.OAK_LOG, Type.PILLAR, Type.LOG)
                            .put(() -> Blocks.MOSSY_STONE_BRICK_SLAB, Type.SLAB_WALL)
                            .put(() -> Blocks.MOSSY_STONE_BRICK_STAIRS, Type.STAIRS_WALL)
                            .put(() -> Blocks.GRASS_BLOCK, Type.GRASS)
                            .put(() -> Blocks.OAK_PLANKS, Type.PLANKS)
                            .put(() -> Blocks.GLASS, Type.GLASS)
                            .put(new RandomCollection<>(() -> Blocks.SAND, () -> Blocks.GRAVEL), Type.FALLING)
                            .put(() -> Blocks.OAK_LEAVES, Type.LEAVES)
                            .put(() -> Blocks.CHEST, Type.CHEST)
                            .put(() -> Blocks.LEVER, Type.LEVER)
                            .put(new RandomCollection<>(() -> Blocks.DANDELION, () -> Blocks.POPPY, () -> Blocks.BLUE_ORCHID, () -> Blocks.AZURE_BLUET))
                            .put(() -> Blocks.STONE_BUTTON, Type.BUTTON)
                            .put(() -> Blocks.POLISHED_ANDESITE, Type.SEAL),

                    /* ------------------   NETHER  ------------------ */
                    new Palette(1F, () -> Biomes.NETHER).setRegistryName(DungeonMod.MODID, "nether")
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.NETHERRACK, 1F)
                                            .add(() -> Blocks.SOUL_SAND, 0.1F),
                                    Type.FLOOR)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.SOUL_SAND, 1F)
                                            .add(() -> Blocks.GRAVEL, 2F),
                                    Type.PATH)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.NETHER_BRICKS, 1F)
                                            .add(() -> Blocks.RED_NETHER_BRICKS, 0.2F),
                                    Type.WALL, Type.PILLAR)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.GOLD_BLOCK, 2F)
                                            .add(() -> Blocks.QUARTZ_BLOCK, 1F)
                                            .add(() -> Blocks.OBSIDIAN, 1F),
                                    Type.GEM)
                            .put(() -> Blocks.MAGMA_BLOCK, Type.RUNE)
                            .put(() -> Blocks.GLOWSTONE, Type.LAMP)
                            .put(() -> Blocks.NETHER_BRICK_SLAB, Type.SLAB, Type.SLAB_WALL)
                            .put(() -> Blocks.NETHER_BRICK_STAIRS, Type.STAIRS, Type.STAIRS_WALL),

                    /* ------------------   NATURE  ------------------ */
                    new Palette(1F, () -> Biomes.FOREST).setRegistryName(DungeonMod.MODID, "nature")
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.INFESTED_STONE, 1F)
                                            .add(() -> Blocks.MOSSY_COBBLESTONE, 0.5F),
                                    Type.FLOOR)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.DIRT, 1F)
                                            .add(() -> Blocks.COARSE_DIRT, 1F)
                                            .add(() -> Blocks.GRASS_PATH, 1F),
                                    Type.PATH)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.STONE_BRICKS, 1F)
                                            .add(() -> Blocks.MOSSY_STONE_BRICKS, 1F)
                                            .add(() -> Blocks.CRACKED_STONE_BRICKS, 0.6F),
                                    Type.WALL)
                            .put(() -> Blocks.SPRUCE_LOG, Type.PILLAR, Type.LOG)
                            .put(new RandomCollection<Supplier<Block>>()
                                            .add(() -> Blocks.GOLD_BLOCK, 1F)
                                            .add(() -> Blocks.EMERALD_BLOCK, 2F),
                                    Type.GEM)
                            .put(() -> Blocks.CHISELED_STONE_BRICKS, Type.RUNE)
                            .put(() -> Blocks.GLOWSTONE, Type.LAMP)
                            .put(() -> Blocks.STONE_SLAB, Type.SLAB)
                            .put(() -> Blocks.STONE_STAIRS, Type.STAIRS)
                            .put(() -> Blocks.STONE_BRICK_SLAB, Type.SLAB_WALL)
                            .put(() -> Blocks.STONE_BRICK_STAIRS, Type.STAIRS_WALL)
            );
        }

        @SubscribeEvent
        public static void onRoomsRegistry(final RegistryEvent.Register<Room> event) {

            Arrays.stream(new String[]{"lava_pillars", "forest", "garden", "well", "stairs", "hole", "hole_taller", "atrium"})
                    .map(name -> new ResourceLocation(DungeonMod.MODID, "room/" + name))
                    .map(r -> new RoomStructure(Room.Type.ROOM, r))
                    .forEach(event.getRegistry()::register);

            Arrays.stream(new String[]{"corridor_1", "star", "bridge"})
                    .map(name -> new ResourceLocation(DungeonMod.MODID, "hallway/" + name))
                    .map(r -> new RoomStructure(Room.Type.HALLWAY, r))
                    .forEach(event.getRegistry()::register);

            event.getRegistry().registerAll(
                    new HallwayMaze().setRegistryName("maze")
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
                    .map(b -> new BlockItem(b, properties).setRegistryName(b.getRegistryName()))
                    .forEach(event.getRegistry()::register);
        }

    }

}
