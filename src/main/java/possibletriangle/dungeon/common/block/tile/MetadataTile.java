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
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.client.MetadataScreen;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

@ObjectHolder("dungeon")
public class MetadataTile extends TileEntity {

    @ObjectHolder("metadata")
    public static final TileEntityType<MetadataTile> TYPE = null;

    private String name = "";
    private StructureMetadata meta = StructureMetadata.getDefault();

    public String getName() {
        return name;
    }

    public StructureMetadata getMeta() {
        return meta;
    }

    public AxisAlignedBB getBounds() {
        Vec3i size = Generateable.roomSizeFromActual(this.size);
        Vec3i from = ???;
        return new AxisAlignedBB(from, size);
    }

    public MetadataTile() {
        super(TYPE);
    }

    public void readMeta() {
        if(!this.world.isRemote && name != null) try {

            ResourceLocation n = new ResourceLocation(name);
            ResourceLocation path = new ResourceLocation(n.getNamespace(), "structures/" + n.getPath());
            IReloadableResourceManager manager = this.world.getServer().getResourceManager();
            manager.getAllResources(path).stream().map(r ->
                    r.getMetadata(StructureMetadata.SERIALIZER)
            ).filter(Objects::nonNull).findFirst().ifPresent(meta -> {
                this.meta = meta;
                markDirty();
            });

        } catch (IOException ex) {
            DungeonMod.LOGGER.error("IOException on reloading metadata for {}", name, ex);
        }
    }

    public void structureBlockUpdated(@Nonnull StructureBlockTileEntity te) {

        DungeonMod.LOGGER.info("Updated structure block");
        this.name = te.getName();
        this.readMeta();
        markDirty();

    }

    public boolean click(PlayerEntity player) {
        if (!player.canUseCommandBlock()) {
            return false;
        } else {
            if (!player.world.isRemote) {
                Minecraft.getInstance().displayGuiScreen(new MetadataScreen(this));
            }

            return true;
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        if(compound.contains("name")) name = compound.getString("name");
        if(compound.contains("meta")) this.meta.deserializeNBT(compound.getCompound("meta"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);

        if(name != null) nbt.putString("name", name);
        nbt.put("meta", meta.serializeNBT());

        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {

    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

}
