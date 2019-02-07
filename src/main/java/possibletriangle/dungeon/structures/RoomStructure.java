package possibletriangle.dungeon.structures;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.template.Template;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.rooms.Room;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RoomStructure extends Room {

    @Override
    public boolean generateWall() {
        return genWall;
    }

    public static final HashMap<String, RoomStructure> MAP = new HashMap<>();

    private boolean genWall, noCeil;

    public final DungeonStructur structure;

    public RoomStructure genWall() {
        genWall = true;
        return this;
    }

    public RoomStructure noCeil() {
        noCeil = true;
        return this;
    }

    public RoomStructure(String category, String id) {
        super(id);

        String source = category + "/" + id;
        structure = new DungeonStructur(source);

        MAP.put(source, this);
    }

    @Override
    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r) {
        structure.generate(primer, options, floor, (x,y,z) -> (x < 16 && z < 16 && y < options.floorHeight && x >= 0 && z >= 0 && y >= 0) );
    }

    @Override
    public boolean noCeiling() {
        return noCeil;
    }
}
