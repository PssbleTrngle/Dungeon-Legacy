package possibletriangle.dungeon.rooms;

import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.RandomCollection;
import possibletriangle.dungeon.rooms.quarter.RoomQuarter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RoomManager {

    private static final RandomCollection<Room> LIST = new RandomCollection<>();
    private static final HashMap<ResourceLocation, Room> MAP = new HashMap<>();
    private static final RandomCollection<RoomQuarter> LIST_QUARTER = new RandomCollection<>();

    public static void register(RoomQuarter room, double probability) {
        if(probability > 0)
            LIST_QUARTER.add(probability, room);
    }

    public static void register(Room room, double probability) {
        if(probability > 0)
            LIST.add(probability, room);
        MAP.put(room.getName(), room);
    }

    public static Room random(Random r) {
        return LIST.next(r);
    }

    public static Room randomFor(int floor, DungeonOptions options, Random r) {
        Room random = null;
        int c = 0;

        while(!(random = random(r)).canReallyGoOnFloor(floor, options.floorCount) && c < 100)
            c++;

        return random;
    }

    public static RoomQuarter randomQuarter(Random r) {
        return LIST_QUARTER.next(r);
    }

    public static Room get(ResourceLocation r) {
        return MAP.get(r);
    }

    public static Room get(String r) {
        return MAP.get(new ResourceLocation(r));
    }


}
