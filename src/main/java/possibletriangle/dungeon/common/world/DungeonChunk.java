package possibletriangle.dungeon.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.state.IProperty;
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

    /**
     * Place a TileEntity at the given position
     * Will spawn loot for chest and insert spawn potentials for spawners 
     * 
     * @param pos The not yet rotated position
     * @param nbt The Tile's NBT Data
     */
    public void setTileEntity(BlockPos pos, CompoundNBT nbt) {
        Rotation rotation = pos.getX() * pos.getZ() == 0 ? Rotation.NONE : this.placement.getRotation();
        BlockPos rotated = rotate(pos, rotation);
        BlockPos real = getPos().asBlockPos().add(rotated);

        nbt.putInt("x", real.getX());
        nbt.putInt("y", real.getY());
        nbt.putInt("z", real.getZ());

        String type = nbt.getString("id");

        switch (type) {
            case "minecraft:mob_spawner":
                nbt.putInt("MaxNearbyEntities", 6);
                nbt.putInt("RequiredPlayerRange", 10);
                nbt.putInt("SpawnCount", 4);
                nbt.putInt("MaxSpawnDelay", 800);
                nbt.putInt("MinSpawnDelay", 200);
                nbt.putInt("SpawnRange", 3);
                nbt.putInt("SpawnRange", 3);

                CompoundNBT data = new CompoundNBT();
                data.putString("id", ctx.palette.randomMob(random).toString());
                nbt.put("SpawnData", data);

                ListNBT potentials = new ListNBT();
                CompoundNBT entry = new CompoundNBT();
                entry.put("Entity", data);
                entry.putInt("Weight", 1);
                potentials.add(entry);
                nbt.put("SpawnPotentials", potentials);
                break;

            case "minecraft:chest":
                nbt.putString("LootTable", DungeonLoot.Rarity.COMMON.path().toString());
                nbt.putLong("LootTableSeed", random.nextLong());
                break;

        }

        chunk.addTileEntity(nbt);
    }

    /**
     * Retrieve the block at a specific position
     * Will rotate about the rotation of this chunk
     * @param pos The position
     */
    public BlockState getBlockState(BlockPos pos) {
        return chunk.getBlockState(rotate(pos, this.placement.getRotation()));
    }

    public BlockState setBlockState(BlockPos pos, BlockState state) {
        return setBlockState(pos, state, this.placement.getRotation());
    }

    public BlockState setBlockState(BlockPos pos, BlockState state, Rotation rotation) {
        if(pos.getX() * pos.getZ() == 0 && rotation != Rotation.NONE) setBlockState(pos, state, Rotation.NONE);

        if(ctx.settings.replacePlaceholders && state.getBlock() instanceof IPlaceholder) {

            Type type = ((IPlaceholder) state.getBlock()).getType();
            BlockState replace = ctx.palette.blockFor(type, random, variant);

            BlockState applied = state.getProperties()
                .stream()
                .reduce(replace, (s, p) -> s.has(p) ? s.with((IProperty) p, state.get(p)) : s, (a, b) -> a);

            return this.setBlockState(pos, applied);

        } else {

            BlockState rotatedState = state.rotate(rotation);
            BlockPos rotated = this.rotate(pos, rotation);
            return chunk.setBlockState(rotated, rotatedState, false);
        }
    }

    /**
     * Rotate a BlockPos around the center of a room
     * @param in The BlockPos to rotate
     * @param size The size of the room in chunks
     * @param rotation The rotation
     * @return A rotated BlockPos
     */
    public BlockPos rotate(BlockPos in, Rotation rotation) {
        float phi = (float) (rotation.ordinal() * Math.PI / 2);
        int sin = (int) MathHelper.sin(phi);
        int cos = (int) MathHelper.cos(phi);

        int center = ctx.getSize() * 8;
        float[] centered = new float[]{in.getX() - center, in.getZ() - center};

        return new BlockPos(
                (int) (centered[0] * cos - centered[1] * sin + center),
                in.getY() + ctx.getFloor() * (DungeonSettings.FLOOR_HEIGHT + 1),
                (int) (centered[0] * sin + centered[1] * cos + center)
        );
    }
}
