package possibletriangle.dungeon.world.structure.metadata;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.INBTSerializable;
import possibletriangle.dungeon.world.structure.IStructure;
import possibletriangle.dungeon.world.structure.metadata.condition.CategoryCondition;
import possibletriangle.dungeon.world.structure.metadata.condition.Condition;

import java.util.Random;
import java.util.function.Predicate;

public class Part implements Predicate<IStructure>, INBTSerializable<CompoundNBT> {

    private Condition<String[]> categories;
    private AxisAlignedBB pos;
    private AxisAlignedBB size;

    public AxisAlignedBB getPos() {
        return this.pos;
    }

    public AxisAlignedBB getSize() {
        return this.size;
    }

    public BlockPos randomPos(Random random) {
        int x = (int) (random.nextInt((int) (pos.maxX - pos.minX + 1)) + pos.minX);
        int y = (int) (random.nextInt((int) (pos.maxY - pos.minY + 1)) + pos.minY);
        int z = (int) (random.nextInt((int) (pos.maxZ - pos.minZ + 1)) + pos.minZ);
        return new BlockPos(x, y, z);
    }

    Part(CompoundNBT nbt) {
        this(new CategoryCondition(new String[0], new String[0], new String[0]), null, null);
        this.deserializeNBT(nbt);
    }

    public Part(Condition<String[]> categories, AxisAlignedBB pos, AxisAlignedBB size) {
        this.size = size;
        this.pos = pos;
        this.categories = categories;
    }

    /**
     * Test if a IStructure qualifies as a substructure part of the specified type
     * @param structure The Substructure to test
     */
    public boolean test(IStructure structure) {
        Vec3i size = structure.getActualSize();
        return categories.test(structure.getMeta().getCategories())
            && size.getX() >= this.size.minX && size.getX() <= this.size.maxX
            && size.getY() >= this.size.minY && size.getY() <= this.size.maxY
            && size.getZ() >= this.size.minZ && size.getZ() <= this.size.maxZ;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        putBox(this.size, "size", nbt);
        putBox(this.pos, "pos", nbt);
        nbt.put("categories", this.categories.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.size = getBox(nbt, "size");
        this.pos = getBox(nbt, "pos");
        this.categories.deserializeNBT(nbt);
    }

    public static void putBox(AxisAlignedBB box, String key, CompoundNBT compound) {
        if(box != null) {
            compound.putDouble(key + "MinX", box.minX);
            compound.putDouble(key + "MinY", box.minY);
            compound.putDouble(key + "MinZ", box.minZ);
            compound.putDouble(key + "MaxX", box.maxX);
            compound.putDouble(key + "MaxY", box.maxY);
            compound.putDouble(key + "MaxZ", box.maxZ);
        }
    }

    public static AxisAlignedBB getBox(CompoundNBT compound, String key) {
        int minx = compound.getInt(key + "MinX");
        int miny = compound.getInt(key + "MinY");
        int minz = compound.getInt(key + "MinZ");
        int maxx = compound.getInt(key + "MaxX");
        int maxy = compound.getInt(key + "MaxY");
        int maxz = compound.getInt(key + "MaxZ");
        return new AxisAlignedBB(minx, miny, minz, maxx, maxy, maxz);
    }
}