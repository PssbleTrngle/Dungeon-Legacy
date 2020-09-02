package possibletriangle.dungeon.world.structure;

import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.*;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.world.generator.DungeonSettings;

import java.util.Collection;
import java.util.function.Predicate;

import static possibletriangle.dungeon.DungeonMod.STRUCTURES;

@ObjectHolder(DungeonMod.ID)
public class StructureType extends ForgeRegistryEntry<StructureType> {

    /**
     * Every black field of a chessboard
     */
    public static final RegistryObject<StructureType> ROOM = STRUCTURES.register("room", () -> new StructureType(StructureType::validRoom));
    /**
     * Every white field of a chessboard
     */
    public static final RegistryObject<StructureType> HALLWAY = STRUCTURES.register("hallway", () -> new StructureType(StructureType::validRoom));
    /**
     * The doors used to be randomly placed at the room walls
     */
    public static final RegistryObject<StructureType> BIG_DOOR = STRUCTURES.register("door/big", () -> new StructureType(StructureType.hasSize(2, 5, 5)));

    /**
     * The doors used to be randomly placed at the room walls
     */
    public static final RegistryObject<StructureType> SMALL_DOOR = STRUCTURES.register("door/small", () -> new StructureType(StructureType.hasSize(2, 5, 4)));

    /**
     * Rare rooms containing a boss enemy
     * Spawning at a minimum distance from other boss rooms and the world spawn
     */
    public static final RegistryObject<StructureType> BOSS = STRUCTURES.register("boss", () -> new StructureType(StructureType::validRoom));
    /**
     * Spawning at a minimum distance
     */
    public static final RegistryObject<StructureType> BASE = STRUCTURES.register("base", () -> new StructureType(StructureType::validRoom));

    /**
     * A substructure part
     */
    public static final RegistryObject<StructureType> PART = STRUCTURES.register("part", () -> new StructureType(s -> true));

    public static final RegistryObject<StructureType> SHOP = STRUCTURES.register("shop", () -> new StructureType(StructureType.hasSize(5, DungeonSettings.FLOOR_HEIGHT, 15)));

    public static Predicate<IStructure> hasSize(int x, int y, int z) {
        return structure -> {
            Vector3i size = structure.getActualSize();
            return size.getX() == x && size.getZ() == z && size.getY() == y;
        };
    }

    public static boolean validRoom(IStructure structure) {
        Vector3i size = structure.getActualSize();
        boolean x = (size.getZ() - 15) % 16 == 0;
        boolean z = (size.getZ() - 15) % 16 == 0;
        return x && z && structure.getSize().getY() > 0;
    }

    final Predicate<IStructure> valid;
    private final String customFolder;

    public static Collection<StructureType> values() {
        return GameRegistry.findRegistry(StructureType.class).getValues();
    }

    public StructureType(Predicate<IStructure> valid, String customFolder) {
        this.valid = valid;
        this.customFolder = customFolder;
    }

    public StructureType(Predicate<IStructure> valid) {
        this(valid, null);
    }

    public String folder() {
        if (customFolder != null) return customFolder;
        return this.getRegistryName().getPath();
    }


}