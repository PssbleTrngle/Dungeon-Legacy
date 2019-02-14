package possibletriangle.dungeon.generator;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.generator.rooms.RoomData;

import java.util.HashMap;

public class WorldDataRooms extends WorldSavedData {

    private final HashMap<BlockPos, RoomData> MAP = new HashMap<>();
    public static final String NAME = Dungeon.MODID + ":room_data";

    public WorldDataRooms() {
        super(NAME);
    }

    public WorldDataRooms(String name) {
        super(name);
    }

    public static WorldDataRooms get(World world) {
        MapStorage storage = world.getMapStorage();
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
        NBTTagList list = compound.getTagList(NAME, 10);
        for(NBTBase base : list) {
            if(base instanceof NBTTagCompound) {

                NBTTagCompound b = (NBTTagCompound) base;
                BlockPos pos = new BlockPos(b.getInteger("chunkX"), b.getInteger("floor"), b.getInteger("chunkZ"));
                String name = b.getString("name");
                Rotation r = Rotation.valueOf(b.getString("rotation"));
                Pallete pallete = Pallete.MAP.get(new ResourceLocation(b.getString("pallete")));
                MAP.put(pos, new RoomData(new ResourceLocation(name), r, pallete));

            }
        }

        floor_height = compound.getInteger("floor_height");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        NBTTagList list = new NBTTagList();
        for(BlockPos pos : MAP.keySet()) {

            NBTTagCompound b = new NBTTagCompound();
            b.setString("name", MAP.get(pos).name.toString());
            b.setInteger("chunkX", pos.getX());
            b.setInteger("floor", pos.getY());
            b.setInteger("chunkZ", pos.getZ());
            b.setString("rotation", MAP.get(pos).rotation.name());
            b.setString("pallete", MAP.get(pos).pallete.getBiomeName());

            list.appendTag(b);

        }

        compound.setTag(NAME, list);
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

    public static void put(int chunkX, int floor, int chunkZ, ResourceLocation name, Rotation r, Pallete pallete, World world, String s) {
        RoomData data = new RoomData(name, r, pallete);
        BlockPos pos = new BlockPos(chunkX, floor, chunkZ);

        if(get(world).MAP.containsKey(pos) && !"side".equals(s)) {

            Dungeon.LOGGER.info("Trying to overwrite existing room: \"{}\" -> \"{}\" [{}]", get(world).MAP.get(pos).name, name, s);

        } else {
            get(world).MAP.put(pos, data);
            get(world).markDirty();
        }
    }

    public static int floor(int y, World world) {

        WorldDataRooms DATA = get(world);

        if (DATA.floor_height > 0) {
            return y / DATA.floor_height;
        }

        return -1;
    }

    public static RoomData atY(int chunkX, int y, int chunkZ, World world) {

        int floor = floor(y, world);
        return atFloor(chunkX, floor, chunkZ, world);

    }

    public static RoomData atFloor(int chunkX, int floor, int chunkZ, World world) {

        WorldDataRooms DATA = get(world);

        if(floor >= 0) {
            return DATA.MAP.get(new BlockPos(chunkX, floor, chunkZ));
        }

        //Dungeon.LOGGER.info("Invalid Room Position: {}", new BlockPos(chunkX, floor, chunkZ));
        return null;

    }

    public static BlockPos toChunk(BlockPos playerPos, World world) {

        int floor = floor(playerPos.getY(), world);
        BlockPos chunk = new BlockPos(playerPos.getX() / 16 - 1, floor, playerPos.getZ() / 16 - 1);
        return chunk;

    }

}

