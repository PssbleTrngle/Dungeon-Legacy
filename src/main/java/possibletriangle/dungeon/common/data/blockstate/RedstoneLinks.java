package possibletriangle.dungeon.common.data.blockstate;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.ObeliskBlock;
import possibletriangle.dungeon.common.block.RedstoneLinkBlock;
import possibletriangle.dungeon.common.block.RedstoneReceiverBlock;
import possibletriangle.dungeon.common.block.RedstoneSenderBlock;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class RedstoneLinks extends BlockStateProvider {

    public RedstoneLinks(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private static ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    @Override
    protected void registerStatesAndModels() {

        getVariantBuilder(RedstoneLinkBlock.RECEIVER).forAllStates(state -> {
            boolean powered = state.get(RedstoneLinkBlock.POWERED);
            ResourceLocation name = blockTexture(state.getBlock());
            ResourceLocation modelName = powered ? extend(name, "_powered") : name;
            ModelFile model = withExistingParent(modelName.getPath(), new ResourceLocation("minecraft", "cube_all"))
                    .texture("all", modelName);
            return ConfiguredModel.builder().modelFile(model).build();
        });

        directionalBlock(RedstoneLinkBlock.SENDER, state -> {
            boolean powered = state.get(RedstoneLinkBlock.POWERED);
            ResourceLocation name = blockTexture(state.getBlock());
            String suffix =powered ? "_powered" : "";
            ResourceLocation side = new ResourceLocation("minecraft", "block/furnace_top");

            return Arrays.stream(Direction.values()).filter(s -> s != Direction.NORTH).reduce(
                    withExistingParent(name.getPath() + suffix, new ResourceLocation("minecraft", "cube"))
                            .texture("particle", side)
                            .texture("north", extend(name, "_front" + suffix)),
                    (m, s) -> m.texture(s.getName(), side), (a, b) -> a);
        });

    }

    @Nonnull
    @Override
    public String getName() {
        return "Redstone Links";
    }
}
