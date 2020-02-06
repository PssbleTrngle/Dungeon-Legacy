package possibletriangle.dungeon.common.world;

import net.minecraft.util.math.ChunkPos;
import possibletriangle.dungeon.common.block.Palette;

public class GenerationContext {

    private int floor;
    private int size;
    public final DungeonSettings settings;
    public final ChunkPos pos;
    public final Palette palette;

    private GenerationContext(int floor, DungeonSettings settings, ChunkPos pos, Palette palette, int size) {
        this.floor = floor;
        this.settings = settings;
        this.pos = pos;
        this.palette = palette;
        this.size = size;
    }

    GenerationContext(DungeonSettings settings, ChunkPos pos, Palette palette) {
        this(0, settings, pos, palette, 1);
    }

    public int getFloor() {
        return this.floor;
    }

    void setFloor(int floor) {
        this.floor = floor;
    }

    public int getSize() {
        return this.size;
    }

    void setSize(Generateable structure) {
        BlockPos size = structure.getSize();
        this.size = Math.max(size.getX(), size.getZ());
    }

}
