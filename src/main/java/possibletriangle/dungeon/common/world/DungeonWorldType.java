package possibletriangle.dungeon.common.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;

public class DungeonWorldType extends WorldType {

    public DungeonWorldType() {
        super("dungeon");
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator(World world) {
        return new DungeonChunkGenerator(world, new DungeonBiomeProvider(world), new DungeonSettings());
    }
}
