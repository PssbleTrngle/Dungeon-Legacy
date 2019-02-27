package possibletriangle.dungeon.generator.rooms.wall;

import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Random;

public abstract class Wall {

    public static final RandomCollection<Wall> LIST = new RandomCollection<>();

    public static Wall random(Random random) {
        return LIST.next(random);
    }

    public Wall(double weight) {
        if(weight > 0)
        LIST.add(weight, this);
    }

    public abstract void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r, int chunkX, int chunkZ);
}
