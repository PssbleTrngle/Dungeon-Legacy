package possibletriangle.dungeon.world.generator;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import possibletriangle.dungeon.palette.Palette;
import possibletriangle.dungeon.world.generator.biome.BaseLayer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PaletteProvider extends BiomeProvider {

    private final Layer biomes, zoomed;
    private final long seed;
    private final BaseLayer layer;

    private LazyAreaLayerContext createContext(long mod) {
        return new LazyAreaLayerContext(25, seed, mod);
    }

    public PaletteProvider(World world) {
        this.seed = world.getSeed();

        this.layer = new BaseLayer(seed, world.getDimension().getType());
        IAreaFactory<LazyArea> base = this.layer.apply(createContext(1L));
        IAreaFactory<LazyArea> zoomed = VoroniZoomLayer.INSTANCE.apply(createContext(10L), base);

        this.biomes = new Layer(base);
        this.zoomed = new Layer(zoomed);
    }

    public Palette getPalette(ChunkPos pos) {
        BlockPos p = pos.asBlockPos();
        return this.layer.paletteAt(p.getX(), p.getZ()).orElseThrow(NullPointerException::new);
    }

    public Biome getBiome(int x, int y) {
        return this.biomes.func_215738_a(x, y);
    }

    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
        return this.biomes.generateBiomes(x, z, width, length);
    }

    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
        int i = centerX - sideLength >> 2;
        int j = centerZ - sideLength >> 2;
        int k = centerX + sideLength >> 2;
        int l = centerZ + sideLength >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Set<Biome> set = Sets.newHashSet();
        Collections.addAll(set, this.zoomed.generateBiomes(i, j, i1, j1));
        return set;
    }

    @Nullable
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Biome[] abiome = this.biomes.generateBiomes(i, j, i1, j1);
        BlockPos blockpos = null;
        int k1 = 0;

        for(int l1 = 0; l1 < i1 * j1; ++l1) {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            if (biomes.contains(abiome[l1])) {
                if (blockpos == null || random.nextInt(k1 + 1) == 0) {
                    blockpos = new BlockPos(i2, 0, j2);
                }

                ++k1;
            }
        }

        return blockpos;
    }

    @Override
    public boolean hasStructure(Structure<?> structure) {
        return false;
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        return Sets.newHashSet();
    }
}
