package possibletriangle.dungeon.common.block.tile;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.common.DungeonCommand;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.Generateable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@ObjectHolder("dungeon")
public class TotemTile extends TileEntity implements ITickableTileEntity {

    @ObjectHolder("totem")
    public static final TileEntityType<TotemTile> TYPE = null;

    private static final AxisAlignedBB EMPTY = new AxisAlignedBB(0,0,0,0,0,0);

    @Nullable
    private BlockPos roomSize;
    @Nullable
    private Team team;
    @Nullable
    private UUID player;
    private int floor = -1;

    /**
     * Stores the players which where in the room last tick
     */
    private List<PlayerEntity> inRoom = Lists.newArrayList();
    private UUID claiming;
    private int claimProgress = 0;

    /**
     * The duration a player has to stand next to a totem to claim it in seconds
     */
    private static final int CLAIM_DURATION = 6;

    public TotemTile() {
        super(TYPE);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        ChunkPos chunk = new ChunkPos(getPos());
        DungeonCommand.roomAt(chunk.asBlockPos(), world).ifPresent(pair -> {
            Generateable room = pair.getValue();
            this.floor = pair.getKey();
            this.roomSize = room.getSize();
        });
        markDirty();
    }

    public boolean inRoom() {
        return this.roomSize != null && this.floor >= 0;
    }

    private AxisAlignedBB claimRange() {
        return new AxisAlignedBB(getPos()).grow(2);
    }

    private AxisAlignedBB roomBox() {
        if(!inRoom()) return EMPTY;
        assert this.roomSize != null;

        BlockPos start = new ChunkPos(getPos()).asBlockPos()
            .add(0, floor * DungeonSettings.FLOOR_HEIGHT, 0);

        BlockPos end = start.add(
            this.roomSize.getX() * 16,
            this.roomSize.getY() * DungeonSettings.FLOOR_HEIGHT,
            this.roomSize.getZ() * 16
        );

        return new AxisAlignedBB(start, end).grow(2);
    }

    public void tick() {

        List<PlayerEntity> claiming = this.world.getEntitiesWithinAABB(PlayerEntity.class, claimRange());

        if(claiming.size() == 1)
            this.loadClaiming(claiming.get(0));

        else if(this.claiming != null)
            this.abort();


        if(isClaimed()) {
            List<PlayerEntity> inRoom = this.world.getEntitiesWithinAABB(PlayerEntity.class, roomBox());

            inRoom.forEach(player -> {
                boolean entered = this.inRoom.contains(player);
                if(entered) this.enteredRoom(player);
                else this.inRoom(player);
            });

            /**
             * All players which were in the room last tick but are not this one
             */
            this.inRoom.removeAll(inRoom);
            this.inRoom.forEach(this::leftRoom);

            this.inRoom = inRoom;
        } 
        
    }

    public void leftRoom(PlayerEntity player) {
        if(isOwner(player)) {
            player.sendStatusMessage(new StringTextComponent("You left your base"), true);
        }
    }

    public void enteredRoom(PlayerEntity player) {
        if(isOwner(player)) {
            player.sendStatusMessage(new StringTextComponent("You entered your base"), true);
        }
    }

    public void inRoom(PlayerEntity player) {
        if(isOwner(player)) {

        } else {
            
        }
    }

    public void loadClaiming(PlayerEntity player) {
        if(this.claiming == null) this.claiming = player.getUniqueID();
        else if(this.claiming.equals(player.getUniqueID())) {

            if(this.claimProgress < CLAIM_DURATION * 20) {

                this.claimProgress++;
                /* Particles */

            } else {

                this.claim(player);

            }

        }
            
        markDirty();
    }

    public void abort() {
        /* Particles */
        this.claiming = null;
        markDirty();
    }

    public boolean isClaimed() {
        return this.team != null || this.player != null;
    }

    public boolean claim(PlayerEntity player) {
        if(!this.isClaimed()) {

            Team team = player.getTeam();
            if(team != null) this.team = team;
            else this.player = player.getUniqueID();
            markDirty();
            /* Particles */

            return true;
        }

        return false;
    }

    /**
     * @return If the player is part of the totems team or it is his totem
     */
    public boolean isOwner(PlayerEntity player) {

        Team team = player.getTeam();
        if(this.team != null && this.team.equals(team)) return true;
        return player.getUniqueID().equals(this.player);

    }

    public boolean click(PlayerEntity player) {
        return true;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        this.roomSize = getPos(compound, "roomSize");
        if(compound.contains("floor")) this.floor = compound.getInt("floor");
        if(compound.hasUniqueId("player")) this.player = compound.getUniqueId("player");
        if(compound.contains("team")) this.team = world.getScoreboard().getTeam(compound.getString("team"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);

        putPos(this.roomSize, "roomSize", compound);
        compound.putInt("floor", this.floor);
        if(this.player != null) compound.putUniqueId("player", this.player);
        if(this.team != null) compound.putString("team", this.team.getName());

        return nbt;
    }

    public static void putPos(BlockPos pos, String key, CompoundNBT compound) {
        if(pos != null) {
            compound.putInt(key + "X", pos.getX());
            compound.putInt(key + "Y", pos.getY());
            compound.putInt(key + "Z", pos.getZ());
        }
    }

    public static BlockPos getPos(CompoundNBT compound, String key) {
        int x = compound.getInt(key + "X");
        int y = compound.getInt(key + "Y");
        int z = compound.getInt(key + "Z");
        return new BlockPos(x, y, z);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {

    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

}
