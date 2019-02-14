package possibletriangle.dungeon.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.placeholder.*;
import possibletriangle.dungeon.block.tile.TileEntityRedstoneLink;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;
import possibletriangle.dungeon.block.tile.TileFakeWall;
import possibletriangle.dungeon.pallete.Pallete;

import java.util.ArrayList;

public class ModBlocks {

    public static ArrayList<Block> LIST = new ArrayList<>();

    public static final Block FLOOR = new BlockPlaceholder(Pallete.Type.FLOOR);
    public static final Block WALL = new BlockPlaceholder(Pallete.Type.WALL);
    public static final Block LAMP = new BlockPlaceholder(Pallete.Type.LAMP).setLightLevel(1F);
    public static final Block GRASS = new BlockPlaceholder(Pallete.Type.GRASS);
    public static final Block DIRT = new BlockPlaceholder(Pallete.Type.DIRT);
    public static final Block RUNE = new BlockPlaceholder(Pallete.Type.RUNE);
    public static final Block GEM = new BlockPlaceholder(Pallete.Type.GEM);
    public static final Block LEAVES = new BlockPlaceholder(Pallete.Type.LEAVES);
    public static final Block LEAVES2 = new BlockPlaceholder(Pallete.Type.LEAVES2);
    public static final Block KEY_STONE = new BlockPlaceholder(Pallete.Type.KEY_STONE);

    public static final Block PILLAR = new BlockPlaceholderPillar(Pallete.Type.PILLAR);
    public static final Block LOG = new BlockPlaceholderPillar(Pallete.Type.LOG);
    public static final Block LOG2 = new BlockPlaceholderPillar(Pallete.Type.LOG2);

    public static final Block STAIRS = new BlockStairsPlaceholder(Pallete.Type.STAIRS, FLOOR.getDefaultState());
    public static final Block STAIRS_WALL = new BlockStairsPlaceholder(Pallete.Type.STAIRS_WALL, WALL.getDefaultState());
    public static final Block SLAB = new BlockSlabPlaceholder(Pallete.Type.SLAB, false);

    public static final Block PLANT = new BlockPlaceholderPlant(Pallete.Type.PLANT);

    public static final Block BREAKABLE_ROCK = new BlockMod("breakable_rock", Material.ROCK);
    public static final Block BREAKABLE_ROCK_HARDER = new BlockMod("breakable_rock_harder", Material.ROCK);

    public static final BlockSpawn SPAWN = new BlockSpawn();
    public static final BlockRedstoneLink REDSTONE_LINK = new BlockRedstoneLink();
    public static final BlockFakeWall FAKE_WALL = new BlockFakeWall();
    public static final BlockFakeWallSided FAKE_WALL_SIDED = new BlockFakeWallSided();

    public static void register(IForgeRegistry<Block> registry) {

        LIST.add(FLOOR);
        LIST.add(WALL);
        LIST.add(LAMP);
        LIST.add(GRASS);
        LIST.add(STAIRS);
        LIST.add(STAIRS_WALL);
        LIST.add(SLAB);
        LIST.add(PLANT);
        LIST.add(RUNE);
        LIST.add(GEM);
        LIST.add(PILLAR);
        LIST.add(LOG);
        LIST.add(LOG2);
        LIST.add(LEAVES);
        LIST.add(LEAVES2);
        LIST.add(DIRT);
        LIST.add(KEY_STONE);

        LIST.add(SPAWN);

        LIST.add(BREAKABLE_ROCK);
        LIST.add(BREAKABLE_ROCK_HARDER);

        LIST.add(REDSTONE_LINK);
        //LIST.add(FAKE_WALL);
        //LIST.add(FAKE_WALL_SIDED);

        for(Block block : LIST) {

            block.setCreativeTab(CreativeTabs.SEARCH);
            registry.register(block);

        }

        GameRegistry.registerTileEntity(TileEntitySpawn.class, new ResourceLocation(Dungeon.MODID, "spawn"));
        GameRegistry.registerTileEntity(TileEntityRedstoneLink.class, new ResourceLocation(Dungeon.MODID, "redstone_link"));
        GameRegistry.registerTileEntity(TileFakeWall.class, new ResourceLocation(Dungeon.MODID, "fake_wall"));

    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {

        for(Block block : LIST) {

            registry.register(createItemBlock(block));

        }

    }

    public static void registerModels() {

        for(Block block : LIST) {

            registerItemModel(Item.getItemFromBlock(block), block.getRegistryName());

        }

        ModelLoader.setCustomStateMapper(REDSTONE_LINK, new StateMap.Builder().ignore(BlockRedstoneLink.POWER).build());

    }

    public static void registerItemModel(Item itemBlock, ResourceLocation name) {
        Dungeon.proxy.registerItemRenderer(itemBlock, 0, name.getResourcePath());
    }

    public static Item createItemBlock(Block block) {
        return new ItemBlock(block).setRegistryName(block.getRegistryName());
    }

}
