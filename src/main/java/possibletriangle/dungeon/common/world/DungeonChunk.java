package possibletriangle.dungeon.common.world;

import net.minecraft.block.BlockState;
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

import java.util.Random;

public class DungeonChunk {

    private final IChunk chunk;
    private final Random random;
    private int floor = 0;
    private final DungeonSettings settings;
    private final PlacementSettings placement;
    private final Palette palette;
    private int variant;

    void setFloor(int floor) {
        this.floor = floor;
    }

    public DungeonChunk(IChunk chunk, Random random, DungeonSettings settings) {
        this.variant = random.nextInt(32);
        this.chunk = chunk;
        this.random = random;
        this.settings = settings;
        this.palette = Palette.random(random);
        this.placement = new PlacementSettings().setRotation(Rotation.func_222466_a(random));
        //this.palette = Palette.random(random);
        //this.palette = getPos().x % 2 == getPos().z % 2 ? Palette.NATURE : Palette.NETHER;
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

        } else if(clazz.isInstance(Direction.Axis.X)) {

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



        chunk.addTileEntity(nbt);
    }

    public BlockState setBlockState(BlockPos pos, BlockState state, Rotation rotation, int size) {
        if(pos.getX() * pos.getZ() == 0 && rotation != Rotation.NONE) setBlockState(pos, state, Rotation.NONE, size);

        if(settings.replacePlaceholders && state.getBlock() instanceof IPlaceholder) {

            Type type = ((IPlaceholder) state.getBlock()).getType();
            BlockState replace = palette.blockFor(type, random, variant);

            BlockState applied =
                    state.getProperties()
                            .stream()
                            .reduce(replace, (s, p) -> s.has(p) ? s.with((IProperty) p, state.get(p)) : s, (a, b) -> a);

            return this.setBlockState(pos, applied);

        } else {

            BlockState rotatedState =
                    state.getProperties()
                            .stream()
                            .reduce(state, (s, p) -> rotateProperty(s, p, rotation), (a, b) -> a);

            BlockPos rotated = this.rotate(pos, rotation, size);
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
                in.getY() + floor * (DungeonSettings.FLOOR_HEIGHT + 1),
                (int) (centered[0] * sin + centered[1] * cos + center)
        );
    }

    public BlockState setBlockState(BlockPos pos, BlockState state, int size) {
        return setBlockState(pos, state, this.placement.getRotation(), size);
    }

    public BlockState setBlockState(BlockPos pos, BlockState state, Rotation rotation) {
        return setBlockState(pos, state, rotation, 1);
    }

    public BlockState setBlockState(BlockPos pos, BlockState state) {
        return setBlockState(pos, state, this.placement.getRotation());
    }

    public BlockState getBlockState(BlockPos pos) {
        return chunk.getBlockState(pos);
    }
}
