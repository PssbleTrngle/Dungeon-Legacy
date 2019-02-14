package possibletriangle.dungeon.generator.rooms.wall;

import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.RandomCollection;
import possibletriangle.dungeon.structures.DungeonStructur;

import java.util.ArrayList;
import java.util.Random;

public class WallRandom extends Wall {

    private final static DungeonStructur CLOSED = new DungeonStructur("wall/door/closed");
    private final static DungeonStructur OPEN = new DungeonStructur("wall/door/rect_open");

    private final RandomCollection<DungeonStructur> doors = new RandomCollection<>();
    private DungeonStructur defaultDoor;

    private final static ArrayList<BlockPos> at = new ArrayList<>();

    static void pos() {
        at.clear();
        at.add(new BlockPos(0, 1, 2));
        at.add(new BlockPos(0, 1, 6));
        at.add(new BlockPos(0, 1, 10));
    }

    public WallRandom(double weight) {
        super(weight);
    }

    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r, int chunkX, int chunkZ) {
        pos();

        for(BlockPos pos : at) for(Rotation rot : Rotation.values()) {

                boolean closed = r.nextDouble() > 0.6;
                boolean open = chunkX % 2 == chunkZ % 2;

                BlockPos check = pos.add(0, floor*options.floorHeight + 2, 1);
                if (primer.getBlockStateWithRotation(check.getX(), check.getY(), check.getZ(), rot).getBlock() == Blocks.SPONGE) {
                    DungeonStructur door = closed ? CLOSED : doors.next(r);
                    if(open) door = OPEN;
                    door.generate(primer, options, floor, null, rot, false, pos);
                }

        }

    }

    @Override
    public void populate(DungeonOptions options, World world, int chunkX, int ChunkZ, int floor, Random r) {

    }

    public WallRandom add(DungeonStructur door, double weight) {
        if(defaultDoor == null) defaultDoor = door;
        doors.add(weight, door);
        return this;
    }

}
