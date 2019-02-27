package possibletriangle.dungeon.generator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.placeholder.IPlaceholder;
import possibletriangle.dungeon.pallete.Pallete;

import java.util.Random;

public class ChunkPrimerDungeon extends ChunkPrimerRotateable {

    private final DungeonOptions options;
    private final Random r;
    private final Pallete pallete;
    private final int variant;

    public ChunkPrimerDungeon(DungeonOptions options, Random r, Pallete pallete, int variant) {
        this.options = options;
        this.r = r;
        this.pallete = pallete;
        this.variant = variant;
    }

    public void setBlockState(int x, int y, int z, int floor, Rotation rotation, IBlockState state) {
        setBlockState(x, y, z, floor, state, rotation, false);
    }

    public void setBlockState(int x, int y, int z, int floor, IBlockState state, Rotation rotation, boolean replace) {

        if(x < 0 || z < 0 || x > 15 || z > 15 || y < 0 || (y >= DungeonOptions.FLOOR_HEIGHT && floor < options.floorCount()-1)) {
            Dungeon.LOGGER.info("Illegal generation at floor {} ({}/{}/{})", floor, x, y, z);
            return;
        }

        y += floor* DungeonOptions.FLOOR_HEIGHT;

        if(replace && getBlockStateWithRotation(x, y, z, rotation).getBlock() != Blocks.AIR) {
            return;
        }

        if(state.getBlock() instanceof IPlaceholder)
            state = pallete.get(((IPlaceholder) state.getBlock()).getType(), state, r, variant);

        else if(state.getBlock() == Blocks.LAVA)
            state = pallete.get(Pallete.Type.FLUID_HARMFUL, state, r, variant);
        else if(state.getBlock() == Blocks.WATER)
            state = pallete.get(Pallete.Type.FLUID_SAVE, state, r, variant);

        else if(state.getBlock() == Blocks.IRON_BARS)
            state = pallete.get(Pallete.Type.BARS, state, r, variant);
        else if(state.getBlock() == Blocks.LADDER)
            state = pallete.get(Pallete.Type.LADDER, state, r, variant);

        setBlockState(x, y, z, state, rotation);
    }

    @Override
    public void setBlockState(int x, int y, int z, IBlockState state, Rotation rotation) {
        if(state.getBlock() != Blocks.STRUCTURE_VOID)
            super.setBlockState(x, y, z, state, rotation);
    }
}
