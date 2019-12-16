package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.Template;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;

public class RoomDev extends Room {

    @Override
    public void generate(DungeonChunk chunk, int floor) {
        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++) {

                chunk.setBlockState(new BlockPos(x, 0, z), TemplateBlock.FLOOR.getDefaultState());

            }
    }
}
