package possibletriangle.dungeon.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

import javax.annotation.Nonnull;

@ObjectHolder(DungeonMod.MODID)
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
