package possibletriangle.dungeon.data.blockstate;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.ObeliskBlock;

import javax.annotation.Nonnull;

public class Obelisks extends BlockStateProvider {

    public Obelisks(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private static ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    @Override
    protected void registerStatesAndModels() {

        withExistingParent("obelisk", new ResourceLocation("minecraft", "block"))
                .texture("particle", "#top")

                .element().from(0, 0, 0).to(16, 16, 16)
                .allFaces((dir, face) -> face.texture(dir.getAxis() == Direction.Axis.Y ? "down" : "bottom").uvs(0, 0, 16, 16)).end()

                .element().from(1, 16, 1).to(15, 22, 15)
                .face(Direction.EAST).end()
                .face(Direction.NORTH).end()
                .face(Direction.WEST).end()
                .face(Direction.SOUTH).end()
                .faces((dir, face) -> face.texture("top").uvs(1, 10, 15, 16)).end()

                .element().from(0, 22, 0).to(16, 32, 16)
                .allFaces((dir, face) -> face.texture(dir.getAxis() == Direction.Axis.Y ? "up" : "top").uvs(0, 0, 16, 16)).end()

                .element().from(0, 22, 0).to(16, 32, 16)
                .face(Direction.EAST).end()
                .face(Direction.NORTH).end()
                .face(Direction.WEST).end()
                .face(Direction.SOUTH).end()
                .faces((dir, face) -> face.texture("runes").uvs(0, 0, 16, 10 ).tintindex(1)).end();

        getVariantBuilder(ObeliskBlock.OBELISK).forAllStates(s -> {
           ObeliskBlock.State state = s.get(ObeliskBlock.STATE);
            ResourceLocation name = blockTexture(s.getBlock());
            ModelFile model = withExistingParent(name.getPath() + "_" + state.getName(), new ResourceLocation(DungeonMod.ID, "obelisk"))
                    .texture("down", extend(name, "_down"))
                    .texture("up", extend(name, "_up"))
                    .texture("bottom", extend(name, "_bottom"))
                    .texture("top", extend(name, "_top"))
                    .texture("runes", extend(name, "_runes"));
           return ConfiguredModel.builder().modelFile(model).build();
        });

    }

    @Nonnull
    @Override
    public String getName() {
        return "Obelisks";
    }
}
