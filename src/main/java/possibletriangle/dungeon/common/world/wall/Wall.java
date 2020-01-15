package possibletriangle.dungeon.common.world.wall;

import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.Structures;

import java.util.Random;

public class Wall {

    private static final AxisAlignedBB[] DOORS = {
            new AxisAlignedBB(new BlockPos(0, 0, 3), new BlockPos(1, 3, 5)),
            new AxisAlignedBB(new BlockPos(0, 0, 7), new BlockPos(1, 3, 10)),
            new AxisAlignedBB(new BlockPos(0, 0, 12), new BlockPos(1, 3, 14))
    };

    public static void generate(DungeonChunk chunk, int floors, Random random, DungeonSettings settings) {

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                if (x * z == 0)
                    for (int y = 0; y < settings.floorHeight * floors; y++)
                        chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.WALL.getDefaultState());

        for (AxisAlignedBB door : DOORS)
            for (Rotation rot : new Rotation[]{ Rotation.NONE, Rotation.CLOCKWISE_90 }) {

                if (random.nextFloat() > 0.6F) {
                    int maxY = (int) door.maxY + random.nextInt(2);
                    for (int x = (int) door.minX; x < door.maxX; x++)
                        for (int y = (int) door.minY; y < maxY; y++)
                            for (int z = (int) door.minZ; z < door.maxZ; z++)
                                chunk.setBlockState(new BlockPos(x, y + 3, z), Blocks.AIR.getDefaultState(), rot);
                }
            }

    }
}
