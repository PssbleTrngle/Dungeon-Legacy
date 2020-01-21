package possibletriangle.dungeon.common.world.room;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.structure.StructureMetadata;

import java.util.Random;

public abstract class Generateable {

    public abstract StructureMetadata getMeta();

    public abstract void generate(DungeonChunk chunk, Random random, GenerationContext context, BlockPos at);

    /**
     * X and Z are the amount of chunks, Y is the amount of floors
     * @return The amount of space required
     */
    public final Vec3i getSize() {
        Vec3i actual = getActualSize();
        int height = (actual.getY() - DungeonSettings.FLOOR_HEIGHT) / (DungeonSettings.FLOOR_HEIGHT + 1) + 1;
        return new Vec3i(actual.getX() / 16, Math.max(1, height), actual.getZ() / 16);
    }

    /**
     * The actual size of the structure in blocks
     * @return The amount of space required
     */
    public Vec3i getActualSize() {
        return new Vec3i(16, DungeonSettings.FLOOR_HEIGHT, 16);
    }

    /**
     * Generate the floor of a room
     * @param chunk the chunk to generate in
     */
    protected final void generateFloor(DungeonChunk chunk, DungeonSettings settings) {

        Vec3i size = getSize();
        BlockState state = TemplateBlock.FLOOR.getDefaultState();

        for (int x = 0; x < size.getX() * 16; x++)
            for (int z = 0; z < size.getZ() * 16; z++)
                for (int y = 0; y < 3; y++)
                    chunk.setBlockState(new BlockPos(x, y, z), state);

    }

}
