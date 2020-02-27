package possibletriangle.dungeon.world.generator;

import net.minecraft.world.gen.GenerationSettings;

public class DungeonSettings extends GenerationSettings {

    public static final int FLOOR_HEIGHT = 10;
    public final int floors = 3;

    public final boolean hasCeiling = false;
    public final boolean replacePlaceholders = true;

}
