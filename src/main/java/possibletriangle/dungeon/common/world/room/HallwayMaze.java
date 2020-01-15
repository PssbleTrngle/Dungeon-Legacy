package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.structure.StructureMetadata;

import java.util.Random;

public class HallwayMaze implements Generateable {

    @Override
    public StructureMetadata getMeta() {
        return new StructureMetadata(1);
    }

    @Override
    public void generate(DungeonChunk chunk, Random random, GenerationContext context) {

        this.generateFloor(chunk);
        BlockState wall = TemplateBlock.WALL.getDefaultState();


        for (int x = 3; x < 14; x++)
            for (int z = 3; z < 14; z++) {

                boolean event = (x+1) % 2 == 0 && (z+1) % 2 == 0;
                boolean odd = ((x+1) % 2) * ((z+1) % 2) == 0);
                boolean close = even || (odd && random.nextInt(4) == 0);

                for (int y = 3; y < context.settings.floorHeight; y++)
                    if (close) chunk.setBlockState(new BlockPos(x, y, z), wall);

            }
    }
}
