package possibletriangle.dungeon.common.block.tile;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.EditStructureScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.client.MetadataScreen;
import possibletriangle.dungeon.common.world.structure.StructureMetadata;
import sun.net.ResourceManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

@ObjectHolder("dungeon")
public class MetadataTile extends TileEntity {

    @ObjectHolder("metadata")
    public static final TileEntityType<MetadataTile> TYPE = null;

    private String name;
    private StructureMetadata meta = StructureMetadata.getDefault();

    public String getName() {
        return name;
    }

    public StructureMetadata getMeta() {
        return meta;
    }

    public MetadataTile() {
        super(TYPE);
    }

    public void readMeta() {
        if(!this.world.isRemote && name != null) try {

            IReloadableResourceManager manager = this.world.getServer().getResourceManager();
            manager.getAllResources(new ResourceLocation(name)).stream().map(r ->
                    r.getMetadata(StructureMetadata.SERIALIZER)
            ).findFirst().ifPresent(meta -> {
                DungeonMod.LOGGER.info("Loaded meta: {}", this.meta.serializeNBT());
                this.meta = meta;
                markDirty();
            });

        } catch (IOException ex) {
            DungeonMod.LOGGER.error("IOException on reloading metdata for {}", name);
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
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        if(name != null) nbt.putString("name", name);
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {

    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

}
