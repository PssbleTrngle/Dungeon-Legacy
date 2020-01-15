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

    /**
     * Generate the floor of a room
     * @param chunk the chunk to generate in
     */
    default void generateFloor(DungeonChunk chunk) {

        Vec3i size = getSize();

        for (int x = 0; x < size.getX() * 16; x++)
            for (int z = 0; z < size.getZ() * 16; z++)
                for (int y = 0; y < 3; y++)
                    chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.FLOOR.getDefaultState());

    }

}
