package possibletriangle.dungeon.rooms;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import possibletriangle.dungeon.pallete.Pallete;

public class RoomData {

    public final ResourceLocation name;
    public final Rotation rotation;
    public final Pallete pallete;

    public RoomData(ResourceLocation name, Rotation rotation, Pallete pallete) {
        this.name = name;
        this.rotation = rotation;
        this.pallete = pallete;
    }

}
