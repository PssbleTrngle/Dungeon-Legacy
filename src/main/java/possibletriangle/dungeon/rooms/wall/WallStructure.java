package possibletriangle.dungeon.rooms.wall;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.template.Template;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.structures.DungeonStructur;
import possibletriangle.dungeon.structures.StructureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WallStructure extends Wall {

    public final DungeonStructur structure;

    public WallStructure(String source, double weight) {
        super(weight);
        structure = new DungeonStructur(source);
    }

    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r) {
        structure.generate(primer, options, floor, (x,y,z) -> ((x == 0 || x == 15 || z == 0 ||z == 15) && y < options.floorHeight && y >= 0) );
    }
}
