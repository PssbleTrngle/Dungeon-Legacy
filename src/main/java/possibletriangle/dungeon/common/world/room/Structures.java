package possibletriangle.dungeon.common.world.room;

import net.minecraftforge.fml.common.Mod;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class Structures {

    public enum Type {
        /**
         * Every black field of a chessboard
         */
        ROOM,
        /**
         * Every white field of a chessboard
         */
        HALLWAY,
        /**
         * The doors used to be randomly placed at the room walls
         */
        DOOR,
        /**
         * Rare rooms containg a boss enemy
         * Spawning at a mininum distance from other boss rooms and the world spawn
         */
        BOSS,
        /**
         * Rare rooms containg a boss enemy
         * Spawning at a mininum distance
         */
        BASE
    }

    private static final HashMap<Type, RandomCollection<Generateable>> VALUES = new HashMap<>();

    /**
     * Unload structures
     */
    public static final clear() {
        VALUES.clear();
    }

    /**
     * The amount of loaded Structures
     */
    public static int count() {
        return VALUES.values().stream()
                .map(RandomCollection::size)
                .reduce(Integer::sum)
                .orElse(0);
    }

    /**
     * Register a structure to a specific type
     * @param structure The structure
     * @param type The type, defining the use of this structure
     */
    public static void register(Generateable structure, Type type) {
        RandomCollection<Generateable> collection = VALUES.getOrDefault(type, new RandomCollection<>());
        collection.add(structure, structure.getMeta().weight);
        VALUES.put(type, collection);
    }

    /**
     * Retrieve a random structure for a specific type
     * @param type The structure type
     * @param random The seeded random
     */
    public static Generateable random(Type type, Random random) {
        return VALUES.getOrDefault(type, new RandomCollection<>()).next(random);
    }
}
