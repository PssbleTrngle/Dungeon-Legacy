package possibletriangle.dungeon.common.world;

import net.minecraft.util.math.ChunkPos;

public class GenerationContext {

    private int floor;
    public final DungeonSettings settings;
    public final ChunkPos pos;
    public final Palette palette;

    public GenerationContext(int floor, DungeonSettings settings, ChunkPos pos, Palette palette) {
        this.floor = floor;
        this.settings = settings;
        this.pos = pos;
        this.palette = palette;
    }

    GenerationContext(DungeonSettings settings, ChunkPos pos, Palette palette) {
        this(0, settings, pos);
    }

    public int getFloor() {
        return this.floor;
    }

    setFloor(int floor) {
        this.floor = floor;
    }

}
