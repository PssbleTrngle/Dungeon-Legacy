package possibletriangle.dungeon.palette;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.placeholder.IPlaceholder;
import possibletriangle.dungeon.common.block.placeholder.Type;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ObjectHolder(DungeonMod.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Palette {

    public static final int MAX_VARIANT = 32;

    private static final IStateProvider DEFAULT = new BlockProvider(Blocks.SPONGE);
    private static final ResourceLocation DEFAULT_MOB = new ResourceLocation("zombie");
    private static final Predicate<ResourceLocation> FILTER = r -> r != null && ModList.get().isLoaded(r.getNamespace());

    private static final RandomCollection<Palette> VALUES = new RandomCollection<>();

    private final HashMap<Type<?>, IStateProvider> blocks = new HashMap<>();
    private final float weight;
    private final ResourceLocation parent;
    public final Supplier<Biome> biome;
    private final ResourceLocation name;

    public ResourceLocation getName() {
        return this.name;
    }

    private RandomCollection<ResourceLocation> mobs = new RandomCollection<>();

    /**
     * Palettes are used to replace {@link IPlaceholder} blocks
     * @param weight The weigth used in generation. A heigher weight causes a higher chance of generation
     * @param biome The biome associated with this palette
     * @param parent The palette used as a fallback
     */
    public Palette(ResourceLocation name, float weight, Supplier<Biome> biome, ResourceLocation parent) {
        this.parent = parent;
        this.biome = biome;
        this.weight = weight;
        this.name = name;
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

    public final void put(IStateProvider block, Type... types) {
        if(block.isValid())
            for(Type type : types)
                this.blocks.putIfAbsent(type, block);
    }

    public ResourceLocation randomMob(Random random) {
        return this.mobs.next(random).orElseGet(() -> getParent().map(parent -> parent.randomMob(random)).orElse(DEFAULT_MOB));
    }

    private Optional<Palette> getParent() {
        return find(this.parent).filter(parent -> parent.getName().equals(getName()));
    }

    private IStateProvider blockFor(Type type) {
        IStateProvider def = getParent().map(p -> p.blockFor(type)).orElse(DEFAULT);
        return this.blocks.getOrDefault(type, def);
    }

    public BlockState blockFor(Type type, Random random, int variant, BlockState from) {
        long seed = random.nextLong();
        IStateProvider provider = blockFor(type);
        BlockState applied = from.getProperties()
                .stream()
                .reduce(provider.getBlock(variant, new Random(seed)).getDefaultState(), (s, p) -> s.has(p) ? s.with((IProperty) p, from.get(p)) : s, (a, b) -> a);
        return provider.apply(applied, variant, new Random(seed));
    }

    public Stream<BlockState> blocksFor(Type type) {
        return this.blocks.getOrDefault(type, DEFAULT).allBlocks();
    }

    public static Collection<Palette> values() {
        return VALUES.all();
    }

    public static Collection<ResourceLocation> keys() {
        return values().stream().map(Palette::getName).collect(Collectors.toList());
    }

    public static Optional<Palette> find(ResourceLocation name) {
        return values().stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    static void clear() {
        VALUES.clear();
    }

    static void register(Palette palette) {

        if(find(palette.getName()).isPresent()) {
            DungeonMod.LOGGER.warn("Trying to register already existing Palette '{}'", palette.getName());
            return;
        }

        String[] missing = Type.values()
                .filter(t -> !palette.blocks.containsKey(t))
                .map(Type::name)
                .toArray(String[]::new);

        if(missing.length > 0) DungeonMod.LOGGER.info("Palette '{}' is missing {} types ({})",
                palette.getName(),
                missing.length,
                String.join(", ", missing)
        );

        VALUES.add(palette, palette.weight);
    }

}
