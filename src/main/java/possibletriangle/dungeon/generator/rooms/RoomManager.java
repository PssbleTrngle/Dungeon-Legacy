package possibletriangle.dungeon.generator.rooms;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.HashMap;
import java.util.Random;

public class RoomManager {

    private static final RandomCollection<Room> LIST = new RandomCollection<>();
    private static final HashMap<ResourceLocation, Room> MAP = new HashMap<>();

    public static void register(Room room, double probability) {
        if(probability > 0)
            LIST.add(probability, room);
        MAP.put(room.getName(), room);
    }

    public static Room random(Random r) {
        return LIST.next(r);
    }

    public static Room randomFor(int floor, DungeonOptions options, Random r, int chunkX, int chunkZ, Rotation rotation, World world) {
        Room random;
        int c = 0;

        while(!(random = random(r)).canReallyGoOnFloor(floor, chunkX, chunkZ, options.floorCount(), rotation, world) && c < 100)
            c++;

        if(c >= 100)
            Dungeon.LOGGER.info("Random room selection took to many tries");

        return random;
    }

    public static Room get(ResourceLocation r) {
        return MAP.get(r);
    }

    public static Room get(String r) {
        return MAP.get(new ResourceLocation(r));
    }


}
