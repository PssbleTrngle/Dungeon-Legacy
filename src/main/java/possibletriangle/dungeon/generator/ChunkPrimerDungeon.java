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

    public ChunkPrimerDungeon(Rotation rotation, DungeonOptions options, Random r) {
        super(rotation);
        this.options = options;
        this.r = r;
        this.palletes = new Pallete[options.floorCount];
    }

    public void set(int floor, Pallete pallete) {
        palletes[floor] = pallete;
    }

    public void setBlockState(int x, int y, int z, int floor, IBlockState state) {
        setBlockState(x, y, z, floor, state, false);
    }

    public void setBlockState(int x, int y, int z, int floor, IBlockState state, boolean replace) {

        if(x < 0 || z < 0 || x > 15 || z > 15 || y < 0 || y >= options.floorHeight) {
            Dungeon.LOGGER.info("Illegal generation atY floor {} ({}/{}/{})", floor, x, y, z);
            return;
        }

        y += floor*options.floorHeight;

        if(replace && getBlockStateWithRotation(x, y, z).getBlock() != Blocks.AIR) {
            return;
        }

        Pallete pallete = floor >= palletes.length ? palletes[palletes.length-1] : palletes[floor];
        if(pallete == null)
            pallete = Pallete.random(r);

        if(state.getBlock() instanceof IPlaceholder)
            state = pallete.get(((IPlaceholder) state.getBlock()).getType(), state, r);

        else if(state.getBlock() == Blocks.LAVA)
            state = pallete.get(Pallete.Type.FLUID_HARMFUL, state, r);
        else if(state.getBlock() == Blocks.WATER)
            state = pallete.get(Pallete.Type.FLUID_SAVE, state, r);

        else if(state.getBlock() == Blocks.TORCH)
            state = pallete.get(Pallete.Type.TORCH, state, r);
        else if(state.getBlock() == Blocks.IRON_BARS)
            state = pallete.get(Pallete.Type.BARS, state, r);
        else if(state.getBlock() == Blocks.LADDER)
            state = pallete.get(Pallete.Type.LADDER, state, r);

        setBlockState(x, y, z, state);
    }

    @Override
    public void setBlockState(int x, int y, int z, IBlockState state) {
        if(state.getBlock() != Blocks.STRUCTURE_VOID)
            super.setBlockState(x, y, z, state);
    }
}
