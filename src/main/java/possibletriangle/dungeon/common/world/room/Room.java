package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.lookup.StrMatcher;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.structure.StructureMetadata;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class Room {

    public static interface Generateable {

        void generate(DungeonChunk chunk, Random random, GenerationContext context);
    
        /**
         * X and Z are the amount of chunks, Y is the amount of floors
         * @return The amount of space required
         */
        default Vec3i getSize(DungeonSettings options) {
            return new Vec3i(1, 1, 1);
        }

    }

    public enum Type {
        HALLWAY, ROOM
    }

    private static final HashMap<Type, RandomCollection<Generateable>> VALUES = new HashMap<>();

    public static int count() {
        return VALUES.values().stream()
                .map(RandomCollection::size)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public static void register(Generateable room, Type type, StructureMetadata metadata) {
        RandomCollection<Generateable> collection = VALUES.getOrDefault(type, new RandomCollection<>());
        room.setMeta(metadata);
        collection.add(room, metadata.weight);
        VALUES.put(type, collection);
    }

    public static Generateable random(Type type, Random random) {
        return VALUES.getOrDefault(type, new RandomCollection<>()).next(random);
    }
}
