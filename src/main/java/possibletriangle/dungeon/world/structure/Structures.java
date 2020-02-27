package possibletriangle.dungeon.world.structure;

import net.minecraftforge.fml.common.Mod;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.util.RandomCollection;

import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class Structures {

    private static final HashMap<StructureType, RandomCollection<IStructure>> VALUES = new HashMap<>();

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
                .orElseGet(() -> 0);
    }

    /**
     * Register a structure to a specific type
     * @param structure The structure
     * @param type The type, defining the use of this structure
     */
    public static void register(IStructure structure, StructureType type) {

        if(type.valid.test(structure)) {

            RandomCollection<IStructure> collection = VALUES.getOrDefault(type, new RandomCollection<>());
            collection.add(structure, structure.getMeta().getWeight());
            VALUES.put(type, collection);

        } else {
            DungeonMod.LOGGER.error("Trying to register a structure which is invalid for type '{}': '{}'", type.getRegistryName(), structure.getMeta().getDisplay());
        }
    }

    /**
     * Retrieve a random structure for a specific type
     * @param type The structure type
     * @param random The seeded random
     */
    public static IStructure random(StructureType type, Random random) {
        return VALUES.getOrDefault(type, new RandomCollection<>()).next(random).orElseThrow(() -> new NullPointerException("No structures register for requested type " + type.getRegistryName()));
    }
}
