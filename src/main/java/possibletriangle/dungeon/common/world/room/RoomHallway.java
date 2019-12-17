package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;

import java.util.Random;

public class RoomHallway extends Room {

    public RoomHallway() {
        super(Type.HALLWAY);
    }

    @Override
    public void generate(DungeonChunk chunk, int floor, Random random, DungeonSettings settings) {
        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++)
                for(int y = 0; y < 3; y++){

                    chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.FLOOR.getDefaultState());

                }
    }
}
