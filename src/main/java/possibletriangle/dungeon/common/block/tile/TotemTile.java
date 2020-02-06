package possibletriangle.dungeon.common.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.client.MetadataScreen;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

@ObjectHolder("dungeon")
public class TotemTile extends TileEntity implements ITickable {

    @ObjectHolder("totem")
    public static final TileEntityType<TotemTile> TYPE = null;

    private BlockPos roomSize;
    private String team;
    private UUID player;
    private int floor;

    /**
     * Stores the players which where in the room last tick
     */
    private List<PlayerEntity> inRoom = new ArrayList<>();
    private UUID claiming;
    private int claimProgress = 0;

    /**
     * The duration a player has to stand next to a totem to claim it in seconds
     */
    private static final int CLAIM_DURATION = 6;

    public TotemTile() {
        super(TYPE);

        ChunkPos chunk = new ChunkPos(getPos());
        Pair<Generateable,Integer> pair = DungeonCommand.roomAt(getWorld(), chunk);
        Generateable room = pair.getKey();
        this.floor = pair.getValue();
        this.roomSize = room.getSize();
        markDirty();
    }

    private AxisAlignedBB claimRange() {
        return new AxisAlignedBB(getPos()).grow(2);
    }

    private AxisAlignedBB roomBox() {

        BlockPos start = new ChunkPos(getPos()).asBlockPos()
            .add(0, floor * DungeonSettings.FLOOR_HEIGHT, 0);

        BlockPos end = start.add(
            this.size.getX() * 16,
            this.size.getY() * DungeonSettings.FLOOR_HEIGHT,
            this.size.getZ() * 16,
        );

        return new AxisAlignedBB(start, end).grow(2);
    }

    public void tick() {

        List<PlayerEntity> claiming = this.world.getEntitiesWithing(claimRange());

        if(!claiming.size() == 1)
            this.loadClaiming(claiming.get(0));

        else if(this.claiming != null)
            this.abort();


        if(isClaimed()) {
            List<PlayerEntity> inRoom = this.world.getEntitiesWithing(roomBox());

            inRoom.forEach(player -> {
                boolean entered = this.inRoom.contains(player);
                if(entered) this.enteredRoom(player);
                else(entered) this.inRoom(player);
            });

            /**
             * All players which were in the room last tick but are not this one
             */
            this.inRoom.removeAll(inRoom)
            this.inRoom.forEach(this::leftRoom);

            this.inRoom = inRoom;
        } 
        
    }

    public void leftRoom(PlayerEntity player) {
        if(isOwner(player)) {
            player.sendMessage(new StringTextComponent("You left your base"), true);
        }
    }

    public void enteredRoom(PlayerEntity player) {
        if(isOwner(player)) {
            player.sendMessage(new StringTextComponent("You entered your base"), true);
        }
    }

    public void inRoom(PlayerEntity player) {
        if(isOwner(player)) {

        } else {
            
        }
    }

    public void loadClaiming(PlayerEntity player) {
        if(this.claiming == null) this.claiming = player.getUUID();
        else if(this.claiming.equals(player.getUUID())) {

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

            String team = player.getTeam();
            if(team != null) this.team = team;
            else this.player = player.getUUID();
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

        String team = player.getTeam();
        if(this.team != null && this.team.equals(team)) return true;
        return player.getUUID().equals(this.player);

    }

    public boolean click(PlayerEntity player) {
        return true;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        this.roomSize = getPos(compound, "roomSize");
        if(comound.has("floor")) this.floor = compount.getInt("floor");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);

        putPos(this.roomSize, "roomSize", compound);
        comound.putInt("floor", this.floor);

        return nbt;
    }

    public static void putPos(BlockPos pos, String key, CompoundNBT compound) {
        compound.putInt(key + "X", pos.getX());
        compound.putInt(key + "Y", pos.getY());
        compound.putInt(key + "Z", pos.getZ());
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
