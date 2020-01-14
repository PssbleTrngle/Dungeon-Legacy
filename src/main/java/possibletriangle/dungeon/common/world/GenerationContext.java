package possibletriangle.dungeon.common.world;

import net.minecraft.util.math.ChunkPos;

public class GenerationContext {

    public final int floor;
    public final DungeonSettings settings;
    public final ChunkPos pos;

    public GenerationContext(int floor, DungeonSettings settings, ChunkPos pos) {
        this.floor = floor;
        this.settings = settings;
        this.pos = pos;
    }

}
