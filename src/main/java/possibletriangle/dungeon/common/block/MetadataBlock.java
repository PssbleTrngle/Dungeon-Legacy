package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.tile.MetadataTile;

import javax.annotation.Nullable;
import java.util.Optional;

@ObjectHolder("dungeon")
public class MetadataBlock extends ContainerBlock {

    @ObjectHolder("metadata_block")
    public static final Block METADATA_BLOCK = null;

    public MetadataBlock() {
        super(Block.Properties.create(Material.IRON, MaterialColor.LIGHT_GRAY)
                .hardnessAndResistance(-1.0F, 3600000.0F)
                .noDrops()
        );
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new MetadataTile();
    }

    public Optional<MetadataTile> getTE(IBlockReader world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof MetadataTile) return Optional.of((MetadataTile) te);
        return Optional.empty();
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        TileEntity structureBlock = world.getTileEntity(neighbor);
        if(structureBlock instanceof StructureBlockTileEntity) {

            getTE(world, pos).ifPresent(te -> te.structureBlockUpdated((StructureBlockTileEntity) structureBlock));

        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return getTE(world, pos).map(te -> te.click(player)).orElse(false);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
