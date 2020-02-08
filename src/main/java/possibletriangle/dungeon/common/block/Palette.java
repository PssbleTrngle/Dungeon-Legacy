package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.placeholder.IPlaceholder;
import possibletriangle.dungeon.common.block.placeholder.Type;
import possibletriangle.dungeon.common.world.room.StateProvider;
import possibletriangle.dungeon.helper.BlockCollection;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ObjectHolder(DungeonMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Palette extends ForgeRegistryEntry<Palette> {

    public static final int MAX_VARIANT = 32;

    @ObjectHolder("stone")
    public static final Palette STONE = null;

    @ObjectHolder("nature")
    public static final Palette NATURE = null;

    @ObjectHolder("nether")
    public static final Palette NETHER = null;

    private static final BlockState DEFAULT = Blocks.SPONGE.getDefaultState();
    private static final ResourceLocation DEFAULT_MOB = new ResourceLocation("zombie");
    private static final Predicate<ResourceLocation> FILTER = r -> r != null && ModList.get().isLoaded(r.getNamespace());

    private static final RandomCollection<Palette> VALUES = new RandomCollection<>();

    private final HashMap<Type, BlockCollection> blocks = new HashMap<>();
    private final float weight;
    private final Supplier<Palette> parent;
    public final Supplier<Biome> biome;

    private RandomCollection<ResourceLocation> mobs = new RandomCollection<>();

    /**
     * Palettes are used to replace {@link IPlaceholder} blocks
     * @param weight The weigth used in generation. A heigher weight causes a higher chance of generation
     * @param biome The biome associated with this palette
     * @param parent The palette used as a fallback
     */
    public Palette(float weight, Supplier<Biome> biome, Supplier<Palette> parent) {
        this.parent = parent;
        this.biome = biome;
        this.weight = weight;
    }

    /**
     * Palettes are used to replace {@link possibletriangle.dungeon.common.block.Palette}
     * @param weight The weigth used in generation. A heigher weight causes a higher chance of generation
     * @param biome The biome associated with this palette
     */
    public Palette(float weight, Supplier<Biome> biome) {
        this(weight, biome, () -> STONE);
    }

    public Palette addMobs(RandomCollection<ResourceLocation> mobs) {
        this.mobs.addAll(mobs.filter(FILTER));
        return this;
    }

    public Palette setMobs(RandomCollection<ResourceLocation> mobs) {
        this.mobs = mobs.filter(FILTER);
        return this;
    }

    /**
     * Returns a random palette using the provided seeded random
     * @param random The seeded random
     */
    public static Palette random(Random random) {
        return VALUES.next(random).orElseThrow(NullPointerException::new);
    }

    public interface MultiConsumer<P,T> {
        P forTypes(T... types);
    }

    /**
     * Associate a collection of blocks with one ore multiple {@link Type}
     * @param collection the blocks
     */
    public MultiConsumer<Palette,Type> put(BlockCollection collection) {
        return types -> {
            for(Type type : types)
                this.blocks.putIfAbsent(type, collection);
            return this;
        };
    }

    public MultiConsumer<Palette,Type> put(StateProvider... providers) {
        return this.put(new BlockCollection(providers));
    }

    public MultiConsumer<Palette,Type> put(Block... blocks) {
        return this.put(Arrays.stream(blocks).map(Block::getDefaultState).toArray(BlockState[]::new));
    }

    public MultiConsumer<Palette,Type> put(BlockState... states) {
        return this.put(Arrays.stream(states).map(s -> (StateProvider) i -> s).toArray(StateProvider[]::new));
    }

    public ResourceLocation randomMob(Random random) {
        return this.mobs.next(random).orElseGet(() -> {
            Palette parent = this.parent.get();
            if(parent == null || this.getRegistryName().equals(parent.getRegistryName())) return DEFAULT_MOB;
            return parent.randomMob(random);
        });
    }

    public BlockCollection blocksFor(Type type) {
        return this.blocks.getOrDefault(type, new BlockCollection(i -> null));
    }

    public BlockState blockFor(Type type, Random random, int variant) {
        BlockState block = blocksFor(type).next(random).orElse(i -> null).apply(variant);
        if(block != null) return block;

        Palette parent = this.parent.get();
        if(parent == null || this.getRegistryName().equals(parent.getRegistryName())) return DEFAULT;
        return parent.blockFor(type, random, variant);
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
