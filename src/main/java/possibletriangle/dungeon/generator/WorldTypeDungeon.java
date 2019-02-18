package possibletriangle.dungeon.generator;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

public class WorldTypeDungeon extends WorldType {

    public static final String NAME = "Dungeon";

    public WorldTypeDungeon() {
        super(NAME);
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
        return new ChunkGeneratorDungeon(new DungeonOptions(), world, new Random(world.getSeed()));
    }

    /*
    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return new BiomeProviderDungeon(world);
    }
    */

}
