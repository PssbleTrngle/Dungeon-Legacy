package possibletriangle.dungeon.rooms;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.RandomCollection;
import possibletriangle.dungeon.generator.WorldDataRooms;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber
public abstract class Room {

    private final ResourceLocation ID;

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

    public final boolean canReallyGoOnFloor(int floor, int floorCount) {
        return (heightest_dependent + floor < floorCount) && (!onlytop || floor == floorCount-1) && (!onlybottom || floor == 0) && canGoOnFloor(floor, floorCount);
    }

    public boolean canGoOnFloor(int floor, int floorCount) {
        return true;
    }

    public @Nullable BlockPos safeSpot() {
        return null;
    }

    private final HashMap<Integer, RandomCollection<ResourceLocation>> ABOVE = new HashMap<>();
    private final HashMap<EnumFaceDirection, RandomCollection<ResourceLocation>> SIDE = new HashMap<>();
    private int heightest_dependent = 0;

    public final void addDependendent(int floorAbove, String s) {
        addDependendent(floorAbove, s, 1);
    }

    public final void addDependendent(int floorAbove, String s, double weight) {
        if(floorAbove > 0) {
            floorAbove = Math.max(heightest_dependent, floorAbove);

            RandomCollection c = ABOVE.get(floorAbove);
            if(c == null) c = new RandomCollection();
            c.add(weight, new ResourceLocation(s));
            ABOVE.put(floorAbove, c);

        }
    }

    public final Room roomAbove(int floorAbove, Random r) {
        if(!ABOVE.containsKey(floorAbove)) return null;
        return RoomManager.get(ABOVE.get(floorAbove).next(r));
    }

    public final Room roomAt(EnumFaceDirection side, Random r) {
        if(!SIDE.containsKey(side)) return null;
        return RoomManager.get(SIDE.get(side).next(r));
    }

    public final void addDependendent(EnumFaceDirection side, String s) {
        addDependendent(side, s, 1);
    }

    public final void addDependendent(EnumFaceDirection side, String s, double weight) {
        switch (side) {
            case EAST:
            case WEST:
            case NORTH:
            case SOUTH:

            RandomCollection c = SIDE.get(side);
            if(c == null) c = new RandomCollection();
            c.add(weight, new ResourceLocation(s));
                SIDE.put(side, c);
        }
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

    public abstract void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r);

    public void tickPlayer(EntityPlayer player, BlockPos chunk) {

    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {

        BlockPos chunk = WorldDataRooms.toChunk(event.player.getPosition(), event.player.getEntityWorld());
        ResourceLocation id = WorldDataRooms.atFloor(chunk.getX(), chunk.getY(), chunk.getZ(), event.player.getEntityWorld());
        Room room = RoomManager.get(id);
        if(room != null)
            room.tickPlayer(event.player, chunk);


    }

}
