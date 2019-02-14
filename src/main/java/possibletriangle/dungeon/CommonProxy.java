package possibletriangle.dungeon;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import possibletriangle.dungeon.block.ModBlocks;
import possibletriangle.dungeon.generator.WorldTypeDungeon;
import possibletriangle.dungeon.loot.LootManager;
import possibletriangle.dungeon.pallete.*;
import possibletriangle.dungeon.generator.rooms.RoomLabyrint;
import possibletriangle.dungeon.generator.rooms.RoomManager;
import possibletriangle.dungeon.generator.rooms.RoomSpawn;
import possibletriangle.dungeon.generator.rooms.wall.WallRandom;
import possibletriangle.dungeon.structures.DungeonStructur;
import possibletriangle.dungeon.generator.rooms.RoomStructure;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preinit(FMLPreInitializationEvent event) {

         RoomSpawn.SPAWN = new RoomSpawn();

        new PalleteNether();
        new PaletteStonebrick();
        new PalleteEnd();
        new PalleteEndQuark();
        new PaletteMossy();
        new PalettePrismarine();

        registerRenderers();


    }

    public void init(FMLInitializationEvent event) {

        LootManager.reload();

        RoomManager.register(new RoomStructure("tower", "spiral_stairs_bottom").genWall().noCeil(), 5);
        RoomManager.register(new RoomStructure("tower", "spiral_stairs_top").genWall(), 0);
        RoomManager.get("spiral_stairs_bottom").addDependendent(1, "spiral_stairs_top");

        RoomManager.register(new RoomStructure("tower", "slime_tower_bottom").genWall().noCeil(), 5);
        RoomManager.register(new RoomStructure("tower", "slime_tower_top").genWall(), 0);
        RoomManager.get("slime_tower_bottom").addDependendent(1, "slime_tower_top");

        RoomManager.register(new RoomStructure("room", "long_start").genWall(), 2);
        RoomManager.register(new RoomStructure("room", "long_end").genWall(), 0);
        RoomManager.get("long_start").addDependendent(EnumFaceDirection.EAST, "long_end");

        RoomManager.register(new RoomStructure("room", "long_nw_bottom").noCeil().onlyBottom(), 1);
        RoomManager.register(new RoomStructure("room", "long_ne_bottom").noCeil(), 0);
        RoomManager.register(new RoomStructure("room", "long_sw_bottom").noCeil(), 0);
        RoomManager.register(new RoomStructure("room", "long_se_bottom").noCeil(), 0);
        RoomManager.register(new RoomStructure("room", "long_nw_top"), 0);
        RoomManager.register(new RoomStructure("room", "long_ne_top"), 0);
        RoomManager.register(new RoomStructure("room", "long_sw_top"), 0);
        RoomManager.register(new RoomStructure("room", "long_se_top"), 0);
        RoomManager.get("long_nw_bottom").addDependendent(EnumFaceDirection.EAST, "long_ne_bottom");
        RoomManager.get("long_nw_bottom").addDependendent(EnumFaceDirection.SOUTH, "long_sw_bottom");
        RoomManager.get("long_ne_bottom").addDependendent(EnumFaceDirection.SOUTH, "long_se_bottom");
        RoomManager.get("long_nw_bottom").addDependendent(1, "long_nw_top");
        RoomManager.get("long_nw_top").addDependendent(EnumFaceDirection.EAST, "long_ne_top");
        RoomManager.get("long_nw_top").addDependendent(EnumFaceDirection.SOUTH, "long_sw_top");
        RoomManager.get("long_ne_top").addDependendent(EnumFaceDirection.SOUTH, "long_se_top");

        RoomManager.register(new RoomStructure("tower", "freefall_bottom").noCeil().genWall().onlyBottom(), 3);
        RoomManager.register(new RoomStructure("tower", "freefall_middle").noCeil().genWall(), 0);
        RoomManager.register(new RoomStructure("tower", "freefall_top").genWall(), 0);
        RoomManager.get("freefall_bottom").addDependendent(1, "freefall_middle");
        RoomManager.get("freefall_bottom").addDependendent(1, "freefall_top");
        RoomManager.get("freefall_middle").addDependendent(1, "freefall_top");

        RoomManager.register(new RoomLabyrint(), 10);
        RoomManager.register(new RoomStructure("corridor", "straight_0").genWall(), 10);
        RoomManager.register(new RoomStructure("corridor", "straight_1").genWall(), 10);
        RoomManager.register(new RoomStructure("corridor", "twisted_0").genWall(), 10);

        RoomManager.register(new RoomStructure("room", "garden").genWall(), 3);
        RoomManager.register(new RoomStructure("room", "fountain").genWall(), 2);
        RoomManager.register(new RoomStructure("room", "lava").genWall(), 4);

        RoomManager.register(new RoomStructure("room", "spawner_0").genWall(), 4);

        RoomManager.register(new RoomStructure("room", "atrium_0").genWall().noCeil().onlyTop(), 6);

        WallRandom wall_random = new WallRandom(1)
                .add(new DungeonStructur("wall/door/rect_open"), 2)
                .add(new DungeonStructur("wall/door/big_open"), 2)
                .add(new DungeonStructur("wall/door/small_open"), 2)

                .add(new DungeonStructur("wall/door/bars_broken"), 0.5)
                .add(new DungeonStructur("wall/door/bars_closed"), 0.5)
                .add(new DungeonStructur("wall/door/bars_open"), 0.5)

                .add(new DungeonStructur("wall/door/fence"), 0.5)
                .add(new DungeonStructur("wall/door/iron_0"), 0.5)
                .add(new DungeonStructur("wall/door/iron_1"), 0.5)

                .add(new DungeonStructur("wall/door/lava"), 0.2)
                .add(new DungeonStructur("wall/door/vines"), 0.8)
                .add(new DungeonStructur("wall/door/wood"), 0.8)

                .add(new DungeonStructur("wall/door/breakable_0"), 0.6)
                .add(new DungeonStructur("wall/door/breakable_1"), 0.3);

        if(Loader.isModLoaded("secretroomsmod"))
            wall_random
                    .add(new DungeonStructur("wall/door/fake_0"), 2)
                    .add(new DungeonStructur("wall/door/fake_1"), 2);

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

    public void registerRenderers() {
    }

}
