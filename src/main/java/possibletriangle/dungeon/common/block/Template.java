package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;

import java.util.function.Function;

public enum Template {

    FLOOR,
    WALL,
    RUNE,
    GEM,
    STAIRS(TemplateStairs::new),
    SLAB(TemplateSlab::new),
    FENCE,
    FLUID_SAFE,
    FLUID_UNSAFE,
    PRESSURE_PLATE,
    BUTTON,
    LEVER,
    GLASS,
    LAMP,
    CHEST,
    FALLING_BLOCK;

    private final Function<Template, Block> block;

    public Block block() {
        return this.block.apply(this);
    }

    Template(Function<Template, Block> block) {
        this.block = block;
    }

    Template() {
        this(TemplateBlock::new);
    }

}
