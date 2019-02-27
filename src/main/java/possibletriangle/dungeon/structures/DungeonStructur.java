package possibletriangle.dungeon.structures;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.WorldDataRooms;
import possibletriangle.dungeon.generator.rooms.RoomData;
import possibletriangle.dungeon.loot.LootManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonStructur {

    private static final ArrayList<DungeonStructur> LIST = new ArrayList<>();

    private final List<Template.BlockInfo> blocks = Lists.newArrayList();
    private final List<NBTTagCompound> entities = Lists.newArrayList();
    private final String source;

    public DungeonStructur(String source) {
        this.source = source;
        reload();
        LIST.add(this);
    }

    public void reload() {
        StructureLoader.read(new ResourceLocation(source), blocks, entities);
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

        BlockPos last = last();
        double[] rotationCenter = new double[]{last.getX() / 2.0, last.getZ() / 2.0};
        generate(primer, options, floor, test, rotation, mirror, offset, rotationCenter);

    }

    public void generate(ChunkPrimerDungeon primer, DungeonOptions options, int floor, GenTest test, Rotation rotation, boolean mirror, BlockPos offset, double[] rotationCenter) {
        for(Template.BlockInfo info : blocks()) {

            //info = StructureLoader.rotate(info, rotation, rotationCenter);

            int x = info.pos.getX() + offset.getX();
            int y = info.pos.getY() + offset.getY();
            int z = info.pos.getZ() + offset.getZ();

            if(test == null ||test.at(x, y, z))
                primer.setBlockState(x, y, z, floor, rotation, info.blockState);

        }
    }

    public void populate(World world, int chunkX, int chunkZ, DungeonOptions options, int floor, Rotation rotation, boolean mirror, BlockPos offset, Random random) {

        RoomData data = WorldDataRooms.atFloor(chunkX, floor, chunkZ, world);
        if(data == null || data.pallete == null)
            return;

        for(Template.BlockInfo info : blocks()) {

            NBTTagCompound nbt = info.tileentityData;

            BlockPos last = last();
            double[] center = new double[]{last.getX() / 2.0, last.getZ() / 2.0};
            info = StructureLoader.rotate(info, rotation, center);

            int x = info.pos.getX() + offset.getX();
            int y = info.pos.getY() + offset.getY();
            int z = info.pos.getZ() + offset.getZ();

            BlockPos blockpos = new BlockPos(x + chunkX*16, y + floor* DungeonOptions.FLOOR_HEIGHT, z + chunkZ*16);
            TileEntity te = world.getTileEntity(blockpos);
            if (te != null) {

                nbt.setInteger("x", blockpos.getX());
                nbt.setInteger("y", blockpos.getY());
                nbt.setInteger("z", blockpos.getZ());
                te.readFromNBT(nbt);
                te.rotate(rotation);

                if(te instanceof IInventory) {
                    IInventory inventory = (IInventory) te;
                    ItemStack first = inventory.getStackInSlot(0);
                    String table = null;
                    if(!first.isEmpty() && first.getTagCompound() != null)
                        table = first.getTagCompound().getString("loottable");

                    LootTable t = LootManager.get(table);

                    if((t != null || inventory.isEmpty()) && inventory.getSizeInventory() / 9 > 1) {
                        inventory.clear();
                        if(t == null)
                            t = LootManager.COMMON;
                        t.fillInventory(inventory, random, new LootContext(0, null, null, null, null, null));
                    }
                }

                if(te instanceof TileEntityMobSpawner) {

                    TileEntityMobSpawner spawner = (TileEntityMobSpawner) te;
                    ResourceLocation mob = data.pallete.mob(random);
                    if(mob != null)
                        spawner.getSpawnerBaseLogic().setEntityId(mob);
                }


            }
        }

        for(NBTTagCompound tag : entities) {

            Vec3d pos = new Vec3d(chunkX*16, floor * DungeonOptions.FLOOR_HEIGHT, chunkZ*16);
            Entity entity = AnvilChunkLoader.readWorldEntityPos(tag, world, pos.x, pos.y, pos.z, true);

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
