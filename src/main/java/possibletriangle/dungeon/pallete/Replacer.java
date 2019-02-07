package possibletriangle.dungeon.pallete;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.RandomCollection;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class Replacer {

    private final TreeMap<Integer, RandomCollection<Object>> MAP = new TreeMap<>();

    public Replacer add(Object o, int priority, double weight) {

        if(o instanceof Block)
            add(((Block) o).getDefaultState(), priority, weight);
        if(o instanceof String)
            add(new ResourceLocation((String) o), priority, weight);
        else if(o instanceof IBlockState || o instanceof ResourceLocation) {

            RandomCollection<Object> r = MAP.get(priority);
            if(r == null) r = new RandomCollection<>();
            r.add(weight, o);
            MAP.put(priority * -1, r);

        }

        return this;
    }

    public Replacer add(Object o) {
        return add(o, 0);
    }

    public Replacer add(Object o, int priority) {
        return add(o, priority, 1);
    }

    public IBlockState get(Random r) {

        for(int key : MAP.keySet()) {
            Object o = MAP.get(key).next(r);

            if (o instanceof IBlockState)
                return (IBlockState) o;
            if (o instanceof ResourceLocation) {
                Block b = Block.getBlockFromName(((ResourceLocation) o).toString());
                if (b != null)
                    return b.getDefaultState();
            }
        }

        return Blocks.AIR.getDefaultState();

    }

}
