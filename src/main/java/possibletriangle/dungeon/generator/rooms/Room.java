package possibletriangle.dungeon.generator.rooms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.helper.RandomCollection;
import possibletriangle.dungeon.generator.WorldDataRooms;
import possibletriangle.dungeon.helper.SpawnData;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber
public abstract class Room {

    private final ResourceLocation ID;
    private boolean simple = true;

    public Room(ResourceLocation id) {
        this.ID = id;
    }

    public Room(String id) {
        this(new ResourceLocation(id));
    }

    public abstract boolean generateWall();

    public abstract boolean noCeiling();

    public final ResourceLocation getName() {
        return ID;
    }

    public final boolean canReallyGoOnFloor(int floor, int chunkX, int chunkZ, int floorCount, Rotation rotation, World world) {
        return canReallyGoOnFloor(floor, chunkX, chunkZ, floorCount, rotation, world, 0);
    }

    private boolean canReallyGoOnFloor(int floor, int chunkX, int chunkZ, int floorCount, Rotation rotation, World world, int tries) {

        if(tries > 10) {
            Dungeon.LOGGER.error("Infinite Recursive Room testing for {}", getName());
            return false;
        }

        boolean b1 = (heightest_dependent + floor < floorCount) && (!onlytop || floor == floorCount-1) && (!onlybottom || floor == 0);
        if(!(b1 && canGoOnFloor(floor, floorCount)))
            return false;

        if(simple)
            return true;

        boolean b2 = true;

        int offset = 1;
        for(int x = -offset; x <= offset; x++)
            for(int z = -offset; z <= offset; z++) {
                int[] i = new int[]{x, z};

                Room room = roomAt(i, rotation, new Random());
                if(room == null)
                    continue;

                RoomData existing = WorldDataRooms.atFloor(chunkX + i[0], floor, chunkZ + i[1], world);
                b2 = b2 && existing == null && room.canReallyGoOnFloor(floor, chunkX + i[0], chunkZ + i[1], floorCount, rotation, world, tries+1);
        }

        return b2;
    }

    public boolean canGoOnFloor(int floor, int floorCount) {
        return true;
    }

    public @Nullable BlockPos safeSpot() {
        return null;
    }

    private final HashMap<Integer, RandomCollection<ResourceLocation>> ABOVE = new HashMap<>();
    private final HashMap<String, RandomCollection<ResourceLocation>> SIDE = new HashMap<>();
    private int heightest_dependent = 0;

    public final void addDependendent(int floorAbove, String s) {
        addDependendent(floorAbove, s, 1);
    }

    public final void addDependendent(int floorAbove, String s, double weight) {
        if(floorAbove > 0) {
            heightest_dependent = Math.max(heightest_dependent, floorAbove);

            RandomCollection<ResourceLocation> c = ABOVE.get(floorAbove);
            if(c == null) c = new RandomCollection<>();
            c.add(weight, new ResourceLocation(s));
            ABOVE.put(floorAbove, c);

        }
    }

    public final Room roomAbove(int floorAbove, Random r) {
        if(!ABOVE.containsKey(floorAbove)) return null;
        return RoomManager.get(ABOVE.get(floorAbove).next(r));
    }

    @Deprecated
    public final Room roomAt(EnumFacing side, Rotation rotation, Random r) {

        switch (side) {
            case UP:
            case DOWN:
                return null;
        }

        return roomAt(new int[]{side.getDirectionVec().getX(), side.getDirectionVec().getZ()}, rotation, r);
    }

    public final Room roomAt(int[] offset, Rotation rotation, Random r) {

        int parts_pi = 0;
        switch(rotation) {
            case CLOCKWISE_90:
                parts_pi = 1;
                break;
            case CLOCKWISE_180:
                parts_pi = 2;
                break;
            case COUNTERCLOCKWISE_90:
                parts_pi = -1;
                break;
        }

        int cos = (int) MathHelper.cos((float) Math.PI/2*parts_pi);
        int sin = (int) MathHelper.sin((float) Math.PI/2*parts_pi);

        int x = offset[0] * cos  - offset[1] * sin;
        int z = offset[0] * sin  + offset[1] * cos;

        int[] i = new int[]{x, z};
        if(!SIDE.containsKey(Arrays.toString(i))) return null;
        return RoomManager.get(SIDE.get(Arrays.toString(i)).next(r));
    }

    public final void addDependendent(EnumFacing side, String s) {
        addDependendent(side, s, 1);
    }

    public final void addDependendent(EnumFacing side, String s, double weight) {
        switch (side) {
            case EAST:
            case WEST:
            case NORTH:
            case SOUTH:
                addDependendent(new int[]{side.getDirectionVec().getX(), side.getDirectionVec().getZ()}, s, weight);
        }
    }

    public final void addDependendent(EnumFacing side1, EnumFacing side2, String s) {
        addDependendent(side1, side2, s, 1);
    }

    public final void addDependendent(EnumFacing side1, EnumFacing side2, String s, double weight) {
        int[] offset = new int[2];
        switch (side1) {
            case EAST:
            case WEST:
            case NORTH:
            case SOUTH:
                offset[0] += side1.getDirectionVec().getX();
                offset[1] += side1.getDirectionVec().getZ();
        }

        switch (side2) {
            case EAST:
            case WEST:
            case NORTH:
            case SOUTH:
                offset[0] += side2.getDirectionVec().getX();
                offset[1] += side2.getDirectionVec().getZ();
        }

        addDependendent(offset, s, weight);
    }

    private void addDependendent(int[] offset, String s, double weight) {

        RandomCollection<ResourceLocation> c = SIDE.get(offset);
        if(c == null) c = new RandomCollection<>();
        c.add(weight, new ResourceLocation(s));
        SIDE.put(Arrays.toString(offset), c);
        simple = false;

    }

    private boolean onlybottom = false;
    public Room onlyBottom() {
        onlytop = false;
        onlybottom = true;
        return this;
    }

    private boolean onlytop = false;
    public Room onlyTop() {
        onlybottom = false;
        onlytop = true;
        return this;
    }

    public abstract void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r, Rotation rotation);

    public void tickPlayer(EntityPlayer player, BlockPos chunk) {

    }

    public void enter(EntityPlayer player, BlockPos chunk) {

    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {

        BlockPos chunk = WorldDataRooms.toChunk(event.player.getPosition(), event.player.getEntityWorld());
        RoomData data = WorldDataRooms.atFloor(chunk.getX(), chunk.getY(), chunk.getZ(), event.player.getEntityWorld());
        if(data == null)
            return;

        Room room = RoomManager.get(data.name);
        if(room != null) {
            room.tickPlayer(event.player, chunk);
        }

    }

    public abstract void populate(DungeonOptions options, World world, int chunkX, int chunkZ, int floor, Random r);

}
