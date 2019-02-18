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
    private final Pallete[] palletes;
    private final int[] variants;

    public ChunkPrimerDungeon(DungeonOptions options, Random r) {
        this.options = options;
        this.r = r;
        this.palletes = new Pallete[options.floorCount];
        this.variants = new int[options.floorCount];
    }

    public void set(int floor, Pallete pallete, int variant) {
        palletes[floor] = pallete;
        variants[floor] = variant;
    }

    public void setBlockState(int x, int y, int z, int floor, Rotation rotation, IBlockState state) {
        setBlockState(x, y, z, floor, state, rotation, false);
    }

    public void setBlockState(int x, int y, int z, int floor, IBlockState state, Rotation rotation, boolean replace) {

        if(x < 0 || z < 0 || x > 15 || z > 15 || y < 0 || (y >= options.floorHeight && floor < options.floorCount-1)) {
            Dungeon.LOGGER.info("Illegal generation at floor {} ({}/{}/{})", floor, x, y, z);
            return;
        }

        y += floor*options.floorHeight;

        if(replace && getBlockStateWithRotation(x, y, z, rotation).getBlock() != Blocks.AIR) {
            return;
        }

        Pallete pallete = floor >= palletes.length ? palletes[palletes.length-1] : palletes[floor];
        int  variant = floor >= variants.length ? variants[variants.length-1] : variants[floor];

        if(state.getBlock() instanceof IPlaceholder)
            state = pallete.get(((IPlaceholder) state.getBlock()).getType(), state, r, variant);

        else if(state.getBlock() == Blocks.LAVA)
            state = pallete.get(Pallete.Type.FLUID_HARMFUL, state, r, variant);
        else if(state.getBlock() == Blocks.WATER)
            state = pallete.get(Pallete.Type.FLUID_SAVE, state, r, variant);

        else if(state.getBlock() == Blocks.TORCH)
            state = pallete.get(Pallete.Type.TORCH, state, r, variant);
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
