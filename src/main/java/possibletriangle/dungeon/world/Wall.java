package possibletriangle.dungeon.world;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.block.placeholder.TemplateBlock;
import possibletriangle.dungeon.world.generator.DungeonChunk;
import possibletriangle.dungeon.world.generator.DungeonSettings;
import possibletriangle.dungeon.world.generator.GenerationContext;
import possibletriangle.dungeon.world.structure.IStructure;
import possibletriangle.dungeon.world.structure.StructureType;
import possibletriangle.dungeon.world.structure.Structures;

import java.util.Random;

public class Wall {

    /**
     * All the positions of the doors on one wall
     */
    private static final BlockPos[] DOORS = {
            new BlockPos(0, 2, 2),
            new BlockPos(0, 2, 6),
            new BlockPos(0, 2, 11)
    };

    /**
     */
    public static void generate(DungeonChunk chunk, int floors, Random random, GenerationContext context) {

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                if (x * z == 0)
                    for (int y = 0; y < (DungeonSettings.FLOOR_HEIGHT + 1) * floors - 1; y++)
                        chunk.setBlockState(new BlockPos(x, y, z), TemplateBlock.WALL.getDefaultState());

        for(int floor = 0; floor < floors; floor++)
            for (int i = 0; i < DOORS.length; i++)
                for (Rotation rot : new Rotation[]{ Rotation.NONE, Rotation.CLOCKWISE_90 }) {
                    DungeonChunk rotated = chunk.with(rot);

                    if (random.nextFloat() > 0.6F) {

                        StructureType type = i == 1 ? StructureType.BIG_DOOR : StructureType.SMALL_DOOR;
                        IStructure door = Structures.random(type, random);

                        door.generate(rotated, random, context, DOORS[i]);
                    }
                }

    }
}
