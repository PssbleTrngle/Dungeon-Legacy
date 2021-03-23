package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.BlockState;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.tile.TrappedChestTile;

import javax.annotation.Nullable;

@ObjectHolder(DungeonMod.ID)
public class PlaceholderTrappedChest extends PlaceholderChest {

    public PlaceholderTrappedChest(TemplateType type) {
        super(type, TrappedChestTile.TYPE);
    }

	@Override
	public TileEntity createNewTileEntity(@Nullable IBlockReader world) {
		return new TrappedChestTile();
    }

    protected Stat<ResourceLocation> getOpenStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    public boolean canProvidePower(BlockState state) {
        return true;
    }

    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return MathHelper.clamp(ChestTileEntity.getPlayersUsing(world, pos), 0, 15);
    }

    public int getStrongPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return side == Direction.UP ? state.getWeakPower(world, pos, side) : 0;
    }

}