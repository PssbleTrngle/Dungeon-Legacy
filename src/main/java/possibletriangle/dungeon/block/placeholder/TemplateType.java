package possibletriangle.dungeon.block.placeholder;

import net.minecraft.block.*;
import net.minecraftforge.fml.RegistryObject;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.data.blockstate.Placeholders;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class TemplateType<T extends Block> {

    private static final ArrayList<TemplateType> VALUES = new ArrayList<>();

    public static final TemplateType<Block> FLOOR = new TemplateType<>("FLOOR", TemplateBlock::new);
    public static final TemplateType<Block> WALL = new TemplateType<>("WALL", TemplateBlock::new);
    public static final TemplateType<Block> PATH = new TemplateType<>("PATH", TemplateBlock::new);
    public static final TemplateType<Block> RUNE = new TemplateType<>("RUNE", TemplateBlock::new);
    public static final TemplateType<Block> GEM = new TemplateType<>("GEM", TemplateBlock::new);
    public static final TemplateType<Block> SEAL = new TemplateType<>("SEAL", TemplateBlock::new);
    public static final TemplateType<Block> GLASS = new TemplateType<>("GLASS", TemplateTransparent::hide);
    public static final TemplateType<Block> FALLING = new TemplateType<>("FALLING", TemplateFalling::new);
    public static final TemplateType<RotatedPillarBlock> PILLAR = new TemplateType<>("PILLAR", TemplatePillar::new, Placeholders::pillar);
    public static final TemplateType<Block> LAMP = new TemplateType<>("LAMP", TemplateBlock.glowing(15));

    public static final TemplateType<StairsBlock> STAIRS = new TemplateType<>("STAIRS", TemplateStairs::new, Placeholders.stairs(FLOOR::getBlock));
    public static final TemplateType<SlabBlock> SLAB = new TemplateType<>("SLAB", TemplateSlab::new, Placeholders.slab(FLOOR::getBlock));
    public static final TemplateType<StairsBlock> STAIRS_WALL = new TemplateType<>("STAIRS_WALL", TemplateStairs::new, Placeholders.stairs(WALL::getBlock));
    public static final TemplateType<SlabBlock> SLAB_WALL = new TemplateType<>("SLAB_WALL", TemplateSlab::new, Placeholders.slab(WALL::getBlock));
    
    public static final TemplateType<Block> PLANKS = new TemplateType<>("PLANKS", TemplateBlock::new, Placeholders::full);
    public static final TemplateType<StairsBlock> STAIRS_WOOD = new TemplateType<>("STAIRS_WOOD", TemplateStairs::new, Placeholders.stairs(PLANKS::getBlock));
    public static final TemplateType<SlabBlock> SLAB_WOOD = new TemplateType<>("SLAB_WOOD", TemplateSlab::new, Placeholders.slab(PLANKS::getBlock));
    public static final TemplateType<Block> LEAVES = new TemplateType<>("LEAVES", TemplateTransparent::new);
    public static final TemplateType<RotatedPillarBlock> STRIPPED_LOG = new TemplateType<>("STRIPPED_LOG", TemplatePillar::new, Placeholders::pillar);
    public static final TemplateType<RotatedPillarBlock> LOG = new TemplateType<>("LOG", TemplatePillar::new, Placeholders::pillar);

    public static final TemplateType<Block> DIRT = new TemplateType<>("DIRT", TemplateGrass::new);
    public static final TemplateType<Block> GRASS = new TemplateType<>("GRASS", TemplateGrass::new, Placeholders.grass(DIRT::getBlock));
    public static final TemplateType<Block> FARMLAND = new TemplateType<>("FARMLAND", TemplateFarmland::new, Placeholders.farmland(DIRT::getBlock));
    public static final TemplateType<Block> PLANT = new TemplateType<>("PLANT", TemplateFlower::new, Placeholders::cross);
    public static final TemplateType<Block> CROP = new TemplateType<>("CROP", TemplateCrop::new, Placeholders::cross);
    public static final TemplateType<Block> FRUIT = new TemplateType<>("FRUIT", TemplateBlock::new, Placeholders::pillar);
    public static final TemplateType<Block> BOOKSHELF = new TemplateType<>("BOOKSHELF", TemplateBlock::new, Placeholders.pillar(PLANKS::getBlock));

    public static final TemplateType<FenceBlock> FENCE = new TemplateType<>("FENCE", TemplateFence::new, Placeholders.fence(PLANKS::getBlock));
    public static final TemplateType<WallBlock> WALL_BLOCK = new TemplateType<>("WALL_BLOCK", TemplateWall::new, Placeholders.wall(WALL::getBlock));
    public static final TemplateType<Block> FLUID_UNSAFE = new TemplateType<>("FLUID_UNSAFE", TemplateBlock::new, Placeholders::full);
    
    public static final TemplateType<PressurePlateBlock> PRESSURE_PLATE = new TemplateType<>("PRESSURE_PLATE", TemplatePlate::new, Placeholders.plate(FLOOR::getBlock));
    public static final TemplateType<AbstractButtonBlock> BUTTON = new TemplateType<>("BUTTON", TemplateButton::new, Placeholders.button(WALL::getBlock));
    public static final TemplateType<LeverBlock> LEVER = new TemplateType<>("LEVER", TemplateLever::new, Placeholders.lever(WALL::getBlock));

    public static final TemplateType<PlaceholderChest> CHEST = new TemplateType<>("chest", PlaceholderChest::new, (t, p) -> {});
    public static final TemplateType<PlaceholderTrappedChest> TRAPPED_CHEST = new TemplateType<>("trapped_chest", PlaceholderTrappedChest::new, (t, p) -> {});

    private final RegistryObject<T> block;
    private final BiConsumer<T, Placeholders> model;
    private final String name;
    
    public void createModel(T name, Placeholders provider) {
        model.accept(name, provider);
    }

    TemplateType(String name, Function<TemplateType<T>, T> block, BiConsumer<T, Placeholders> model) {
        this.block = DungeonMod.registerBlock("placeholder_" + name, () -> block.apply(this));
        this.model = model;
        this.name = name;
        VALUES.add(this);
    }

    TemplateType(String name, Function<TemplateType<T>, T> block) {
        this(name, block, Placeholders::full);
    }

    public String name() {
        return this.name;
    }

    public static Stream<TemplateType> values() {
        return VALUES.stream();
    }

    public static Optional<TemplateType> byName(String name) {
        return values().filter(t -> t.name().equalsIgnoreCase(name)).findFirst();
    }

    public T getBlock() {
        return this.block.get();
    }
    
}
