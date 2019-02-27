package possibletriangle.dungeon.pallete;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class Replacer {

    private final TreeMap<Integer, RandomCollection<Object>> MAP = new TreeMap<>();

    private boolean isEmpty = true;

    public boolean isEmpty() {
        return isEmpty;
    }

    public Replacer add(Object o, int priority, double weight) {

        if(o instanceof Block)
            add(((Block) o).getDefaultState(), priority, weight);
        if(o instanceof ResourceLocation)
            add(((ResourceLocation) o).toString(), priority, weight);
        else if(o instanceof IBlockState || o instanceof String) {

            RandomCollection<Object> r = MAP.get(priority);
            if(r == null) r = new RandomCollection<>();
            r.add(weight, o);
            MAP.put(priority * -1, r);
            isEmpty = false;

        }

        return this;
    }

    public IBlockState[] states() {

        if(isEmpty)
            return new IBlockState[0];

        ArrayList<IBlockState> list = new ArrayList<>();
        for(Object o : MAP.get(MAP.firstKey()).all()) {
            IBlockState state = convert(o);
            if(state != null)
                list.add(state);
        }

        return list.toArray(new IBlockState[0]);

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
            IBlockState state = convert(o);
            if(state != null)
                return state;
        }

        return Blocks.NOTEBLOCK.getDefaultState();

    }

    private IBlockState convert(Object o) {

        if (o instanceof IBlockState)
            return (IBlockState) o;
        if (o instanceof String) {
            String s = (String) o;
            int meta = s.split(":").length == 3 ? Integer.parseInt(s.substring(s.lastIndexOf(':')+1)) : 0;
            String name = s.split(":").length == 3 ? s.substring(0, s.lastIndexOf(':')) : s;
            Block b = Block.getBlockFromName((new ResourceLocation(name)).toString());
            if (b != null)
                return b.getStateFromMeta(meta);
        }

        Dungeon.LOGGER.error("\"{}\" does not exist!", o);
        return null;
    }

}
