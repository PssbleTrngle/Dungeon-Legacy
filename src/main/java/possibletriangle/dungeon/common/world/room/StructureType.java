package possibletriangle.dungeon.common.world.room;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.*;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.StateProvider;
import possibletriangle.dungeon.helper.BlockCollection;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ObjectHolder(DungeonMod.MODID)
public class StructureType extends ForgeRegistryEntry<StructureType> {

    /**
     * Every black field of a chessboard
     */
    @ObjectHolder("room")
    public static final StructureType ROOM = null;
    /**
     * Every white field of a chessboard
     */
    @ObjectHolder("hallway")
    public static final StructureType HALLWAY = null;
    /**
     * The doors used to be randomly placed at the room walls
     */
    @ObjectHolder("door/big")
    public static final StructureType BIG_DOOR = null;
    @ObjectHolder("door/small")
    public static final StructureType SMALL_DOOR = null;
    /**
     * Rare rooms containing a boss enemy
     * Spawning at a minimum distance from other boss rooms and the world spawn
     */
    @ObjectHolder("boss")
    public static final StructureType BOSS = null;
    /**
     * Spawning at a minimum distance
     */
    @ObjectHolder("base")
    public static final StructureType BASE = null;

    public static Predicate<Generateable> hasSize(int x, int y, int z) {
        return structure -> {
            Vec3i size = structure.getActualSize();
            return size.getX() == x && size.getZ() == z && size.getY() == y;
        };
    }

    public static boolean validRoom(Generateable structure) {
        Vec3i size = structure.getActualSize();
        boolean x = (size.getZ() - 15) % 16 == 0;
        boolean z = (size.getZ() - 15) % 16 == 0;
        return x && z && structure.getSize().getY() > 0;
    }

    final Predicate<Generateable> valid;
    private final String customFolder;

    public static Collection<StructureType> values() {
        return GameRegistry.findRegistry(StructureType.class).getValues();
    }

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