package possibletriangle.dungeon.generator;

import net.minecraft.block.BlockSlab;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.placeholder.BlockSlabPlaceholder;
import possibletriangle.dungeon.block.ModBlocks;
import possibletriangle.dungeon.pallete.Pallete;
import possibletriangle.dungeon.rooms.Room;
import possibletriangle.dungeon.rooms.RoomManager;
import possibletriangle.dungeon.rooms.RoomSpawn;
import possibletriangle.dungeon.rooms.wall.Wall;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkGeneratorDungeon implements IChunkGenerator {

    private final DungeonOptions options;
    private final World world;
    private final Random r;


    public ChunkGeneratorDungeon(DungeonOptions options, World world, Random r) {
        this.options = options;
        this.world = world;
        this.r = r;
    }


    @Override
    public Chunk generateChunk(int chunkX, int chunkZ) {

        WorldDataRooms.setFloorHeight(options.floorHeight, world);
        Rotation rotation = Rotation.values()[(int) Math.floor(Rotation.values().length * r.nextDouble())];
        ChunkPrimerDungeon primer = new ChunkPrimerDungeon(rotation, options, r);

        BlockPos spawn = world.getSpawnPoint();
        boolean isSpawn = spawn.getX() / 16 == chunkX && spawn.getZ() / 16 == chunkZ;

        Room[] above = new Room[options.floorCount];
        Pallete[] above_pallete = new Pallete[options.floorCount];
        for(int floor = 0; floor < options.floorCount; floor++) {

            Room room;
            Pallete pallete;

            if(floor > 0 && above[floor] != null) {
                room = above[floor];
                pallete = above_pallete[floor];
            }
            else {
                if (isSpawn && floor == 0) {
                    room = RoomSpawn.SPAWN;
                    Dungeon.LOGGER.info("Spawn Room generated atY {}/{}", chunkX*16, chunkZ*16);
                } else
                    room = RoomManager.randomFor(floor, options, r);

                pallete = Pallete.random(r);
            }

            primer.set(floor, pallete);

            for(int a = 1; options.floorCount > floor+a; a++) {
                above[floor + a] = room.roomAbove(a, r);
                above_pallete[floor + a] = pallete;
            }

            room.generateAt(options, primer, floor, r);
            if(room.generateWall())
                Wall.random(r).generateAt(options, primer, floor, r);
            if(!room.noCeiling()) {
                for(int x = 0; x < 16; x++)
                    for(int z = 0; z < 16; z++)
                        primer.setBlockState(x, options.floorHeight-1, z, floor, ModBlocks.SLAB.getDefaultState().withProperty(BlockSlabPlaceholder.HALF, BlockSlab.EnumBlockHalf.TOP), true);
            }

            WorldDataRooms.put(chunkX, floor, chunkZ, room.getName(), world);

        }

        if(options.hasCeiling)
            for(int x = 0; x < 16; x++)
                for(int z = 0; z < 16; z++)
                    primer.setBlockState(x, 0, z, options.floorCount, ModBlocks.FLOOR.getDefaultState());

        Chunk chunk = new Chunk(world, primer, chunkX, chunkZ);
        chunk.generateSkylightMap();

        return chunk;
    }

    @Override
    public void populate(int x, int z) {

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return true;
    }
}
