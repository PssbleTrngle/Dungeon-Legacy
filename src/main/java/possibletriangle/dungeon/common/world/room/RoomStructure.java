package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import possibletriangle.dungeon.DungeonMod;
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
        if(this.template == null) {
            DungeonMod.LOGGER.error("File for room '{}' could not be found", source.toString());
        }
        setRegistryName(source);
    }

    @Override
    public Vec3i getSize(DungeonSettings options) {
        BlockPos templateSize = template.getSize();
        return new Vec3i(1, Math.max(1, templateSize.getY() / options.floorHeight), 1);
    }

    @Override
    public void generate(DungeonChunk chunk, int floor, Random random, DungeonSettings settings) {
        template.generate(chunk, new BlockPos(1, 0, 1));
    }

}
