package possibletriangle.dungeon.generator;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import possibletriangle.dungeon.Dungeon;

import java.util.HashMap;

public class WorldDataRooms extends WorldSavedData {

    private final HashMap<BlockPos, ResourceLocation> MAP = new HashMap<>();
    public static final String NAME = Dungeon.MODID + ":room_data";

    public WorldDataRooms() {
        super(NAME);
    }

    public WorldDataRooms(String name) {
        super(name);
    }

    public static WorldDataRooms get(World world) {
        MapStorage storage = world.getMapStorage() == null ? world.getPerWorldStorage() : world.getMapStorage();
        WorldDataRooms instance = (WorldDataRooms) storage.getOrLoadData(WorldDataRooms.class, NAME);

        if (instance == null) {
            instance = new WorldDataRooms();
            storage.setData(NAME, instance);
        }
        return instance;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        MAP.clear();
        NBTTagList list = compound.getTagList(NAME, 0);
        for(NBTBase base : list) {
            if(base instanceof NBTTagCompound) {

                NBTTagCompound b = (NBTTagCompound) base;
                BlockPos pos = new BlockPos(b.getInteger("chunkY"), b.getInteger("floor"), b.getInteger("chunkX"));
                String name = b.getString("name");
                MAP.put(pos, new ResourceLocation(name));

            }
        }

        floor_height = compound.getInteger("floor_height");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        NBTTagList list = new NBTTagList();
        for(BlockPos pos : MAP.keySet()) {

            NBTTagCompound b = new NBTTagCompound();
            b.setString("name", MAP.get(pos).toString());
            b.setInteger("chunkX", pos.getX());
            b.setInteger("floor", pos.getY());
            b.setInteger("chunkZ", pos.getZ());

            list.appendTag(b);

        }

        compound.setInteger("floor_height", floor_height);

        return compound;
    }

    private int floor_height;
    public static void setFloorHeight(int floor_height, World world) {
        get(world).floor_height = floor_height;
    }
    public static int getFloorHeight(World world) {
       return get(world).floor_height;
    }

    public static void put(int chunkX, int floor, int chunkZ, ResourceLocation name, World world) {
        get(world).MAP.put(new BlockPos(chunkX, floor, chunkZ), name);
        get(world).markDirty();
    }

    public static int floor(int y, World world) {

        WorldDataRooms DATA = get(world);

        if (DATA.floor_height > 0) {
            return y / DATA.floor_height;
        }

        return -1;
    }

    public static ResourceLocation atY(int chunkX, int y, int chunkZ, World world) {

        WorldDataRooms DATA = get(world);
        int floor = floor(y, world);

        return atFloor(chunkX, floor, chunkZ, world);

    }

    public static ResourceLocation atFloor(int chunkX, int floor, int chunkZ, World world) {

        WorldDataRooms DATA = get(world);

        if(floor > 0) {
            return DATA.MAP.get(new BlockPos(chunkX, floor, chunkZ));
        }

        return null;

    }

    public static BlockPos toChunk(BlockPos playerPos, World world) {

        int floor = floor(playerPos.getY(), world);
        BlockPos chunk = new BlockPos(playerPos.getX() / 16 + 1, floor, playerPos.getZ() / 16 + 1);
        return chunk;

    }

}

