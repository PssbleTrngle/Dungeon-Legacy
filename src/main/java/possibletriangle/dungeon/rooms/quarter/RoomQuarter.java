package possibletriangle.dungeon.rooms.quarter;

import net.minecraft.world.chunk.ChunkPrimer;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;

public abstract class RoomQuarter {

    public abstract void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, int[] offset);

}
