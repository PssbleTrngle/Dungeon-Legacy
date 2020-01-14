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

    public enum Type {
        HALLWAY, ROOM
    }

    private StructureMetadata metadata;

    public StructureMetadata getMeta() {
        return this.metadata;
    }

    private void setMeta(StructureMetadata metadata) {
        this.metadata = metadata;
    }

    public static int count() {
        return VALUES.values().stream()
                .map(RandomCollection::size)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public static void register(Room room, Type type, StructureMetadata metadata) {
        RandomCollection<Room> collection = VALUES.getOrDefault(type, new RandomCollection<>());
        room.setMeta(metadata);
        collection.add(room, metadata.weight);
        VALUES.put(type, collection);
    }

    private static final HashMap<Type, RandomCollection<Room>> VALUES = new HashMap<>();

    public static Room random(Type type, Random random) {
        return VALUES.getOrDefault(type, new RandomCollection<>()).next(random);
    }

    public abstract void generate(DungeonChunk chunk, Random random, GenerationContext context);

    /**
     * X and Z are the amount of chunks, Y is the amount of floors
     * @return The amount of space a room requires
     */
    public Vec3i getSize(DungeonSettings options) {
        return new Vec3i(1, 1, 1);
    }

    /*
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRoomsRegistry(final RegistryEvent.Register<Room> event) {

        DungeonMod.LOGGER.info("Registered {} rooms", event.getRegistry().getEntries().size());
        Arrays.stream(Type.values()).forEach(t -> VALUES.putIfAbsent(t, new RandomCollection<>()));

        event.getRegistry().forEach(room -> VALUES.get(room.type).add(room, room.weight));

    }
    */
}
