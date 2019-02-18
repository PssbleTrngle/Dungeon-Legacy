package possibletriangle.dungeon.generator;

import com.google.common.collect.Lists;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import possibletriangle.dungeon.pallete.Pallete;

import java.util.List;
import java.util.Random;

public class BiomeProviderDungeon extends BiomeProvider {

    private final World world;
    private final Random r;

    public BiomeProviderDungeon(World world) {
        this.world = world;
        this.r = new Random(world.getSeed());
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return Pallete.random(r);
    }
}

