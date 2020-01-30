package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;

import java.util.function.Function;

public enum Type {

    FLOOR,
    WALL,
    PATH,
    RUNE,
    GEM,
    SEAL,
    GLASS(t -> new TemplateTransparent(t, true)),
    FALLING(TemplateFalling::new),
    PILLAR(TemplatePillar::new),
    LAMP(t -> new TemplateBlock(t, 15)),

    STAIRS(TemplateStairs::new),
    SLAB(TemplateSlab::new),
    STAIRS_WALL(TemplateStairs::new),
    SLAB_WALL(TemplateSlab::new),
    
    PLANKS,
    STAIRS_WOOD(TemplateStairs::new),
    SLAB_WOOD(TemplateSlab::new),
    LEAVES(TemplateTransparent::new),
    STRIPPED_LOG(TemplatePillar::new),
    LOG(TemplatePillar::new),

    DIRT(TemplateGrass::new),
    GRASS(TemplateGrass::new),
    FARMLAND(TemplateFarmland::new),
    PLANT(TemplateFlower::new),
    CROP(TemplateCrop::new),
    FRUIT,
    BOOKSHELF,

    FENCE(TemplateFence::new),
    FLUID_UNSAFE,
    
    PRESSURE_PLATE(TemplatePlate::new),
    BUTTON(TemplateButton::new),
    LEVER(TemplateLever::new);

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
