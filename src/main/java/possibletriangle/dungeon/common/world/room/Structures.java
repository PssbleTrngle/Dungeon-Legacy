package possibletriangle.dungeon.common.world.room;

import net.minecraftforge.fml.common.Mod;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class Structures {

    public enum Type {
        HALLWAY, ROOM, DOOR, BOSS, BASE
    }

    private static final HashMap<Type, RandomCollection<Generateable>> VALUES = new HashMap<>();

    public static final clear() {
        VALUES.clear();
    }

    public static int count() {
        return VALUES.values().stream()
                .map(RandomCollection::size)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public static void register(Generateable room, Type type) {
        RandomCollection<Generateable> collection = VALUES.getOrDefault(type, new RandomCollection<>());
        collection.add(room, room.getMeta().weight);
        VALUES.put(type, collection);
    }

    public static Generateable random(Type type, Random random) {
        return VALUES.getOrDefault(type, new RandomCollection<>()).next(random);
    }
}
