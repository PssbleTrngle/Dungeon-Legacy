package possibletriangle.dungeon.common.world.room;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;

import java.util.Random;

public class RoomMaze extends Room {

    public RoomMaze() {
        super(Type.HALLWAY);
    }

    private static final int SIZE = 15;

    private static BlockPos[] EDGES = {
        new BlockPos(1, 0, 1),
        new BlockPos(1, 0, SIZE),
        new BlockPos(SIZE, 0, 1),
        new BlockPos(SIZE, 0, SIZE)
    }

    private static BlockPos[] MIDS = Arrays.stream(new BlockPos[]{
        new BlockPos(1, 0, 4),
        new BlockPos(1, 0, 6),
        new BlockPos(1, 0, 10),
        new BlockPos(1, 0, 12),
        new BlockPos(SIZE, 0, 4),
        new BlockPos(SIZE, 0, 6),
        new BlockPos(SIZE, 0, 10),
        new BlockPos(SIZE, 0, 12)
    })
    .map(p -> new BlockPos[]{ p, new BlockPos(p.getZ(), p.getY(), p.getX() })
    .flatMap(p -> Stream.of(p))
    .toArray(BlockPos[]::new);

    @Override
    public void generate(DungeonChunk chunk, int floor, Random random, DungeonSettings settings) {

        Collections.shuffle(EDGES).slice(0, 2).forEach(p -> chunk.setBlockState(p, TemplateBlock.WALL.getDefaultState()));
        Collections.shuffle(MIDS).slice(0, 8).forEach(p -> chunk.setBlockState(p, TemplateBlock.WALL.getDefaultState()));

        for(int x = 3; x < SIZE - 1; x++)
            for(int z = 3; z < SIZE - 1; z++)
                for(int y = 3; y < settings.floorHeight; y++)
                    chunk.setBlockState(new BlockPos(x, y, z), at(x, z, random).getDefaultState());

    }

    private Block at(int x, int z, Random random) {
        
        if ((x + 1) % 2 * (z + 1) % 2 != 0)
            return TemplateBlock.WALL;
        else if(random.nextFloat() < 0.7)
            return Blocks.AIR;
        else if(random.nextFloat() < 0.3)
            return TemplateBlock.FLOOR; /* TODO replace with breakable rock */
        else
            return TemplateBlock.WALL;

    }

}
