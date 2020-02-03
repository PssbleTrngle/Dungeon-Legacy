package possibletriangle.dungeon.common.content;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
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
import possibletriangle.dungeon.common.world.room.StateProvider;
import possibletriangle.dungeon.common.world.room.Structures;
import possibletriangle.dungeon.common.world.structure.StructureLoader;
import possibletriangle.dungeon.helper.BlockCollection;
import possibletriangle.dungeon.helper.Variant;
import possibletriangle.dungeon.helper.Fallback;
import possibletriangle.dungeon.common.world.room.StructureType;
import sun.management.counter.Variability;

import java.util.*;
import java.util.stream.Stream;

public class Palettes {

    public static BlockCollection withGroth(IntegerProperty property, Block... plants) {
        BlockCollection c = new BlockCollection();
        Stream<BlockState> defaults = Arrays.stream(plants).map(Block::getDefaultState);
        if(defaults.allMatch(state -> state.has(property))) {
            property.getAllowedValues().stream().map(age ->
                new Variant(defaults.map(s -> s.with(property, age)).toArray(BlockState[]::new))
            ).forEach(s -> c.add(s, 1));
        }
        return c;
    }

    /*
    public static BlockState[] withGroth(Block plant) {
        BlockState d = plant.getDefaultState();

        Optional<IntegerProperty> age = d.getProperties()
                .stream()
                .filter(p -> p instanceof IntegerProperty)
                .filter(p -> p.getName().equals("age"))
                .findFirst()
                .map(p -> (IntegerProperty) p);

        return age.map(property -> {
            BlockCollection c = new BlockCollection();
            return property.getAllowedValues()
                    .stream()
                    .map(v -> d.with(property, v))
                    .toArray(BlockState[]::new);
        }).orElse(new BlockState[]{ d });
    }
    */

    public static void register(RegistryEvent.Register<Palette> event) {

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
                    .put(new Variant(Blocks.OAK_STAIRS, Blocks.SPRUCE_STAIRS, Blocks.DARK_OAK_STAIRS)).forTypes(Type.STAIRS_WOOD)
                    .put(new Variant(Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.DARK_OAK_SLAB)).forTypes(Type.SLAB_WOOD)
                    .put(new Variant(Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.DARK_OAK_LOG)).forTypes(Type.LOG)
                    .put(new Variant(Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES)).forTypes(Type.LEAVES)
                    .put(withGroth(CropsBlock.AGE, Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS, Blocks.BEETROOTS)).forTypes(Type.CROP)
                    .put(Blocks.FARMLAND).forTypes(Type.FARMLAND)
                    .put(Blocks.DIRT).forTypes(Type.DIRT)
                    .put(Blocks.STONE_PRESSURE_PLATE).forTypes(Type.PRESSURE_PLATE)
                    .put(Blocks.LAVA).forTypes(Type.FLUID_UNSAFE)
                    .put(Blocks.STONE_BRICK_WALL).forTypes(Type.FENCE)
                    .put(Blocks.LEVER).forTypes(Type.LEVER)
                    .put(Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.AZURE_BLUET).forTypes(Type.PLANT)
                    .put(Blocks.STONE_BUTTON).forTypes(Type.BUTTON)
                    .put(new Variant(Blocks.MELON, Blocks.PUMPKIN)).forTypes(Type.FRUIT)
                    .put(Blocks.POLISHED_ANDESITE).forTypes(Type.SEAL)
                    .put(Blocks.BOOKSHELF).forTypes(Type.BOOKSHELF)
                    .put(new Variant(Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_DARK_OAK_LOG)).forTypes(Type.STRIPPED_LOG),

            /* ------------------   NATURE  ------------------ */
            new Palette(1F, () -> Biomes.FOREST).setRegistryName(DungeonMod.MODID, "nature")
                    .put(new Variant(Blocks.DARK_OAK_LOG, Blocks.SPRUCE_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG)).forTypes(Type.PILLAR)
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

            /* ------------------  MUSHROOM  ------------------ */
            new Palette(0.3F, () -> Biomes.MUSHROOM_FIELDS).setRegistryName(DungeonMod.MODID, "mushroom")
                    .put(Blocks.MYCELIUM).forTypes(Type.GRASS)
                    .put(new Variant(Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK)).forTypes(Type.LEAVES)
                    .put(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM).forTypes(Type.PLANT)
                    .put(Blocks.MUSHROOM_STEM).forTypes(Type.LOG),

            /* ------------------  QUARTZ  ------------------ */
            new Palette(0F, () -> Biomes.PLAINS).setRegistryName(DungeonMod.MODID, "quartz")
                    .put(Blocks.QUARTZ_BLOCK).forTypes(Type.WALL, Type.FLOOR)
                    .put(Blocks.CHISELED_QUARTZ_BLOCK).forTypes(Type.RUNE, Type.PATH)
                    .put(Blocks.QUARTZ_SLAB).forTypes(Type.SLAB, Type.SLAB_WALL)
                    .put(Blocks.QUARTZ_STAIRS).forTypes(Type.STAIRS, Type.STAIRS_WALL)
                    .put(Blocks.GOLD_BLOCK, Blocks.DIAMOND_BLOCK).forTypes(Type.GEM)
                    .put(Blocks.WHITE_CONCRETE_POWDER).forTypes(Type.FALLING)
                    .put(Blocks.GLOWSTONE).forTypes(Type.LAMP)
                    .put(Blocks.QUARTZ_PILLAR).forTypes(Type.PILLAR),

            /* ------------------  SAND  ------------------ */
            new Palette(0F, () -> Biomes.DESERT).setRegistryName(DungeonMod.MODID, "sand")
                    .put(Blocks.SANDSTONE).forTypes(Type.WALL, Type.FLOOR, Type.PILLAR)
                    .put(Blocks.CHISELED_SANDSTONE).forTypes(Type.RUNE)
                    .put(Blocks.SAND).forTypes(Type.PATH, Type.FALLING, Type.FARMLAND)
                    .put(Blocks.DIRT).forTypes(Type.DIRT, Type.GRASS)
                    .put(Blocks.SANDSTONE_SLAB).forTypes(Type.SLAB, Type.SLAB_WALL)
                    .put(Blocks.SANDSTONE_STAIRS).forTypes(Type.STAIRS, Type.STAIRS_WALL)
                    .put(new BlockCollection()
                            .add(Blocks.DEAD_BUSH, 1F)
                            .add(Blocks.CACTUS, 0.2F))
                        .forTypes(Type.PLANT, Type.CROP)
                    .put(Blocks.MELON).forTypes(Type.FRUIT)
                    .put(Blocks.GOLD_BLOCK, Blocks.LAPIS_BLOCK, Blocks.COAL_BLOCK).forTypes(Type.GEM),

            /* ------------------   NETHER  ------------------ */
            new Palette(0F, () -> Biomes.NETHER).setRegistryName(DungeonMod.MODID, "nether")
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
                    .put(Blocks.NETHER_WART_BLOCK).forTypes(Type.FRUIT)
                    .put(Blocks.GLOWSTONE).forTypes(Type.LAMP)
                    .put(Blocks.RED_STAINED_GLASS).forTypes(Type.GLASS)
                    .put(Blocks.NETHER_BRICK_SLAB).forTypes(Type.SLAB, Type.SLAB_WALL)
                    .put(Blocks.NETHER_BRICK_STAIRS).forTypes(Type.STAIRS, Type.STAIRS_WALL)
                    .put(withGroth(NetherWartBlock.AGE, Blocks.NETHER_WART)).forTypes(Type.CROP)
                    .put(Blocks.PUMPKIN).forTypes(Type.FRUIT)
                    .put(Blocks.RED_CONCRETE_POWDER).forTypes(Type.FALLING)
                    .put(Blocks.RED_MUSHROOM_BLOCK).forTypes(Type.LEAVES)
                    .put(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM).forTypes(Type.PLANT)
                    .put(Blocks.MUSHROOM_STEM).forTypes(Type.LOG),

            /* ------------------  PRISMARINE  ------------------ */
            new Palette(0F, () -> Biomes.OCEAN).setRegistryName(DungeonMod.MODID, "prismarine")
                    .put(Blocks.PRISMARINE).forTypes(Type.FLOOR)
                    .put(Blocks.DARK_PRISMARINE).forTypes(Type.PATH, Type.PILLAR)
                    .put(Blocks.SEA_LANTERN).forTypes(Type.LAMP)
                    .put(Blocks.PRISMARINE_BRICKS).forTypes(Type.WALL)
                    .put(Blocks.GOLD_BLOCK, Blocks.LAPIS_BLOCK).forTypes(Type.GEM)
                    .put(Blocks.PRISMARINE_STAIRS).forTypes(Type.STAIRS)
                    .put(Blocks.PRISMARINE_SLAB).forTypes(Type.SLAB)
                    .put(Blocks.PRISMARINE_BRICK_SLAB).forTypes(Type.SLAB_WALL)
                    .put(Blocks.PRISMARINE_BRICK_STAIRS).forTypes(Type.STAIRS_WALL)
                    .put(Blocks.SAND).forTypes(Type.FARMLAND, Type.DIRT, Type.GRASS)
                    .put(Blocks.SAND).forTypes(Type.FALLING)
                    .put(Blocks.WET_SPONGE).forTypes(Type.FRUIT)
                    .put(Blocks.SUGAR_CANE).forTypes(Type.CROP)
                    .put(new Variant(Blocks.BRAIN_CORAL, Blocks.BUBBLE_CORAL, Blocks.FIRE_CORAL, Blocks.TUBE_CORAL, Blocks.FIRE))
                        .forTypes(Type.PLANT, Type.CROP),

            /* ------------------  END  ------------------ */
            new Palette(0F, () -> Biomes.THE_END).setRegistryName(DungeonMod.MODID, "end")
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
                    .put(Blocks.PURPLE_STAINED_GLASS).forTypes(Type.GLASS)
        );

    }

}