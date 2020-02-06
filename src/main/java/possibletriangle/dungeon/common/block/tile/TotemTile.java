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
public class TotemTile extends TileEntity {

    @ObjectHolder("totem")
    public static final TileEntityType<TotemTile> TYPE = null;

    private BlockPos roomSize;

    public MetadataTile() {
        super(TYPE);

        ChunkPos chunk = new ChunkPos(getPos());
        Generateable room = DungeonChunkGenerator.roomsFor(getWorld(), chunk);
        this.roomSize = room.getSize();
        markDirty();
    }

    public boolean click(PlayerEntity player) {
        return true;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        this.roomSize = getPos(compound, "roomSize");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);

        putPos(this.roomSize, "roomSize", compound);

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
