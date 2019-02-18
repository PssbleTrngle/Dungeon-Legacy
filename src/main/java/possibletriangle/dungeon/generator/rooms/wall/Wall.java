package possibletriangle.dungeon.generator.rooms.wall;

import net.minecraft.world.World;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.RandomCollection;

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

    public abstract void populate(DungeonOptions options, World world, int chunkX, int ChunkZ, int floor, Random r);
}
