package possibletriangle.dungeon.rooms.wall;

import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.template.Template;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.RandomCollection;
import possibletriangle.dungeon.structures.DungeonStructur;
import possibletriangle.dungeon.structures.StructureLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WallRandom extends Wall {

    private final static Wall FULL = new WallStructure("wall/all", 0);
    private final static DungeonStructur CLOSED = new DungeonStructur("wall/door/wall");

    private final RandomCollection<DungeonStructur> doors = new RandomCollection<>();
    private DungeonStructur defaultDoor;

    private final static HashMap<BlockPos, Rotation> at = new HashMap<>();
    {
        at.put(new BlockPos(0, 1, 3), Rotation.NONE);
        at.put(new BlockPos(0, 1, 7), Rotation.NONE);
        at.put(new BlockPos(0, 1, 11), Rotation.NONE);

        at.put(new BlockPos(3, 1, 0), Rotation.CLOCKWISE_90);
        at.put(new BlockPos(7, 1, 0), Rotation.CLOCKWISE_90);
        at.put(new BlockPos(11, 1, 0), Rotation.CLOCKWISE_90);

        at.put(new BlockPos(14, 1, 3), Rotation.CLOCKWISE_180);
        at.put(new BlockPos(14, 1, 7), Rotation.CLOCKWISE_180);
        at.put(new BlockPos(14, 1, 11), Rotation.CLOCKWISE_180);

        at.put(new BlockPos(3, 1, 14), Rotation.COUNTERCLOCKWISE_90);
        at.put(new BlockPos(7, 1, 14), Rotation.COUNTERCLOCKWISE_90);
        at.put(new BlockPos(11, 1, 14), Rotation.COUNTERCLOCKWISE_90);
    }

    public WallRandom(double weight) {
        super(weight);
    }

    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r) {

        FULL.generateAt(options, primer, floor, r);

        boolean last = false;
        for(BlockPos pos : at.keySet()) {

            if(false && at.get(pos) == Rotation.NONE ||at.get(pos) == Rotation.CLOCKWISE_90) {

                defaultDoor.generate(primer, options, floor, null, at.get(pos), false, pos);

            } else {

                boolean open = !last && r.nextDouble() > 0.4;
                last = open;

                DungeonStructur door = open ? doors.next(r) : CLOSED;
                door.generate(primer, options, floor, null, at.get(pos), false, pos);

            }
        }

    }

    public WallRandom add(DungeonStructur door, double weight) {
        if(defaultDoor == null) defaultDoor = door;
        doors.add(weight, door);
        return this;
    }

}
