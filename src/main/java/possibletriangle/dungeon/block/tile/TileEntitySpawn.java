package possibletriangle.dungeon.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.WorldDataRooms;
import possibletriangle.dungeon.helper.SpawnData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class TileEntitySpawn extends TileEntity implements ITickable {

    private double[] offset = {0.5, 0, 0.5};
    public final double vortexSize = 1.5;

    private static final int neededTicks = 20 * 8;

    private int floorHeight;
    private BlockPos chunk;
    public boolean global;

    public TileEntitySpawn(boolean global) {
        this.global = global;
    }

    public TileEntitySpawn() {
        this(false);
    }

    public void initSpawn() {
        floorHeight = WorldDataRooms.getFloorHeight(world);
        this.chunk = WorldDataRooms.toChunk(getPos(), world);
        if(global)
            SpawnData.get(world).GLOBAL.add(getPos());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        compound.setDouble("offset_x", offset[0]);
        compound.setDouble("offset_y", offset[1]);
        compound.setDouble("offset_z", offset[2]);
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

        if(world.isRemote)
            return;

        if(chunk == null || floorHeight <= 0 || chunk.getX() != getPos().getX()/16 - 1 || chunk.getZ() != getPos().getZ()/16 - 1) {
            initSpawn();
            return;
        }

        BlockPos anchor_pos = new BlockPos(this.chunk.getX() * 16 + 16, this.chunk.getY() * floorHeight, this.chunk.getZ() * 16 + 16);
        AxisAlignedBB box = new AxisAlignedBB(anchor_pos, anchor_pos.add(15, floorHeight-1, 15));
        AxisAlignedBB box_vortex = new AxisAlignedBB(getPos())
                .offset(offset[0], offset[1], offset[2])
                .grow(Math.floor(vortexSize+1));

        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, box);
        List<EntityPlayer> players_vortex = world.getEntitiesWithinAABB(EntityPlayer.class, box_vortex, player -> vortexSize+0.5 >= player.getDistance(getPos().getX()+0.5, getPos().getY()+0.5, getPos().getZ()+0.5));

        boolean hasOwner = SpawnData.hasOwner(this.chunk, world);
        if(!hasOwner || global) {

            String s = Dungeon.MODID + ":tick_in_vortex";

            for (EntityPlayer player : players) {
                if (!last.contains(player)) {

                    player.sendMessage(new TextComponentString("Stand in the vortex for " + (neededTicks/20) + "s to claim room"));

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

        if(hasOwner || global) for(EntityPlayer player : players) {

            if(isOwner(player)) {

                if(!last.contains(player))
                    player.sendMessage(new TextComponentString("You entered your spawn room"));

            } else {

                if(!last.contains(player)) {
                    player.sendMessage(new TextComponentString("You entered someone else's spawn room"));
                }

                Vec3d current = new Vec3d(player.posX, player.posY, player.posZ);
                Vec3d next = current.addVector(player.motionX, 0, player.motionZ);
                double current_dist = current.distanceTo(new Vec3d(getPos()));
                double next_dist = next.distanceTo(new Vec3d(getPos()));

                player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("weakness"), 20*1, 100, true, false));

            }

        }

        last = players;
        last_vortex = players_vortex;

    }

    public void removeOwner() {
        SpawnData.declaimSpawn(getPos(), world);
    }

    public void setOwner(UUID uuid) {
        SpawnData.claimSpawn(getPos(), uuid, world);
    }

    public boolean isOwner(EntityPlayer player) {
        if(player == null)
            return false;

        return isOwner(player.getUniqueID()) || chunk.equals(SpawnData.getSpawnChunk(player, world));
 }

    public boolean isOwner(UUID uuid) {
        return uuid .equals(SpawnData.getOwner(chunk, world));
    }

    @Override
    public void rotate(Rotation rotation) {
        super.rotate(rotation);

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

        offset[0] = offset[0] * cos - offset[1] * sin;
        offset[1] = offset[0] * sin + offset[1] * cos;

        markDirty();
    }
}
