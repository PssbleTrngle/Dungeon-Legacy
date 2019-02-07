package possibletriangle.dungeon.rooms.quarter;

import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.chunk.ChunkPrimer;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;

public class RoomQuarterColor extends RoomQuarter {

    public final EnumDyeColor color;

    public RoomQuarterColor(EnumDyeColor color) {
        this.color = color;
    }

    @Override
    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, int[] offset) {

        IBlockState concrete = Blocks.CONCRETE.getDefaultState().withProperty(BlockConcretePowder.COLOR, color);

        for(int x = 0; x < 8; x++)
            for(int z = 0; z < 8; z++)
                primer.setBlockState(x + offset[0]*8, 0, z + offset[1]*8, floor, concrete);

    }
}
