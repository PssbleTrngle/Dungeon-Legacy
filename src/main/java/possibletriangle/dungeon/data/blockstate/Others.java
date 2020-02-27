package possibletriangle.dungeon.data.blockstate;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.BreakableBlock;
import possibletriangle.dungeon.block.MetadataBlock;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class Others extends BlockStateProvider {

    public Others(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    @Override
    protected void registerStatesAndModels() {

        Stream.of(
                BreakableBlock.STONE,
                BreakableBlock.GRAVEL,
                BreakableBlock.WOOD,
                MetadataBlock.METADATA_BLOCK
        ).forEach(this::simpleBlock);

    }

    @Nonnull
    @Override
    public String getName() {
        return "Others";
    }
}
