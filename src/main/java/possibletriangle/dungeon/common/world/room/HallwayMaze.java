package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.GenerationContext;

import java.util.Random;

public class HallwayMaze extends Room {

    @Override
    public void generate(DungeonChunk chunk, Random random, GenerationContext context) {
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = 0; y < 3; y++)
                    chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.FLOOR.getDefaultState());

        for (int x = 3; x < 14; x++)
            for (int z = 3; z < 14; z++) {
                boolean b = random.nextInt(4) == 0;
                for (int y = 3; y < context.settings.floorHeight; y++) {
                    if ((x+1) % 2 == 0 && (z+1) % 2 == 0)
                        chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.WALL.getDefaultState());
                    else if (b && ((x+1) % 2) * ((z+1) % 2) == 0)
                        chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.WALL.getDefaultState());
                }
            }
    }
}
