package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.common.block.tile.ObeliskTile;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;
import java.util.stream.Stream;

public class ObeliskBlock extends ContainerBlock {

    public static final IProperty<State> STATE = EnumProperty.create("state", State.class);
    private static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(0, 0, 0, 16, 16, 16),
            Block.makeCuboidShape(1, 16, 1, 15, 22, 15),
            Block.makeCuboidShape(0, 22, 0, 16, 32, 16)
    ).reduce(VoxelShapes.empty(), VoxelShapes::or, (v1, v2) -> v1);

    @ObjectHolder("dungeon:obelisk")
    public static final Block OBELISK = null;

    public ObeliskBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.GRAY)
                .hardnessAndResistance(-1.0F, 3600000.0F)
                .noDrops()
        );
        this.setDefaultState(getDefaultState().with(STATE, State.UNCLAIMED));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
		builder.add(STATE);
	}

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new ObeliskTile();
    }

    public static Optional<ObeliskTile> getTE(IBlockReader world, BlockPos pos) {
        if(world == null) return Optional.empty();
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof ObeliskTile) return Optional.of((ObeliskTile) te);
        return Optional.empty();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        player.sendStatusMessage(new StringTextComponent("Hi, my state is " + state.get(STATE).getName()), true);
        return getTE(world, pos).map(te -> te.click(player)).orElse(false);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public enum State implements IStringSerializable {
        UNCLAIMED(new Color(42,42,42)),
        CLAIMED(new Color(255, 255, 255)),
        INVALID(new Color(175, 17, 17));

        public final int color;
        State(int color) {
            this.color = color;
        }

        State(Color color) {
            this(color.getRGB());
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
    }

}
