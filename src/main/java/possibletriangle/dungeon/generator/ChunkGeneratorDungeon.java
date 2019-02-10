package possibletriangle.dungeon.generator;

import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.EnumFaceDirection;
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
import possibletriangle.dungeon.rooms.RoomData;
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
        ChunkPrimerDungeon primer = new ChunkPrimerDungeon(options, r);

        BlockPos spawn = world.getSpawnPoint();

        for(int floor = 0; floor < options.floorCount; floor++) {
            boolean isSpawn = spawn.getX() / 16 == chunkX && spawn.getZ() / 16 == chunkZ && floor == 0;

            RoomData data = WorldDataRooms.atFloor(chunkX, floor, chunkZ, world);

            if(data == null) {
                Rotation rot = Rotation.values()[(int) Math.floor(Rotation.values().length * r.nextDouble())];
                Room room = (isSpawn ? RoomSpawn.SPAWN : RoomManager.randomFor(floor, options, r, chunkX, chunkZ, rot, world));
                data = new RoomData(room.getName(), rot, Pallete.random(r));
                WorldDataRooms.put(chunkX, floor, chunkZ, room.getName(), data.rotation, data.pallete, world, "random");
            }

            primer.set(floor, data.pallete);

            Room room = RoomManager.get(data.name);
            if(room != null) {

                for(int floorsAbove = 1; options.floorCount > floor+floorsAbove; floorsAbove++) {
                    Room roomAbove = room.roomAbove(floorsAbove, r);
                    if(roomAbove != null)
                        WorldDataRooms.put(chunkX, floor + floorsAbove, chunkZ, roomAbove.getName(), data.rotation, data.pallete, world, "above");
                }

                for(EnumFaceDirection side : new EnumFaceDirection[]{EnumFaceDirection.SOUTH, EnumFaceDirection.WEST, EnumFaceDirection.NORTH, EnumFaceDirection.EAST}) {
                    Room roomAt = room.roomAt(side, data.rotation, r);

                    int[] i = new int[2];
                    switch(side) {
                        case SOUTH:
                            i = new int[]{0, -1};
                            break;
                        case WEST:
                            i = new int[]{-1, 0};
                            break;
                        case NORTH:
                            i = new int[]{0, 1};
                            break;
                        case EAST:
                            i = new int[]{1, 0};
                            break;
                    }

                    if(roomAt != null)
                        WorldDataRooms.put(chunkX + i[0], floor, chunkZ + i[1], roomAt.getName(), data.rotation, data.pallete, world, "side");
                }

                room.generateAt(options, primer, floor, r, data.rotation);
                if (room.generateWall())
                    Wall.random(r).generateAt(options, primer, floor, r);
                if (!room.noCeiling()) {
                    for (int x = 0; x < 16; x++)
                        for (int z = 0; z < 16; z++)
                            primer.setBlockState(x, options.floorHeight - 1, z, floor, ModBlocks.SLAB.getDefaultState().withProperty(BlockSlabPlaceholder.HALF, BlockSlab.EnumBlockHalf.TOP), data.rotation, true);
                }

            }

        }

        if(options.hasCeiling)
            for(int x = 0; x < 16; x++)
                for(int z = 0; z < 16; z++)
                    primer.setBlockState(x, 0, z, options.floorCount, Rotation.NONE, ModBlocks.FLOOR.getDefaultState());

        Chunk chunk = new Chunk(world, primer, chunkX, chunkZ);
        chunk.generateSkylightMap();

        return chunk;
    }

    @Override
    public void populate(int chunkX, int chunkZ) {

        for(int floor = 0; floor < options.floorCount; floor++) {
            RoomData data = WorldDataRooms.atFloor(chunkX, floor, chunkZ, world);
            if(data != null) {
                Room room = RoomManager.get(data.name);
                if(room != null) {
                    room.populate(options, world, chunkX, chunkZ, floor, r);
                }
            }
        }

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
        return false;
    }
}
