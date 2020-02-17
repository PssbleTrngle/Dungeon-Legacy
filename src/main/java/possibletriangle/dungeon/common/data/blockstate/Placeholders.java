package possibletriangle.dungeon.common.data.blockstate;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.placeholder.IPlaceholder;

import javax.annotation.Nonnull;

public class Placeholders extends BlockStateProvider  {

    public Placeholders(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private boolean exists(ResourceLocation name) {
        return existingFileHelper.exists(name, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");
    }

    public static void full(Block block, Placeholders provider) {
        ResourceLocation name = provider.modLoc("block/" + block.getRegistryName().getPath());
        if(provider.exists(name)) provider.cubeAll(name.getPath(), name);
    }

    public static void stairs(StairsBlock block, Placeholders provider) {
        ResourceLocation name = provider.modLoc("block/" + block.getRegistryName().getPath());
        if(provider.exists(name)) provider.stairsBlock(block, name.getPath(), name);
    }

    private void create(Block block) {
        assert block instanceof IPlaceholder;
        ((IPlaceholder) block).getType().createModel(block, this);
    }

    @Override
    protected void registerStatesAndModels() {

        GameRegistry.findRegistry(Block.class).getValues()
                .stream()
                .filter(b -> b instanceof IPlaceholder)
                .forEach(this::create);

    }

    @Nonnull
    @Override
    public String getName() {
        return "Placeholders";
    }
}
