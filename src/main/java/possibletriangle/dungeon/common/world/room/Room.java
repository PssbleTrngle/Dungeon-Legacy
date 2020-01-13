package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.Vec3i;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class Room extends ForgeRegistryEntry<Room> {

    public enum Type {
        HALLWAY, ROOM
    }

    private static final HashMap<Type, RandomCollection<Room>> VALUES = new HashMap<>();

    private float weight = 1F;
    private final Type type;

    public Room(Type type) {
        this.type = type;
    }

    public Room setWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public static Room random(Type type, Random random) {
        return VALUES.getOrDefault(type, new RandomCollection<>()).next(random);
    }

    public abstract void generate(DungeonChunk chunk, int floor, Random random, DungeonSettings settings);

    /**
     * X and Z are the amount of chunks, Y is the amount of floors
     * @return The amount of space a room requires
     */
    public Vec3i getSize(DungeonSettings options) {
        return new Vec3i(1, 1, 1);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRoomsRegistry(final RegistryEvent.Register<Room> event) {

        DungeonMod.LOGGER.info("Registered {} rooms", event.getRegistry().getEntries().size());
        Arrays.stream(Type.values()).forEach(t -> VALUES.putIfAbsent(t, new RandomCollection<>()));

        event.getRegistry().forEach(room -> VALUES.get(room.type).add(room, room.weight));

    }

}
