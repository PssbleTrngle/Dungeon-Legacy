package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;

import java.util.function.Function;

public enum Type {

    FLOOR,
    WALL,
    PATH,
    RUNE,
    GEM,
    PLANKS,
    GLASS(t -> new TemplateTransparent(t, true)),
    FALLING(TemplateFalling::new),
    PILLAR(TemplatePillar::new),
    SEAL,

    GRASS(TemplateGrass::new),
    LEAVES(TemplateTransparent::new),
    LOG(TemplatePillar::new),

    STAIRS(TemplateStairs::new),
    SLAB(TemplateSlab::new),
    STAIRS_WALL(TemplateStairs::new),
    SLAB_WALL(TemplateSlab::new),

    FENCE,
    FLUID_SAFE,
    FLUID_UNSAFE,
    PRESSURE_PLATE,
    BUTTON,
    LEVER,
    LAMP(t -> new TemplateBlock(t, 15)),
    CHEST;

    private final Function<Type, Block> block;

    public Block block() {
        return this.block.apply(this);
    }

    Type(Function<Type, Block> block) {
        this.block = block;
    }

    Type() {
        this(TemplateBlock::new);
    }

}
