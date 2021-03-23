package possibletriangle.dungeon.world.structure;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import possibletriangle.dungeon.block.placeholder.TemplateBlock;
import possibletriangle.dungeon.block.placeholder.TemplateType;
import possibletriangle.dungeon.world.generator.DungeonChunk;
import possibletriangle.dungeon.world.generator.DungeonSettings;
import possibletriangle.dungeon.world.generator.GenerationContext;
import possibletriangle.dungeon.world.structure.metadata.StructureMetadata;

import java.util.Random;

public abstract class IStructure {

    public abstract StructureMetadata getMeta();

    public abstract void generate(DungeonChunk chunk, Random random, GenerationContext context, BlockPos at);

    public static BlockPos roomSizeFromActual(BlockPos actual) {
        int height = (actual.getY() - DungeonSettings.FLOOR_HEIGHT) / (DungeonSettings.FLOOR_HEIGHT + 1) + 1;
        int x = (actual.getZ() - 15) / 16 + 1;
        int z = (actual.getZ() - 15) / 16 + 1;
        return new BlockPos(x, Math.max(1, height), z);
    }

    /**
     * X and Z are the amount of chunks, Y is the amount of floors
     * @return The amount of space required
     */
    public final BlockPos getSize() {
        return roomSizeFromActual(getActualSize());
    }

    /**
     * The actual size of the structure in blocks
     * @return The amount of space required
     */
    public BlockPos getActualSize() {
        return new BlockPos(15, DungeonSettings.FLOOR_HEIGHT, 15);
    }

    /**
     * Generate the floor of a room
     * @param chunk the chunk to generate in
     */
    protected final void generateFloor(DungeonChunk chunk, DungeonSettings settings) {

        Vector3i size = getSize();
        BlockState state = TemplateType.FLOOR.getBlock().getDefaultState();

        for (int x = 0; x < size.getX() * 16; x++)
            for (int z = 0; z < size.getZ() * 16; z++)
                for (int y = 0; y < 3; y++)
                    chunk.setBlockState(new BlockPos(x, y, z), state);

    }

}
