package possibletriangle.dungeon.world.structure;

import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.*;
import possibletriangle.dungeon.DungeonMod;

import java.util.Collection;
import java.util.function.Predicate;

@ObjectHolder(DungeonMod.ID)
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

    /**
     * A substructure part
     */
    @ObjectHolder("part")
    public static final StructureType PART = null;

    public static Predicate<IStructure> hasSize(int x, int y, int z) {
        return structure -> {
            Vec3i size = structure.getActualSize();
            return size.getX() == x && size.getZ() == z && size.getY() == y;
        };
    }

    public static boolean validRoom(IStructure structure) {
        Vec3i size = structure.getActualSize();
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
        if(customFolder != null) return customFolder;
        return this.getRegistryName().getPath();
    }


}