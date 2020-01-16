package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.common.block.TemplateBlock;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.structure.StructureMetadata;

import java.util.Random;

public class HallwayMaze implements Generateable {

    private final float closeChance;
    private final float lavaChance;
    private final float wallChance;

    public HallwayMaze() {
        this(0.25F, 0.05F, 0.9F);
    }

    public HallwayMaze(float closeChance, float lavaChance, float wallChance) {
        this.closeChance = closeChance;
        this.lavaChance = lavaChance;
        this.wallChance = wallChance;
    }

    @Override
    public StructureMetadata getMeta() {
        return new StructureMetadata(1);
    }

    /**
     * 
     *  Generates a maze filling out a 13x13 space
     * 
     *      ───── LAYOUT ─────
     *          Even    E
     *          Odd     O
     *          Wall    #
     *          Torch   *
     *      ──────────────────
     * 
     *    0  │# #     # # #       # # #     #│#
     *    ───┼───────────────────────────────┼──────
     *    F  │#         *           #        │#
     *    E  │    E O E # E O E   E # E   E  │
     *    D  │    O       O           O   O  │
     *    C  │#   E O E   E O E O E   E   E  │#
     *    B  │# # #       O   O           # #│#
     *    A  │#   E O E   E   E   E   E   E  │#
     *    9  │                        O   O  │
     *    8  │    E   E O E O E   E   E   E  │
     *    7  │        O           O          │
     *    6  │#   E   E   E O E   E O E   E  │#
     *    5  │# # #   O       O   O       # *│#
     *    4  │#   E O E   E O E   E   E   E  │#
     *    3  │                               │
     *    2  │    E   E # E   E   E # E   E  │
     *    1  │#         #           #        │#
     *    0  │# #     # # #       # # #     #│#
     *    ───┼───────────────────────────────┼──────
     *    F  │0 1 2 3 4 5 6 7 8 9 A B C D E F│0
     */
    @Override
    public void generate(DungeonChunk chunk, Random random, GenerationContext context) {

        this.generateFloor(chunk);
        BlockState wall = TemplateBlock.WALL.getDefaultState();
        BlockState lava = Blocks.LAVA.getDefaultState();

        for (int x = 2; x < 15; x++)
            for (int z = 2; z < 15; z++) {

                boolean even = x % 2 == 0 && z % 2 == 0;
                boolean odd = (x % 2) * (z % 2) == 0);
                boolean close = even || (odd && random.next() <= closeChance);
                boolean setLava = close && random.next <= lavaChance;

                for (int y = 2; y < context.settings.floorHeight; y++)
                    if (close) chunk.setBlockState(new BlockPos(x, y, z), setLava ? wall : lava);

            }

        for(Rotation rotation : Rotation.values())
            for(int z : new int[]{ 5, 11 }) {

                boolean gen = random.next() <= wallChance;
                for (int y = 3; y < context.settings.floorHeight; y++) {

                    chunk.setBlockState(new BlockPos(1, y, z), wall);
                    if(gen) chunk.setBlockState(new BlockPos(2, y, z), wall);

                }

                if(!gen) chunk.setBlockState(new BlockPos(2, 5, z), Blocks.TORCH.getDefaultState());

            }
    }
}
