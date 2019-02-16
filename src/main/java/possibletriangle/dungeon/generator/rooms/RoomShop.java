package possibletriangle.dungeon.generator.rooms;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import possibletriangle.dungeon.generator.ChunkPrimerDungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.generator.RandomCollection;
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

}
