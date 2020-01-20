package possibletriangle.dungeon.common.world.room;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.room.StateProvider;
import possibletriangle.dungeon.helper.BlockCollection;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ObjectHolder(DungeonMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class StructureType extends ForgeRegistryEntry<Palette> {

    /**
     * Every black field of a chessboard
     */
    @ObjectHolder("room")
    public static final ROOM(Type::validRoom) = null;
    /**
     * Every white field of a chessboard
     */
    @ObjectHolder("hallway")
    public static final HALLWAY(Type::validRoom) = null;
    /**
     * The doors used to be randomly placed at the room walls
     */
    @ObjectHolder("door")
    public static final DOOR(s -> true) = null;
    /**
     * Rare rooms containing a boss enemy
     * Spawning at a minimum distance from other boss rooms and the world spawn
     */
    @ObjectHolder("boss")
    public static final BOSS(Type::validRoom) = null;
    /**
     * Spawning at a minimum distance
     */
    @ObjectHolder("base")
    public static final BASE(Type::validRoom) = null;

    static boolean validRoom(Generateable structure) {
        Vec3i size = structure.getActualSize();
        int floorHeight = DungeonSettings.FLOOR_HEIGHT;
        return size.getX() % 16 == 0 && size.getZ() % 16 == 0
                && (size.getY() - floorHeight) % (floorHeight + 1) == 0;
    }

    public final Predicate<Generateable> valid;
    private final customFolder;

    public StructureType(Predicate<Generateable> valid, String customFolder) {
        this.valid = valid;
        this.customFolder = customFolder;
    }

    public StructureType(Predicate<Generateable> valid) {
        this(valid, null);
    }

    public String folder() {
        if(customFolder != null) return customFolder;
        return this.getRegistryName().getPath();
    }


}