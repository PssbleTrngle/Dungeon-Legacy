package possibletriangle.dungeon.palette;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.placeholder.IPlaceholder;
import possibletriangle.dungeon.block.placeholder.TemplateType;
import possibletriangle.dungeon.palette.providers.BlockProvider;
import possibletriangle.dungeon.palette.providers.IStateProvider;
import possibletriangle.dungeon.util.RandomCollection;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Palette {

    public static final int MAX_VARIANT = 32;

    private static final IStateProvider DEFAULT = new BlockProvider(Blocks.SPONGE);
    private static final ResourceLocation DEFAULT_MOB = new ResourceLocation("zombie");
    private static final Predicate<ResourceLocation> FILTER = r -> r != null && ModList.get().isLoaded(r.getNamespace());

    private static final RandomCollection<Palette> VALUES = new RandomCollection<>();

    private final HashMap<TemplateType<?>, IStateProvider> blocks = new HashMap<>();
    public final float weight;
    private final ResourceLocation parent;
    public final Biome biome;
    private final ResourceLocation name;
    @Nullable
    private final Predicate<DimensionType> dimensions;

    public ResourceLocation getName() {
        return this.name;
    }

    private RandomCollection<ResourceLocation> mobs = new RandomCollection<>();

    /**
     * Palettes are used to replace {@link IPlaceholder} blocks
     *
     * @param weight The weigth used in generation. A heigher weight causes a higher chance of generation
     * @param biome  The biome associated with this palette
     * @param parent The palette used as a fallback
     */
    public Palette(ResourceLocation name, float weight, Biome biome, ResourceLocation parent, Predicate<DimensionType> dimensions) {
        this.parent = parent;
        this.biome = biome;
        this.weight = weight;
        this.name = name;
        this.dimensions = dimensions;
    }

    public boolean validDimension(DimensionType type) {
        if (this.dimensions != null) return this.dimensions.test(type);
        return getParent().map(p -> p.validDimension(type)).orElse(false);
    }

    /**
     * Returns a random palette using the provided seeded random
     *
     * @param random The seeded random
     */
    public static Palette random(Random random) {
        return VALUES.next(random).orElseThrow(NullPointerException::new);
    }

    public final void put(IStateProvider block, TemplateType... types) {
        if (block.isValid())
            for (TemplateType type : types)
                this.blocks.putIfAbsent(type, block);
    }

    public ResourceLocation randomMob(Random random) {
        return this.mobs.next(random).orElseGet(() -> getParent().map(parent -> parent.randomMob(random)).orElse(DEFAULT_MOB));
    }

    private Optional<Palette> getParent() {
        return find(this.parent).filter(parent -> !this.equals(parent));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Palette)
            return getName().toString().equalsIgnoreCase(((Palette) obj).getName().toString());
        return super.equals(obj);
    }

    private IStateProvider blockFor(TemplateType type) {
        IStateProvider def = getParent().map(p -> p.blockFor(type)).orElse(DEFAULT);
        return this.blocks.getOrDefault(type, def);
    }

    public BlockState blockFor(TemplateType type, Random random, int variant, BlockState from) {
        long seed = random.nextLong();
        IStateProvider provider = blockFor(type);
        BlockState applied = from.func_235904_r_().stream()
                .reduce(provider.getBlock(variant, new Random(seed)).getDefaultState(),
                        (s, p) -> s.func_235901_b_(p) ? s.with((Property) p, from.get(p)) : s, (a, b) -> a);
        return provider.apply(applied, variant, new Random(seed));
    }

    public Stream<BlockState> blocksFor(TemplateType type) {
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

        if (find(palette.getName()).isPresent()) {
            DungeonMod.LOGGER.warn("Trying to register already existing Palette '{}'", palette.getName());
            return;
        }

        String[] missing = TemplateType.values()
                .filter(t -> !palette.blocks.containsKey(t))
                .map(TemplateType::name)
                .toArray(String[]::new);

        if (missing.length > 0) DungeonMod.LOGGER.info("Palette '{}' is missing {} types ({})",
                palette.getName(),
                missing.length,
                String.join(", ", missing)
        );

        VALUES.add(palette, palette.weight);
    }

}
