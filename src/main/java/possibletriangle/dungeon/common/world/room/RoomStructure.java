package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.structure.DungeonStructure;

import java.util.Random;

public class RoomStructure extends Room {

    private final DungeonStructure structure;

    public RoomStructure(DungeonStructure structure) {
        this.structure = structure;
    }

    @Override
    public Vec3i getSize(DungeonSettings options) {
        BlockPos templateSize = structure.getSize();
        return new Vec3i(1, Math.max(1, templateSize.getY() / options.floorHeight), 1);
    }

    @Override
    public void generate(DungeonChunk chunk, Random random, GenerationContext context) {
        structure.generate(chunk, new BlockPos(1, 0, 1));
    }

}
