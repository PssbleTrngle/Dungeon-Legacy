package possibletriangle.dungeon.world.generator.biome;

import com.google.common.collect.Maps;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import possibletriangle.dungeon.palette.Palette;
import possibletriangle.dungeon.util.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class BaseLayer implements IAreaTransformer0 {

    private final Map<Palette, SimplexNoiseGenerator> noise = Maps.newHashMap();
    private static final double MOD = 250;

    public BaseLayer(long seed, DimensionType dimension) {
        Random r = new Random(seed);
        Palette.values().stream()
                .filter(p -> p.validDimension(dimension))
                .forEach(palette -> noise.put(palette, new SimplexNoiseGenerator(new Random(r.nextLong()))));
    }

    private Pair<Palette,Double> valueFor(Palette palette, int x, int z) {
        return new Pair<>(palette, this.noise.get(palette).getValue(x / MOD, z / MOD) * palette.weight);
    }

    public Optional<Palette> paletteAt(int x, int z) {
        return this.noise.keySet().stream()
                .map(p -> valueFor(p, x, z))
                .max(Comparator.comparingDouble(Pair::getSecond))
                .map(Pair::getFirst);
    }

    @Override
    public int apply(INoiseRandom noise, int x, int z) {
        Biome biome =  this.paletteAt(x, z)
                .map(p -> p.biome)
                .orElse(Biomes.THE_VOID);
        return Registry.BIOME.getId(biome);
    }
}
