package possibletriangle.dungeon.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.MetadataBlock;
import possibletriangle.dungeon.world.generator.DungeonSettings;
import possibletriangle.dungeon.world.structure.IStructure;
import possibletriangle.dungeon.world.structure.StructureLoader;
import possibletriangle.dungeon.world.structure.metadata.StructureMetadata;

import javax.annotation.Nonnull;
import java.util.Optional;

public class MetadataTile extends BaseTile {

    public static final RegistryObject<TileEntityType<? extends MetadataTile>> TYPE = DungeonMod.TILES.register("metadata", () -> TileEntityType.Builder.create(
            MetadataTile::new, MetadataBlock.METADATA_BLOCK.get()).build(null)
    );

    private String name = "";
    private StructureMetadata meta = StructureMetadata.getDefault();
    private BlockPos from, size;

    public BlockPos getOffset() {
        return from;
    }

    public String getName() {
        return name;
    }

    public StructureMetadata getMeta() {
        return meta;
    }

    public Optional<AxisAlignedBB> getBounds() {
        if (this.size == null) return Optional.empty();
        BlockPos r = IStructure.roomSizeFromActual(this.size);
        BlockPos size = new BlockPos(
                (r.getX() - 1) * 16 + 15,
                r.getY() * DungeonSettings.FLOOR_HEIGHT,
                (r.getZ() - 1) * 16 + 15
        );
        return Optional.of(new AxisAlignedBB(new BlockPos(0, 0, 0), size).offset(getOffset()));
    }

    public MetadataTile() {
        super(TYPE.get());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return this.getBounds().orElseGet(super::getRenderBoundingBox);
    }

    public void readMeta() {
        if (world != null && !world.isRemote && name != null) {
            ServerWorld world = (ServerWorld) this.world;

            ResourceLocation n = new ResourceLocation(name);
            ResourceLocation path = new ResourceLocation(n.getNamespace(), "structures/" + n.getPath() + ".nbt");
            IResourceManager manager = world.getServer().getDataPackRegistries().func_240970_h_();

            StructureLoader.getMetadata(manager, path).ifPresent(meta -> {
                this.meta = meta;
                markDirty();
            });
        }
    }

    public void structureBlockUpdated(@Nonnull StructureBlockTileEntity te) {

        BlockPos offset = te.getPos().subtract(this.getPos());
        this.name = te.getName();
        this.from = te.getPosition().add(offset);
        this.size = te.getStructureSize();

        this.readMeta();
        markDirty();

    }

    public boolean click(PlayerEntity player) {
        if (!player.canUseCommandBlock()) {
            return false;
        } else {
            //TODO open inventory
            //DungeonMod.openMetaTile(this);
            return true;
        }
    }

    @Override
    public void deserializeNBT(BlockState state, CompoundNBT compound) {
        super.deserializeNBT(state, compound);

        if (compound.contains("name")) name = compound.getString("name");
        if (compound.contains("meta")) this.meta.deserializeNBT(compound.getCompound("meta"));
        this.from = ObeliskTile.getPos(compound, "from");
        this.size = ObeliskTile.getPos(compound, "size");
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = super.serializeNBT();

        if (name != null) compound.putString("name", name);
        compound.put("meta", meta.serializeNBT());
        ObeliskTile.putPos(this.from, "from", compound);
        ObeliskTile.putPos(this.size, "size", compound);

        return compound;
    }

}
