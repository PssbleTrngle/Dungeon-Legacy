package possibletriangle.dungeon.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DungeonBiomeProvider extends OverworldBiomeProvider {

    public DungeonBiomeProvider(World world) {
        super(new OverworldBiomeProviderSettings().setWorldInfo(world.getWorldInfo()).setGeneratorSettings(new OverworldGenSettings()));
    }

}
