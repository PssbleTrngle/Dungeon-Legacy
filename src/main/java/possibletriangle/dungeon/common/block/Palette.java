package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.HashMap;
import java.util.Random;

public class Palette {

    private final HashMap<Template, RandomCollection<Block>> blocks = new HashMap<>();
    private final RandomCollection<Block> def = new RandomCollection<>(Blocks.SPONGE);

    public Palette() {}

    public Palette put(Template template, RandomCollection<Block> collection) {
        this.blocks.putIfAbsent(template, collection);
        return this;
    }

    public Block blockFor(Template template, Random random) {
        return this.blocks.getOrDefault(template, def).next(random);
    }

}
