package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.Vec3i;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.structure.StructureMetadata;

import java.util.Random;

public interface Generateable {

    StructureMetadata getMeta();

    void generate(DungeonChunk chunk, Random random, GenerationContext context);

    /**
     * X and Z are the amount of chunks, Y is the amount of floors
     * @return The amount of space required
     */
    default Vec3i getSize(DungeonSettings options) {
        return new Vec3i(1, 1, 1);
    }
}
