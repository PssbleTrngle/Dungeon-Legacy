package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.helper.RandomCollection;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ObjectHolder(DungeonMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Palette extends ForgeRegistryEntry<Palette> {

    @ObjectHolder("stone")
    public static final Palette STONE = null;

    @ObjectHolder("nature")
    public static final Palette NATURE = null;

    @ObjectHolder("nether")
    public static final Palette NETHER = null;

    private static final Block DEFAULT = Blocks.SPONGE;
    private static final RandomCollection<Palette> VALUES = new RandomCollection<>();

    private final HashMap<Type, RandomCollection<Supplier<Block>>> blocks = new HashMap<>();
    private final float weight;
    private final Supplier<Palette> parent;
    public final Supplier<Biome> biome;

    public Palette(float weight, Supplier<Biome> biome) {
        this(weight, biome, () -> STONE);
    }

    public Palette(float weight, Supplier<Biome> biome, Supplier<Palette> parent) {
        this.parent = parent;
        this.biome = biome;
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

    private RandomCollection<Supplier<Block>> blocksFor(Type type) {
        return this.blocks.getOrDefault(type, new RandomCollection<>(() -> null));
    }

    public Block blockFor(Type type, Random random) {
        Block block = blocksFor(type).next(random).get();
        if(block != null) return block;

        Palette parent = this.parent.get();
        if(parent == null || this.getRegistryName().equals(parent.getRegistryName())) return DEFAULT;
        return parent.blockFor(type, random);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPalettesRegistry(final RegistryEvent.Register<Palette> event) {

        DungeonMod.LOGGER.info("Registered {} palettes", event.getRegistry().getEntries().size());
        event.getRegistry().forEach(palette -> {
            String[] missing = Arrays.stream(Type.values())
                    .filter(t -> !palette.blocks.containsKey(t))
                    .map(Enum::name)
                    .toArray(String[]::new);

            DungeonMod.LOGGER.info("Palette '{}' is missing {} types ({})",
                    palette.getRegistryName(),
                    missing.length,
                    Arrays.stream(missing).collect(Collectors.joining(", "))
            );

            VALUES.add(palette, palette.weight);
        });

    }

}
