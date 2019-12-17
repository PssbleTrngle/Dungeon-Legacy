package possibletriangle.dungeon.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.IPlaceholder;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.Type;

import java.util.Arrays;
import java.util.Random;

public class DungeonChunk {

    private final IChunk chunk;
    private final Random random;
    private int floor = 0;
    private final DungeonSettings settings;
    private final PlacementSettings placement;
    private final Palette palette;

    void setFloor(int floor) {
        this.floor = floor;
    }

    public DungeonChunk(IChunk chunk, Random random, DungeonSettings settings, PlacementSettings placement) {
        this.chunk = chunk;
        this.random = random;
        this.settings = settings;
        this.placement = placement;
        this.palette = Palette.random(random);
    }

    public ChunkPos getPos() {
        return this.chunk.getPos();
    }

    public BlockState setBlockState(BlockPos pos, BlockState state, Rotation rotation) {

        if(state.getBlock() instanceof IPlaceholder) {

            Type type = ((IPlaceholder) state.getBlock()).getType();
            BlockState replace = palette.blockFor(type, random).getDefaultState();

            BlockState applied =
                    state.getProperties()
                            .stream()
                            .reduce(replace, (s, p) -> s.has(p) ? s.with((IProperty) p, state.get(p)) : s, (a, b) -> a);

            return this.setBlockState(pos, applied);

        } else {

            float phi = (float) (rotation.ordinal() * Math.PI / 2);
            int sin = (int) MathHelper.sin(phi);
            int cos = (int) MathHelper.cos(phi);

            float[] centered = new float[]{pos.getX() - 7.5F, pos.getZ() - 7.5F};

            BlockPos rotated = new BlockPos(
                    (int) (centered[0] * cos - centered[1] * sin + 7.5F),
                    pos.getY() + floor * (settings.floorHeight + 1),
                    (int) (centered[0] * sin + centered[1] * cos + 7.5F)
            );

            return chunk.setBlockState(rotated, state, false);
        }
    }

    public BlockState setBlockState(BlockPos pos, BlockState state) {
        return setBlockState(pos, state, this.placement.getRotation());
    }

    public BlockState getBlockState(BlockPos pos) {
        return chunk.getBlockState(pos);
    }
}
