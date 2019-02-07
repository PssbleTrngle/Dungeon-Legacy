package possibletriangle.dungeon.rooms.wall;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.template.Template;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.RandomCollection;
import possibletriangle.dungeon.structures.StructureLoader;

import java.util.ArrayList;
import java.util.List;
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

    public abstract void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r);
}
