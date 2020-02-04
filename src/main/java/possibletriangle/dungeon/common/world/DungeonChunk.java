package possibletriangle.dungeon.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import possibletriangle.dungeon.common.block.IPlaceholder;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.Type;
import possibletriangle.dungeon.common.data.DungeonLoot;

import java.util.Random;

public class DungeonChunk {

    private final IChunk chunk;
    private final Random random;
    private final PlacementSettings placement;
    private final GenerationContext ctx;
    private int variant;

    public DungeonChunk(IChunk chunk, Random random, GenerationContext ctx) {
        this(random.nextInt(Palette.MAX_VARIANT), chunk, random, ctx, new PlacementSettings().setRotation(Rotation.randomRotation(random)));
    }

    private DungeonChunk(int variant, IChunk chunk, Random random, GenerationContext ctx, PlacementSettings placement) {
        this.variant = variant;
        this.chunk = chunk;
        this.random = random;
        this.ctx = ctx;
        this.placement = placement;
    }

    public DungeonChunk with(Rotation rotation) {
        return new DungeonChunk(variant, chunk, random, ctx, placement.setRotation(rotation));
    }

    public ChunkPos getPos() {
        return this.chunk.getPos();
    }

    private static BlockState rotateProperty(BlockState state, IProperty<?> property, Rotation rotation) {
        Class clazz = property.getValueClass();
        if(clazz.isInstance(Rotation.NONE)) {

            IProperty<Rotation> p = (IProperty<Rotation>) property;
            Rotation r = state.get(p).add(rotation);
            if(p.getAllowedValues().contains(r)) return state.with(p, r);

        } else if(clazz.isInstance(Direction.DOWN)) {

            IProperty<Direction> p = (IProperty<Direction>) property;
            Direction d = state.get(p);
            for(int i = 0; i < rotation.ordinal(); i++) d = d.rotateAround(Direction.Axis.Y);
            if(p.getAllowedValues().contains(d)) return state.with(p, d);

        } else if(clazz.isInstance(Direction.Axis.X) && rotation != Rotation.NONE && rotation != rotation.CLOCKWISE_180) {

            IProperty<Direction.Axis> p = (IProperty<Direction.Axis>) property;
            Direction.Axis a = state.get(p);
            switch (a) {
                case X:
                    a = Direction.Axis.Z;
                    break;
                case Z:
                    a = Direction.Axis.X;
                    break;
            }
            if (p.getAllowedValues().contains(a)) return state.with(p, a);

        }

        return state;
    }

    public void setTileEntity(BlockPos pos, CompoundNBT nbt) {
        this.setTileEntity(pos, nbt, 1);
    }

    public void setTileEntity(BlockPos pos, CompoundNBT nbt, int size) {
        Rotation rotation = pos.getX() * pos.getZ() == 0 ? Rotation.NONE : this.placement.getRotation();
        BlockPos rotated = this.rotate(pos, rotation, size);
        BlockPos real = getPos().asBlockPos().add(rotated);

        nbt.putInt("x", real.getX());
        nbt.putInt("y", real.getY());
        nbt.putInt("z", real.getZ());

        nbt.putString("LootTable", DungeonLoot.Rarity.COMMON.path().toString());

        chunk.addTileEntity(nbt);
    }

    public BlockState setBlockState(BlockPos pos, BlockState state, Rotation rotation) {
        if(pos.getX() * pos.getZ() == 0 && rotation != Rotation.NONE) setBlockState(pos, state, Rotation.NONE);

        if(ctx.settings.replacePlaceholders && state.getBlock() instanceof IPlaceholder) {

            Type type = ((IPlaceholder) state.getBlock()).getType();
            BlockState replace = ctx.palette.blockFor(type, random, variant);

            BlockState applied =
                    state.getProperties()
                            .stream()
                            .reduce(replace, (s, p) -> s.has(p) ? s.with((IProperty) p, state.get(p)) : s, (a, b) -> a);

            return this.setBlockState(pos, applied);

        } else {

            /*BlockState rotatedState =
                    state.getProperties()
                            .stream()
                            .reduce(state, (s, p) -> rotateProperty(s, p, rotation), (a, b) -> a);
*/
            BlockState rotatedState = state.rotate(rotation);
            BlockPos rotated = this.rotate(pos, rotation, 1);
            return chunk.setBlockState(rotated, rotatedState, false);
        }
    }

    private BlockPos rotate(BlockPos in, Rotation rotation, int size) {
        float phi = (float) (rotation.ordinal() * Math.PI / 2);
        int sin = (int) MathHelper.sin(phi);
        int cos = (int) MathHelper.cos(phi);

        int center = size * 8;
        float[] centered = new float[]{in.getX() - center, in.getZ() - center};

        return new BlockPos(
                (int) (centered[0] * cos - centered[1] * sin + center),
                in.getY() + ctx.getFloor() * (DungeonSettings.FLOOR_HEIGHT + 1),
                (int) (centered[0] * sin + centered[1] * cos + center)
        );
    }

    public BlockState setBlockState(BlockPos pos, BlockState state) {
        return setBlockState(pos, state, this.placement.getRotation());
    }

    public BlockState getBlockState(BlockPos pos) {
        return chunk.getBlockState(rotate(pos, this.placement.getRotation(), 1));
    }
}
