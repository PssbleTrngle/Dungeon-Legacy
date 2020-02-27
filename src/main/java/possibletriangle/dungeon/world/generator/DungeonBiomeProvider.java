package possibletriangle.dungeon.world.generator;

import net.minecraft.world.World;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.biome.provider.OverworldBiomeProviderSettings;
import net.minecraft.world.gen.OverworldGenSettings;

public class DungeonBiomeProvider extends OverworldBiomeProvider {

    public DungeonBiomeProvider(World world) {
        super(new OverworldBiomeProviderSettings()
                .setWorldInfo(world.getWorldInfo())
                .setGeneratorSettings(new OverworldGenSettings()));
    }

}
