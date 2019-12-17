package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.room.Room;
import possibletriangle.dungeon.helper.RandomCollection;
import sun.security.action.PutAllAction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Palette extends ForgeRegistryEntry<Palette> {

    private static final RandomCollection<Palette> VALUES = new RandomCollection<>();

    private final HashMap<Type, RandomCollection<Supplier<Block>>> blocks = new HashMap<>();
    private final RandomCollection<Supplier<Block>> def = new RandomCollection<>(() -> Blocks.SPONGE);
    private final float weight;

    public Palette(float weight) {
        this.weight = weight;
    }

    public static Palette random(Random random) {
        return VALUES.next(random);
    }

    public Palette put(RandomCollection<Supplier<Block>> collection, Type... types) {
        for(Type type : types)
            this.blocks.putIfAbsent(type, collection);
        return this;
    }

    public Palette put(Supplier<Block> block, Type... types) {
        return this.put(new RandomCollection<>(block), types);
    }

    public Block blockFor(Type template, Random random) {
        return this.blocks.getOrDefault(template, def).next(random).get();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRoomsRegistry(final RegistryEvent.Register<Palette> event) {

        DungeonMod.LOGGER.info("Registered {} palettes", event.getRegistry().getEntries().size());
        event.getRegistry().forEach(pallete -> VALUES.add(pallete, pallete.weight));

    }

}
