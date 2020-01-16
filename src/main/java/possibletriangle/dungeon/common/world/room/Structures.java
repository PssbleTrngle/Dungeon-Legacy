package possibletriangle.dungeon.common.world.room;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.HashMap;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class Structures {

    public enum Type {

        /**
         * Every black field of a chessboard
         */
        ROOM(Type::validRoom),
        /**
         * Every white field of a chessboard
         */
        HALLWAY(Type::validRoom),
        /**
         * The doors used to be randomly placed at the room walls
         */
        DOOR(s -> true),
        /**
         * Rare rooms containing a boss enemy
         * Spawning at a minimum distance from other boss rooms and the world spawn
         */
        BOSS(Type::validRoom),
        /**
         * Spawning at a minimum distance
         */
        BASE(Type::validRoom);

        static boolean validRoom(Generateable structure) {
            Vec3i size = structure.getActualSize();
            int floorHeight = DungeonSettings.FLOOR_HEIGHT;
            return size.getX() % 16 == 0 && size.getZ() % 16 == 0 && size.getY() <= floorHeight || size.getY() % (floorHeight + 1) == 0;
        }

        Predicate<Generateable> valid;
        Type(Predicate<Generateable> valid) {
            this.valid = valid;
        }

    }

    private static final HashMap<Type, RandomCollection<Generateable>> VALUES = new HashMap<>();

    /**
     * Unload structures
     */
    public static void clear() {
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

        if(type.valid.test(structure)) {

            RandomCollection<Generateable> collection = VALUES.getOrDefault(type, new RandomCollection<>());
            collection.add(structure, structure.getMeta().weight);
            VALUES.put(type, collection);

        } else {
            DungeonMod.LOGGER.error("Trying to register a structure which is invalid for type '{}'", type.name());
        }
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
