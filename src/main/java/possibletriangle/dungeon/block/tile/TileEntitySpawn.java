package possibletriangle.dungeon.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.WorldDataRooms;
import possibletriangle.dungeon.helper.SpawnData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileEntitySpawn extends TileEntity implements ITickable {

    public UUID owner;
    public String lastTeam;
    private double[] offset = {0, 0, 0};
    public final double vortexSize = 1.5;

    private static final int neededTicks = 20 * 5;

    private int floorHeight;
    private BlockPos pos;
    public boolean global;

    public TileEntitySpawn(boolean global) {
        this.global = global;
    }

    public void init() {
        BlockPos chunk = WorldDataRooms.toChunk(getPos(), world);
        floorHeight = WorldDataRooms.getFloorHeight(world);
        pos = new BlockPos((chunk.getX() - 2) * 16, chunk.getY() * floorHeight, (chunk.getZ()-2) * 16);
        if(global)
            SpawnData.get(world).GLOBAL.add(getPos());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        lastTeam = compound.getString("lastTeam");
        String uuid = compound.getString("owner");
        if(!uuid.equals("")) {
            owner = UUID.fromString(uuid);
        }
        if(pos != null) {
            compound.setIntArray("roomAnchor", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        }

        compound.setDouble("offset_x", offset[0]);
        compound.setDouble("offset_y", offset[1]);
        compound.setDouble("offset_z", offset[2]);
        compound.setInteger("floorHeight", floorHeight);
        compound.setBoolean("global", global);

    }

    public double[] getOffset() {
        return offset;
    }

    public void setOffset(double x, double y, double z) {
        this.offset = new double[]{x, y, z};
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        if(lastTeam != null)
            compound.setString("lastTeam", lastTeam);
        if(owner != null)
            compound.setString("owner", owner.toString());

        int[] p = compound.getIntArray("roomAnchor");
        if(p.length == 3) {
           pos = new BlockPos(p[0], p[1], p[2]);
        }

        floorHeight = compound.getInteger("floorHeight");
        offset = new double[]{
            compound.getDouble("offset_x"),
            compound.getDouble("offset_y"),
            compound.getDouble("offset_z")
        };

        global = compound.getBoolean("global");

        return super.writeToNBT(compound);
    }

    private List<EntityPlayer> last = new ArrayList<>();
    private List<EntityPlayer> last_vortex = new ArrayList<>();
    @Override
    public void update() {

        if(pos == null || floorHeight <= 0) {
            init();
            return;
        }

        if(world.isRemote)
            return;

        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(15, floorHeight-1, 15));
        AxisAlignedBB box_vortex = new AxisAlignedBB(getPos())
                .offset(offset[0], offset[1], offset[2])
                .grow(Math.floor(vortexSize+1));

        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, box);
        List<EntityPlayer> players_vortex = world.getEntitiesWithinAABB(EntityPlayer.class, box_vortex, player -> vortexSize+0.5 >= player.getDistance(getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5));

        if(!SpawnData.hasOwner(getPos(), world) || global) {

            String s = Dungeon.MODID + ":tick_in_vortex";

            for (EntityPlayer player : players) {
                if (!last.contains(player)) {

                    player.sendMessage(new TextComponentString("Stand in the vortex for " + (neededTicks/10) + "s to claim room"));

                }

            }

            for (EntityPlayer player : players_vortex) {
                if(!last_vortex.contains(player))
                    player.sendMessage(new TextComponentString("Entered Vortex"));

                int t = player.getEntityData().getInteger(s);
                if(t < neededTicks)
                    player.getEntityData().setInteger(s, t+1);
                else {
                    player.sendMessage(new TextComponentString("You claimed the room"));
                    player.getEntityData().removeTag(s);
                    if(global)
                        SpawnData.resetSpawn(player.getUniqueID(), world);
                    else
                        setOwner(player.getUniqueID());
                }
            }

            if(last_vortex != null)
                for(EntityPlayer player : last_vortex)
                    if(player != null && !players_vortex.contains(player)) {
                        player.getEntityData().removeTag(s);
                        player.sendMessage(new TextComponentString("Left Vortex"));
                    }
        }
        if(SpawnData.hasOwner(getPos(), world) || global) for(EntityPlayer player : players) {


            if(WorldDataRooms.toChunk(getPos(), world).equals(WorldDataRooms.toChunk(SpawnData.getSpawn(player, world), world))) {

                if(!last.contains(player))
                    player.sendMessage(new TextComponentString("You entered your spawn room"));

            } else {

                if(!last.contains(player))
                    player.sendMessage(new TextComponentString("You entered someone else's spawn room"));

            }

        }

        last = players;
        last_vortex = players_vortex;

    }

    public static final boolean multipleSpawns = false;

    public void removeOwner() {
        SpawnData.declaimSpawn(getPos(), world);
    }

    public void setOwner(UUID uuid) {
        SpawnData.claimSpawn(getPos(), uuid, world);
    }

    public boolean isOwner(EntityPlayer player) {
        if(player == null)
            return false;

        return isOwner(player.getUniqueID()) || (player.getTeam() != null && player.getTeam().getName().equals(lastTeam));
 }

    public boolean isOwner(UUID uuid) {
        return owner == uuid;
    }

}
