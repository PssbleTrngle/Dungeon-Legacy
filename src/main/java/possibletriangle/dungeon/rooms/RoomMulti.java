package possibletriangle.dungeon.rooms;

import net.minecraft.world.chunk.ChunkPrimer;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.rooms.quarter.RoomQuarter;

import javax.annotation.Nullable;
import java.util.Random;

public class RoomMulti extends Room {

    public RoomMulti() {
        super("multi");
    }

    @Override
    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r) {

        for(int x = 0; x <= 1; x++)
            for(int z = 0; z <= 1; z++) {

                RoomQuarter quarter = RoomManager.randomQuarter(r);
                quarter.generateAt(options, primer, floor, new int[]{x, z});

            }

    }

    @Override
    public boolean generateWall() {
        return false;
    }

    @Override
    public boolean noCeiling() {
        return false;
    }
}
