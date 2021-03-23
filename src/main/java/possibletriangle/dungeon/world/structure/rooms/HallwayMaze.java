package possibletriangle.dungeon.world.structure.rooms;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import possibletriangle.dungeon.block.placeholder.TemplateBlock;
import possibletriangle.dungeon.block.placeholder.TemplateType;
import possibletriangle.dungeon.world.generator.DungeonChunk;
import possibletriangle.dungeon.world.generator.DungeonSettings;
import possibletriangle.dungeon.world.generator.GenerationContext;
import possibletriangle.dungeon.world.structure.IStructure;
import possibletriangle.dungeon.world.structure.metadata.StructureMetadata;

import java.util.Random;

public class HallwayMaze extends IStructure {

    private final float closeChance;
    private final float lavaChance;
    private final float wallChance;

    public HallwayMaze() {
        this(0.25F, 0.08F, 0.9F);
    }

    public HallwayMaze(float closeChance, float lavaChance, float wallChance) {
        this.closeChance = closeChance;
        this.lavaChance = lavaChance;
        this.wallChance = wallChance;
    }

    @Override
    public StructureMetadata getMeta() {
        return new StructureMetadata(1, "Maze Hallway");
    }

    /**
     *  Generates a maze filling out a 13x13 space
     */
    @Override
    public void generate(DungeonChunk chunk, Random random, GenerationContext ctx, BlockPos at) {

        this.generateFloor(chunk, ctx.settings);
        BlockState wall = TemplateType.WALL.getBlock().getDefaultState();
        BlockState lava = Blocks.LAVA.getDefaultState();
        Vector3i size = this.getSize();

        for (int x = 2; x < 16 * size.getX() - 1; x++)
            for (int z = 2; z < 16 * size.getZ() - 1; z++) {

                boolean even = x % 2 == 0 && z % 2 == 0;
                boolean odd = (x % 2) * (z % 2) == 0;
                boolean close = even || (odd && random.nextFloat() <= closeChance);
                boolean setLava = odd && random.nextFloat() <= lavaChance;

                for (int y = 2; y < DungeonSettings.FLOOR_HEIGHT; y++)
                    if (close) chunk.setBlockState(new BlockPos(x, y, z), setLava ? lava : wall);

            }

        for(Rotation rotation : Rotation.values())
            for(int z : new int[]{ 5, 11 }) {

                boolean gen = random.nextFloat() <= wallChance;
                for (int y = 3; y < DungeonSettings.FLOOR_HEIGHT; y++) {

                    chunk.setBlockState(new BlockPos(1, y, z), wall, rotation);
                    if(gen) chunk.setBlockState(new BlockPos(2, y, z), wall, rotation);

                }

                /* TODO I don't think torches generate yet, look into this */
                if(!gen) chunk.setBlockState(new BlockPos(2, 5, z), Blocks.TORCH.getDefaultState(), rotation);

            }
    }
}
