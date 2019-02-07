package possibletriangle.dungeon;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import possibletriangle.dungeon.block.ModBlocks;
import possibletriangle.dungeon.generator.WorldTypeDungeon;
import possibletriangle.dungeon.pallete.*;
import possibletriangle.dungeon.rooms.RoomLabyrint;
import possibletriangle.dungeon.rooms.RoomManager;
import possibletriangle.dungeon.rooms.RoomSpawn;
import possibletriangle.dungeon.rooms.wall.WallRandom;
import possibletriangle.dungeon.structures.DungeonStructur;
import possibletriangle.dungeon.structures.RoomStructure;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preinit(FMLPreInitializationEvent event) {

        new PalleteNether();
        new PaletteStonebrick();
        new PalleteEnd();
        new PaletteMossy();
        new PalettePrismarine();

    }

    public void init(FMLInitializationEvent event) {

        /*
        for(EnumDyeColor color : EnumDyeColor.values()) {
            RoomManager.register(RoomColor.ROOMS[color.ordinal()] = new RoomColor(color), color.ordinal());
            RoomManager.register(RoomColorGlass.ROOMS[color.ordinal()] = new RoomColorGlass(color), color.ordinal());
            RoomManager.register(new RoomQuarterColor(color), color.ordinal());
        }
        */


        //RoomManager.register(new RoomMulti(), 10.0);

        RoomManager.register(new RoomStructure("tower", "spiral_stairs_bottom").genWall().noCeil(), 5);
        RoomManager.register(new RoomStructure("tower", "spiral_stairs_top").genWall(), 0);
        RoomManager.get("spiral_stairs_bottom").addDependendent(1, "spiral_stairs_top");

        RoomManager.register(new RoomStructure("tower", "slime_tower_bottom").noCeil(), 5);
        RoomManager.register(new RoomStructure("tower", "slime_tower_top"), 0);
        RoomManager.get("slime_tower_bottom").addDependendent(1, "slime_tower_top");

        RoomManager.register(new RoomStructure("tower", "freefall_bottom").noCeil().genWall().onlyBottom(), 10);
        RoomManager.register(new RoomStructure("tower", "freefall_middle").noCeil().genWall(), 0);
        RoomManager.register(new RoomStructure("tower", "freefall_top").genWall(), 0);
        RoomManager.get("freefall_bottom").addDependendent(1, "freefall_middle");
        RoomManager.get("freefall_bottom").addDependendent(2, "freefall_top");

        RoomManager.register(new RoomLabyrint(), 10);
        RoomManager.register(new RoomStructure("corridor", "straight_0").genWall(), 10);
        RoomManager.register(new RoomStructure("corridor", "straight_1").genWall(), 10);
        RoomManager.register(new RoomStructure("corridor", "twisted_0").genWall(), 10);

        RoomManager.register(new RoomStructure("room", "garden").genWall(), 2);
        RoomManager.register(new RoomStructure("room", "fountain").genWall(), 2);
        RoomManager.register(new RoomStructure("room", "lava").genWall(), 4);


        //new WallStructure("all", 1);
        new WallRandom(1)
                .add(new DungeonStructur("wall/door/rect_open"), 2)
                .add(new DungeonStructur("wall/door/big_open"), 2)
                .add(new DungeonStructur("wall/door/small_open"), 2)

                .add(new DungeonStructur("wall/door/bars_broken"), 0.5)
                .add(new DungeonStructur("wall/door/bars_closed"), 0.5)
                .add(new DungeonStructur("wall/door/bars_open"), 0.5)

                .add(new DungeonStructur("wall/door/fence"), 0.5)
                .add(new DungeonStructur("wall/door/iron"), 0.5)

                .add(new DungeonStructur("wall/door/lava"), 0.2)
                .add(new DungeonStructur("wall/door/vines"), 0.8)
                .add(new DungeonStructur("wall/door/wood"), 0.8);

    }

    public void postinit(FMLPostInitializationEvent event) {

        new WorldTypeDungeon();

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModBlocks.registerItemBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModBlocks.registerModels();
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

}
