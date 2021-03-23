package possibletriangle.dungeon.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.tile.ObeliskTile;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;
import java.util.stream.Stream;

public class ObeliskBlock extends Block {

    public static final Property<State> STATE = EnumProperty.create("state", State.class);
    private static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(0, 0, 0, 16, 16, 16),
            Block.makeCuboidShape(1, 16, 1, 15, 22, 15),
            Block.makeCuboidShape(0, 22, 0, 16, 32, 16)
    ).reduce(VoxelShapes.empty(), VoxelShapes::or, (v1, v2) -> v1);

    public static final RegistryObject<Block> OBELISK = DungeonMod.registerBlock("obelisk", ObeliskBlock::new);

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
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ObeliskTile();
    }


    public static Optional<ObeliskTile> getTE(IBlockReader world, BlockPos pos) {
        if(world == null) return Optional.empty();
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof ObeliskTile) return Optional.of((ObeliskTile) te);
        return Optional.empty();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        player.sendStatusMessage(new StringTextComponent("Hi, my state is " + state.get(STATE).func_176610_l()), true);
        return getTE(world, pos).filter(te -> te.click(player))
                .map($ -> ActionResultType.SUCCESS)
                .orElse(ActionResultType.PASS);
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
            return func_176610_l();
        }

        @Override
        public String func_176610_l() {
            return this.name().toLowerCase();
        }
    }

}
