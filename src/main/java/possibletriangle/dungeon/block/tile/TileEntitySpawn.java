package possibletriangle.dungeon.block.tile;

import javafx.scene.chart.Axis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.WorldDataRooms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileEntitySpawn extends TileEntity implements ITickable {

    private UUID owner;
    String lastTeam;

    private int floorHeight;
    private BlockPos pos;
    public TileEntitySpawn() {
        BlockPos chunk = WorldDataRooms.toChunk(getPos(), getWorld());
        floorHeight = WorldDataRooms.getFloorHeight(world);
        pos = new BlockPos((chunk.getX() - 2) * 16, chunk.getY() * floorHeight, (chunk.getZ()-2) * 16);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        lastTeam = compound.getString("lastTeam");
        String uuid = compound.getString("owner");
        if(uuid != null) {
            owner = UUID.fromString(uuid);
        }
        if(pos != null) {
            compound.setIntArray("roomAnchor", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        }

        compound.setInteger("floorHeight", floorHeight);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        if(lastTeam != null)
            compound.setString("lastTeam", lastTeam);
        if(owner != null)
            compound.setString("owner", owner.toString());

        int[] p = compound.getIntArray("roomAnchor");
        if(p != null) {
           pos = new BlockPos(p[0], p[1], p[2]);
        }

        floorHeight = compound.getInteger("floorHeight");

        return super.writeToNBT(compound);
    }

    private List<EntityPlayer> last = new ArrayList<>();
    @Override
    public void update() {

        if(pos == null || floorHeight <= 0)
            return;

        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(15, floorHeight-1, 15));
        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, box);

        if(owner != null) {
            EntityPlayer player = getWorld().getPlayerEntityByUUID(owner);
            if(player != null) {

                String newTeam = player.getTeam() == null ? null : player.getTeam().getName();
                if(newTeam == null || !newTeam.equals(lastTeam)) {
                    markDirty();
                }

            }

        } else {

            if(!world.isRemote) {
                for (EntityPlayer player : players)
                    if (!last.contains(player)) {

                        player.sendMessage(new TextComponentString("Make this room your spawn?"));

                    }

            }

        }

        last = players;

    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        markDirty();
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
