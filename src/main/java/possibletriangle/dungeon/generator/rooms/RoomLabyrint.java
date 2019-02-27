package possibletriangle.dungeon.generator.rooms;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import possibletriangle.dungeon.block.ModBlocks;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.structures.DungeonStructur;

import java.util.ArrayList;
import java.util.Random;

public class RoomLabyrint extends Room {

    public RoomLabyrint() {
        super("labyrinth");
    }

    private final static DungeonStructur FULL = new DungeonStructur("room/hall");

    @Override
    public boolean generateWall() {
        return true;
    }

    @Override
    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r, Rotation rotation) {

        ArrayList<BlockPos[]> p = new ArrayList<>();
        p.add(new BlockPos[]{new BlockPos(2, 0, 1)});
        p.add(new BlockPos[]{new BlockPos(5, 0, 1), new BlockPos(6, 0, 1)});
        p.add(new BlockPos[]{new BlockPos(9, 0, 1), new BlockPos(10, 0, 1)});
        p.add(new BlockPos[]{new BlockPos(13, 0, 1)});
        p.add(new BlockPos[]{new BlockPos(2, 0, 13)});
        p.add(new BlockPos[]{new BlockPos(5, 0, 13), new BlockPos(6, 0, 13)});
        p.add(new BlockPos[]{new BlockPos(9, 0, 13), new BlockPos(10, 0, 13)});
        p.add(new BlockPos[]{new BlockPos(13, 0, 13)});

        FULL.generate(primer, options, floor, (x,y,z) -> (x == 0 || x == 15) || (z == 0 || z == 15), Rotation.NONE);

        for(BlockPos[] p2 : p) {

            if(r.nextBoolean())
                for(BlockPos p3 : p2)
                    for(int y = 3; y < DungeonOptions.FLOOR_HEIGHT -2; y++)
                        primer.setBlockState(p3.getX(), y, p3.getZ(), floor, Rotation.NONE, ModBlocks.WALL.getDefaultState());
                    else
                        for(BlockPos p3 : p2)
                            for(int y = 3; y < DungeonOptions.FLOOR_HEIGHT -2; y++)
                                primer.setBlockState(p3.getZ(), y, p3.getX(), floor, Rotation.NONE, ModBlocks.WALL.getDefaultState());

        }

        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++) {

                    for(int y = 0; y < 3; y++)
                        primer.setBlockState(x, y, z, floor, rotation, ModBlocks.FLOOR.getDefaultState());
                    if(x > 1 && x < 14 && z > 1 && z < 14) {

                        if(x % 3 != 0 && z % 3 != 0) {

                            for(int y = 3; y < DungeonOptions.FLOOR_HEIGHT -2; y++)
                                primer.setBlockState(x, y, z, floor, rotation, ModBlocks.WALL.getDefaultState());

                        } else if((x % 3 == 0 || z % 3 == 0) && (x % 3 != 0 || z % 3 != 0))
                            if(r.nextDouble() < 0.25) {

                                for(int y = 3; y < DungeonOptions.FLOOR_HEIGHT -2; y++)
                                    primer.setBlockState(x, y, z, floor, rotation, ModBlocks.WALL.getDefaultState());

                            }

                    }

                }

    }

    @Override
    public boolean noCeiling() {
        return false;
    }

    @Override
    public void populate(DungeonOptions options, World world, int chunkX, int chunkZ, int floor, Random r) {

    }
}
