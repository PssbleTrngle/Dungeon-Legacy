package possibletriangle.dungeon.generator.rooms;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import possibletriangle.dungeon.generator.*;
import possibletriangle.dungeon.helper.RandomCollection;
import possibletriangle.dungeon.structures.DungeonStructur;

import java.util.Random;

public class RoomShop extends RoomStructure {

    private final RandomCollection<DungeonStructur> SHOPS = new RandomCollection<>();

    public RoomShop() {
        super("room", "shop");
    }

    @Override
    public void generateAt(DungeonOptions options, ChunkPrimerDungeon primer, int floor, Random r, Rotation rotation) {
        super.generateAt(options, primer, floor, r, rotation);

        SHOPS.next(r).generate(primer, options, floor, null, rotation, false, new BlockPos(10, 0, 1));

    }

    public RoomShop add(String shop_name, double weight) {
        SHOPS.add(weight, new DungeonStructur("room/shop/" + shop_name));
        return this;
    }

    @Override
    public void populate(DungeonOptions options, World world, int chunkX, int chunkZ, int floor, Random r) {

        Rotation rotation = WorldDataRooms.atFloor(chunkX, floor, chunkZ, world).rotation;

        BlockPos pos = ChunkPrimerRotateable.rotate(new BlockPos(12, 3, 7), rotation, new double[]{15 / 2.0, 15 / 2.0});
        pos = pos.add(chunkX*16, floor * DungeonOptions.FLOOR_HEIGHT, chunkZ*16);

        EntityVillager villager = new EntityVillager(world);

        villager.setPosition(pos.getX(), pos.getY(), pos.getZ());

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("NoAI", true);
        nbt.setBoolean("Invulnerable", true);
        villager.readEntityFromNBT(nbt);

        switch (rotation) {
            case COUNTERCLOCKWISE_90:
                villager.rotationYaw = 0;
                break;
            case NONE:
                villager.rotationYaw = 90;
                break;
            case CLOCKWISE_180:
                villager.rotationYaw = 270;
                break;
            case CLOCKWISE_90:
                villager.rotationYaw = 180;
                break;
        }

        world.spawnEntity(villager);

    }
}
