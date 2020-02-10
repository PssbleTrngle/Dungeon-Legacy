package possibletriangle.dungeon.common.block.placeholder;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

@ObjectHolder(DungeonMod.ID)
public class TemplateBlock extends Block implements IPlaceholder {

    @ObjectHolder("placeholder_pillar")
    public static final Block PILLAR = null;

    @ObjectHolder("placeholder_floor")
    public static final Block FLOOR = null;

    @ObjectHolder("placeholder_wall")
    public static final Block WALL = null;

    private final Type type;

    public static Properties PROPERTIES() {
        return Properties.create(Material.ROCK)
                .sound(SoundType.STONE)
                .hardnessAndResistance(1000.0F)
                .noDrops();
    }

    public TemplateBlock(Type type) {
        this(type, 0);
    }

    public TemplateBlock(Type type, int light) {
        super(PROPERTIES().lightValue(light));
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
