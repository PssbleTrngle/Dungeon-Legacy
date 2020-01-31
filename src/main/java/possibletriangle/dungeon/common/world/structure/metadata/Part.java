package possibletriangle.dungeon.common.world.structure.metadata;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.structure.metadata.condition.Condition;

import java.util.Random;
import java.util.function.Predicate;

public class Part implements Predicate<Generateable> {

    private final Condition<String[]> categories;
    private final AxisAlignedBB pos;
    private final AxisAlignedBB size;

    public BlockPos getPos(Random random) {
        int x = (int) (random.nextInt((int) (pos.maxX - pos.minX + 1)) + pos.minX);
        int y = (int) (random.nextInt((int) (pos.maxY - pos.minY + 1)) + pos.minY);
        int z = (int) (random.nextInt((int) (pos.maxZ - pos.minZ + 1)) + pos.minZ);
        return new BlockPos(x, y, z);
    }

    public Part(Condition<String[]> categories, AxisAlignedBB pos, AxisAlignedBB size) {
        this.size = size;
        this.pos = pos;
        this.categories = categories;
    }

    public boolean test(Generateable structure) {
        Vec3i size = structure.getActualSize();
        return categories.test(structure.getMeta().getCategories())
            && size.getX() >= this.size.minX && size.getX() <= this.size.maxX
            && size.getY() >= this.size.minY && size.getY() <= this.size.maxY
            && size.getZ() >= this.size.minZ && size.getZ() <= this.size.maxZ;
    }

}