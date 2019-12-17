package possibletriangle.dungeon.common.world.wall;

import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.Room;

import java.util.Random;

public class Wall extends Room {

    public Wall() {
        super(Type.WALL);
    }

    private static final AxisAlignedBB[] DOORS = {
            new AxisAlignedBB(new BlockPos(0, 4, 3), new BlockPos(0, 7, 5)),
            new AxisAlignedBB(new BlockPos(0, 4, 7), new BlockPos(0, 7, 9)),
            new AxisAlignedBB(new BlockPos(0, 4, 10), new BlockPos(0, 7, 12))
    };

    @Override
    public void generate(DungeonChunk chunk, int floor, Random random, DungeonSettings settings) {

        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++)
                if(x * z == 0)
                    for(int y = 0; y < settings.floorHeight; y++) {

                        chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.WALL.getDefaultState());

                }

        for(AxisAlignedBB door : DOORS) for(Rotation rot : new Rotation[]{ Rotation.NONE, Rotation.CLOCKWISE_90 }) {

            boolean open = random.nextFloat() > 0.6F;

            if(open)
                for(int x = (int) door.minX; x < door.maxX; x++)
                    for(int y = (int) door.minY; y < door.maxY; y++)
                        for(int z = (int) door.minZ; z < door.maxZ; z++)
                            chunk.setBlockState(new BlockPos(x, y, z), Blocks.SPONGE.getDefaultState(), rot);
        }

    }
}
