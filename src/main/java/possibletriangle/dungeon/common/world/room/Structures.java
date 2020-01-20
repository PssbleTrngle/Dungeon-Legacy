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

    private static final HashMap<StructureType, RandomCollection<Generateable>> VALUES = new HashMap<>();

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
    public static void register(Generateable structure, StructureType type) {

        if(type.valid.test(structure)) {

            RandomCollection<Generateable> collection = VALUES.getOrDefault(type, new RandomCollection<>());
            collection.add(structure, structure.getMeta().weight);
            VALUES.put(type, collection);

        } else {
            DungeonMod.LOGGER.error("Trying to register a structure which is invalid for type '{}': '{}'", type.getRegistryName(), structure.getMeta().display);
        }
    }

    /**
     * Retrieve a random structure for a specific type
     * @param type The structure type
     * @param random The seeded random
     */
    public static Generateable random(StructureType type, Random random) {
        return VALUES.getOrDefault(type, new RandomCollection<>()).next(random);
    }
}
