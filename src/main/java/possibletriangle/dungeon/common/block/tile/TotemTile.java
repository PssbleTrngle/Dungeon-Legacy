package possibletriangle.dungeon.common.block.tile;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.command.impl.TeamCommand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
<<<<<<< HEAD:src/main/java/possibletriangle/dungeon/common/block/tile/TotemTile.java
=======
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
>>>>>>> Obelisk & Metadata TESR Outline:src/main/java/possibletriangle/dungeon/common/block/tile/ObeliskTile.java
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;
<<<<<<< HEAD:src/main/java/possibletriangle/dungeon/common/block/tile/TotemTile.java
import possibletriangle.dungeon.common.DungeonCommand;
import possibletriangle.dungeon.common.block.TotemBlock;
=======
import possibletriangle.dungeon.common.block.ObeliskBlock;
import possibletriangle.dungeon.common.world.DungeonChunkGenerator;
>>>>>>> Obelisk & Metadata TESR Outline:src/main/java/possibletriangle/dungeon/common/block/tile/ObeliskTile.java
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.Generateable;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

<<<<<<< HEAD:src/main/java/possibletriangle/dungeon/common/block/tile/TotemTile.java
@ObjectHolder("dungeon")
public class TotemTile extends TileEntity implements ITickableTileEntity {

    @ObjectHolder("totem")
    public static final TileEntityType<TotemTile> TYPE = null;
=======
public class ObeliskTile extends TileEntity implements ITickableTileEntity {

    @ObjectHolder("dungeon:obelisk")
    public static final TileEntityType<ObeliskTile> TYPE = null;
>>>>>>> Obelisk & Metadata TESR Outline:src/main/java/possibletriangle/dungeon/common/block/tile/ObeliskTile.java

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

    public void updateTeam() {
        if(this.world != null && this.player != null) {
            PlayerEntity player = this.world.getPlayerByUuid(this.player);
            if(player != null) {
                Team team = player.getTeam();
                if(team != null) {
                    this.player = null;
                    this.team = team;
                    markDirty();
                    updateState();
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        updateTeam();

        /* Find the room it was placed in and save the required information */
        ChunkPos chunk = new ChunkPos(getPos());
        DungeonChunkGenerator.roomAt(chunk.asBlockPos(), world).ifPresent(pair -> {

            Generateable room = pair.getValue();
            this.floor = pair.getKey();
            this.roomSize = room.getSize();
            markDirty();

        });

        if(!this.inRoom()) updateState(TotemBlock.State.INVALID);

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

        return new AxisAlignedBB(start, end);
    }

    public void tick() {
        if(!inRoom()) return;

        if(!isClaimed()) {
            List<PlayerEntity> claiming = this.world.getEntitiesWithinAABB(PlayerEntity.class, claimRange());
            if (claiming.size() == 1)
                this.loadClaiming(claiming.get(0));
            else if (this.claiming != null)
                this.abort();
        }

        {
            AxisAlignedBB box = this.roomBox();
            double s = 0.5;
            for(double x = box.minX; x <= box.maxX; x += s)
                for(double y = box.minY; y <= box.maxY; y += s)
                    for(double z = box.minZ; z <= box.maxZ; z += s) {
                        int bx = x <= box.minX || x > box.maxX - s ? 0 : 1;
                        int by = y <= box.minY || y > box.maxY - s ? 0 : 1;
                        int bz = z <= box.minZ || z > box.maxZ - s ? 0 : 1;
                        if(bx + by + bz < 2) world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
                    }
        }

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
        assert world != null;
        if(isOwner(player)) {
            player.sendStatusMessage(new StringTextComponent("You left your base"), true);
        }
    }

    public void enteredRoom(PlayerEntity player) {
        assert world != null;
        if(isOwner(player)) {
            player.sendStatusMessage(new StringTextComponent("You entered your base"), true);
            player.setSpawnPoint(getPos(), true, world.getDimension().getType());
        }
    }

    public void inRoom(PlayerEntity player) {
        if(isOwner(player)) {

        } else {
            
        }
    }

    public void particleIn(IParticleData particle, double radius, int count) {
        if(world instanceof ServerWorld) {
            double x = Math.random() * radius + 0.5 - radius / 2 + getPos().getX();
            double y = Math.random() * radius + getPos().getY();
            double z = Math.random() * radius + 0.5 - radius / 2 + getPos().getZ();
            ((ServerWorld) world).spawnParticle(particle, x, y, z, count, 0, 0, 0, 0);
        }
    }

    public void loadClaiming(PlayerEntity player) {
        if(this.claiming == null) this.claiming = player.getUniqueID();
        else if(this.claiming.equals(player.getUniqueID())) {

            if(this.claimProgress < CLAIM_DURATION * 20) {

                this.claimProgress++;
<<<<<<< HEAD:src/main/java/possibletriangle/dungeon/common/block/tile/TotemTile.java
                /* Particles */
=======
                particleIn(ParticleTypes.END_ROD, 2, 1);
>>>>>>> Obelisk & Metadata TESR Outline:src/main/java/possibletriangle/dungeon/common/block/tile/ObeliskTile.java

            } else {

                this.claim(player);

            }

        }

        markDirty();
    }

    public void abort() {
<<<<<<< HEAD:src/main/java/possibletriangle/dungeon/common/block/tile/TotemTile.java
        /* Particles */
=======

        particleIn(ParticleTypes.POOF, 3, 20);
>>>>>>> Obelisk & Metadata TESR Outline:src/main/java/possibletriangle/dungeon/common/block/tile/ObeliskTile.java
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
            this.markDirty();
            /* Particles */

<<<<<<< HEAD:src/main/java/possibletriangle/dungeon/common/block/tile/TotemTile.java
            this.updateState(TotemBlock.State.CLAIMED);
=======
            if(this.world instanceof ServerWorld) {
                double x = getPos().getX() + 0.5;
                double y = getPos().getY() + 1;
                double z = getPos().getZ() + 0.5;
                ((ServerWorld) world).spawnParticle(ParticleTypes.ENCHANT, x, y, z, 100, 0, 0, 0, 5);
            };

            this.updateState(ObeliskBlock.State.CLAIMED);
>>>>>>> Obelisk & Metadata TESR Outline:src/main/java/possibletriangle/dungeon/common/block/tile/ObeliskTile.java

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

    public int getColor() {
        if(this.team != null) return Optional.ofNullable(team.getColor().getColor()).orElse(TotemBlock.State.CLAIMED.color);
        else if(this.player != null) return TotemBlock.State.CLAIMED.color;
        else if(this.inRoom()) return TotemBlock.State.UNCLAIMED.color;
        return TotemBlock.State.INVALID.color;
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

        if(this.inRoom()) {
            putPos(this.roomSize, "roomSize", compound);
            compound.putInt("floor", this.floor);
        }

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

<<<<<<< HEAD:src/main/java/possibletriangle/dungeon/common/block/tile/TotemTile.java
    private void updateState(TotemBlock.State state) {
=======
    private void updateState() {
        if(this.world == null) return;
        ObeliskBlock.State state = world.getBlockState(getPos()).get(ObeliskBlock.STATE);
        this.updateState(state);
    }

    private void updateState(ObeliskBlock.State state) {
>>>>>>> Obelisk & Metadata TESR Outline:src/main/java/possibletriangle/dungeon/common/block/tile/ObeliskTile.java
        if(this.world == null) return;

        BlockState block = TotemBlock.TOTEM.getDefaultState().with(TotemBlock.STATE, state);
        this.world.setBlockState(getPos(), block);
    }

}
