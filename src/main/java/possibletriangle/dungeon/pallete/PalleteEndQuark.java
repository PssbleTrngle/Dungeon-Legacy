package possibletriangle.dungeon.pallete;

import net.minecraft.init.Blocks;

public class PalleteEndQuark extends PalleteEnd {

    @Override
    public String getName() {
        return "end_quark";
    }

    @Override
    public String[] requiredMods() {
        return new String[] {"quark"};
    }

    @Override
    public double weight() {
        return 0.1;
    }

    @Override
    public Replacer forType(Type type) {

        Replacer r = new Replacer();

        switch(type) {

            case WALL:
                r.add("quark:biotite_block");
                break;
            case FLOOR:
                r.add("quark:duskbound_block");
                break;
            case PILLAR:
                r.add("quark:duskbound_block_wall");
                break;

            case STAIRS_WALL:
                r.add("quark:biotite_stairs");
                break;
            case STAIRS:
                r.add("quark:duskbound_block_stairs");
                break;
            case SLAB:
                r.add("quark:duskbound_block_slab");
                break;

            default:
                return super.forType(type);

        }

        return r;

    }
}
