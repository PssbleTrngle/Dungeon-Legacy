package possibletriangle.dungeon.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

import java.util.Random;

public abstract class RedstoneLinkBlock extends Block {

    public static final RegistryObject<Block> RECEIVER = DungeonMod.registerBlock("redstone_receiver", RedstoneReceiverBlock::new);
    public static final RegistryObject<Block> SENDER = DungeonMod.registerBlock("redstone_sender", RedstoneSenderBlock::new);

    public static final int DISTANCE = 6;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public RedstoneLinkBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F).tickRandomly());
        setDefaultState(this.getDefaultState().with(POWERED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(POWERED);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.get(POWERED)) {
            double x = Math.random() * 1.4 + pos.getX() - 0.2;
            double y = Math.random() * 1.4 + pos.getY() - 0.2;
            double z = Math.random() * 1.4 + pos.getZ() - 0.2;
            world.addParticle(RedstoneParticleData.REDSTONE_DUST, x, y, z, 0, 0, 0);
        }
    }
}
