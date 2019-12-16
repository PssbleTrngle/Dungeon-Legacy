package possibletriangle.dungeon.common.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.room.Room;

import java.util.Collections;
import java.util.Random;

public class DungeonChunkGenerator extends ChunkGenerator<DungeonSettings> {

    public DungeonChunkGenerator(World world, BiomeProvider biomes, DungeonSettings settings) {
        super(world, biomes, settings);
    }

    @Override
    public void generateSurface(IChunk chunk) {

    }

    @Override
    public int getGroundHeight() {
        return 0;
    }

    @Override
    public void makeBase(IWorld world, IChunk chunk) {
        Random random = new Random(world.getSeed());
        Room room = Room.REGISTRY.getValue(new ResourceLocation(DungeonMod.MODID, "dev"));
        room.generate(new DungeonChunk(chunk), 1);
    }

    @Override
    public int func_222529_a(int i, int i1, Heightmap.Type type) {
        return 0;
    }
}
