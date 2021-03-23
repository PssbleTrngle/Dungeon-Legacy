package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

import java.util.function.Function;

public class TemplateBlock extends Block implements IPlaceholder {

    private final TemplateType type;

    public static Properties PROPERTIES() {
        return Properties.create(Material.ROCK)
                .sound(SoundType.STONE)
                .hardnessAndResistance(1000.0F)
                .noDrops();
    }

    public TemplateBlock(TemplateType type) {
        this(type, 0);
    }

    public TemplateBlock(TemplateType type, int light) {
        super(PROPERTIES().func_235838_a_($ -> light));
        this.type = type;
        setRegistryName("placeholder_" + type.name().toLowerCase());
    }

    public static <T extends Block> Function<TemplateType<T>, Block> glowing(int light) {
        return type -> new TemplateBlock(type, light);
    }

    @Override
    public TemplateType getType() {
        return this.type;
    }
}
