package possibletriangle.dungeon.structures;

import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.structure.template.Template;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.ChunkPrimerRotateable;
import possibletriangle.dungeon.generator.DungeonOptions;

import java.util.ArrayList;
import java.util.List;

public class DungeonStructur {

    private static final ArrayList<DungeonStructur> LIST = new ArrayList<>();

    private final List<Template.BlockInfo> blocks = Lists.<Template.BlockInfo>newArrayList();
    private final String source;

    public DungeonStructur(String source) {
        this.source = source;
        reload();
        LIST.add(this);
    }

    public void reload() {
        StructureLoader.read(new ResourceLocation(source), blocks);
    }

    public static void reloadAll() {
        int i = 0;
        for(DungeonStructur structure : LIST) {
            structure.reload();
            i++;
        }
        Dungeon.LOGGER.info("Reloaded {} structures", i);
    }

    public Template.BlockInfo[] blocks() {
        return blocks.toArray(new Template.BlockInfo[0]);
    }

    public void generate(ChunkPrimerDungeon primer, DungeonOptions options, int floor, GenTest test, Rotation rotation) {
        generate(primer, options, floor, test, rotation, false, new BlockPos(0, 0, 0));
    }

    public void generate(ChunkPrimerDungeon primer, DungeonOptions options, int floor, GenTest test, Rotation rotation, boolean mirror, BlockPos offset) {
        for(Template.BlockInfo info : blocks()) {

            BlockPos last = last();
            double[] center = new double[]{last.getX() / 2.0, last.getZ() / 2.0};
            info = StructureLoader.rotate(info, rotation, center);

            int x = info.pos.getX() + offset.getX();
            int y = info.pos.getY() + offset.getY();
            int z = info.pos.getZ() + offset.getZ();

            if(test == null ||test.at(x, y, z))
                primer.setBlockState(x, y, z, floor, rotation, info.blockState);

        }
    }

    public void populate(World world, int chunkX, int chunkZ, DungeonOptions options, int floor, Rotation rotation, boolean mirror, BlockPos offset) {

        for(Template.BlockInfo info : blocks()) {

            if(info.tileentityData != null) {

                BlockPos last = last();
                double[] center = new double[]{last.getX() / 2.0, last.getZ() / 2.0};
                info = StructureLoader.rotate(info, rotation, center);

                int x = info.pos.getX() + offset.getX();
                int y = info.pos.getY() + offset.getY();
                int z = info.pos.getZ() + offset.getZ();

                BlockPos blockpos = new BlockPos(x + chunkX*16, y + floor*options.floorHeight, z + chunkZ*16);
                TileEntity te = world.getTileEntity(blockpos);
                if (te != null) {
                    info.tileentityData.setInteger("x", blockpos.getX());
                    info.tileentityData.setInteger("y", blockpos.getY());
                    info.tileentityData.setInteger("z", blockpos.getZ());
                    te.readFromNBT(info.tileentityData);
                    te.rotate(rotation);
                }

            }
        }

    }

    public interface GenTest {
        boolean at(int x, int y, int z);
    }

    public BlockPos last() {

        int x = 0, y = 0, z = 0;

        for(Template.BlockInfo info : blocks) {
            x = Math.max(x, info.pos.getX());
            y = Math.max(y, info.pos.getY());
            z = Math.max(z, info.pos.getZ());
        }

        return new BlockPos(x, y, z);

    }

}
