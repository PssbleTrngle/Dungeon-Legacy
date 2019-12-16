package possibletriangle.dungeon.common;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import possibletriangle.dungeon.common.world.DungeonChunkGenerator;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.DungeonWorldType;

public class CommonProxy {

    public void init() {

        new DungeonWorldType();

    }

}
