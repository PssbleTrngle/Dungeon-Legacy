package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.structure.DungeonStructure;
import possibletriangle.dungeon.common.world.structure.StructureLoader;

import java.util.Random;

public class RoomStructure extends Room {

    private final DungeonStructure template;

    public RoomStructure(Type type, ResourceLocation source) {
        super(type);
        this.template = StructureLoader.read(source);
        setRegistryName(source);
    }

    @Override
    public void generate(DungeonChunk chunk, int floor, Random random, DungeonSettings settings) {
        template.generate(chunk, new BlockPos(1, 0, 1));
    }

}
