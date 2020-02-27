package possibletriangle.dungeon.world;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.ChunkGenerator;
import possibletriangle.dungeon.world.generator.DungeonChunkGenerator;
import possibletriangle.dungeon.world.generator.DungeonSettings;

public class DungeonWorldType extends WorldType {

    public DungeonWorldType() {
        super("dungeon");
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator(World world) {
        return new DungeonChunkGenerator(world, new DungeonSettings());
    }

}
