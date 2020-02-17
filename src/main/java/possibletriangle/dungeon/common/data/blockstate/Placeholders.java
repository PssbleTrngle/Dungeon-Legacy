package possibletriangle.dungeon.common.data.blockstate;

import net.minecraft.block.*;
import net.minecraft.client.renderer.model.BlockPartRotation;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.placeholder.IPlaceholder;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Placeholders extends BlockStateProvider  {

    public Placeholders(DataGenerator gen, ExistingFileHelper filehelper) {
        super(gen, DungeonMod.ID, filehelper);
    }

    private boolean exists(ResourceLocation name) {
        return existingFileHelper.exists(name, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");
    }

    private  ResourceLocation locationFor(Block block) {
        ResourceLocation r = block.getRegistryName();
        assert r != null;
        return new ResourceLocation(r.getNamespace(), "block/" + r.getPath());
    }

    public static void full(Block block, Placeholders provider) {
        ResourceLocation name = provider.locationFor(block);
        provider.cubeAll(name.getPath(), name);
    }

    public static void cross(Block block, Placeholders provider) {
        ResourceLocation name = provider.locationFor(block);
        provider.cross(name.getPath(), name);
    }

    public static void pillar(RotatedPillarBlock block, Placeholders provider) {
        ResourceLocation name = provider.locationFor(block);
        provider.axisBlock(block, name);
    }

    private void defaultState(Block block, ModelFile... models) {
        getVariantBuilder(block).partialState().addModels(Arrays.stream(models).map(model ->
                ConfiguredModel.builder().modelFile(model).buildLast()
        ).toArray(ConfiguredModel[]::new));
    }

    public static BiConsumer<Block, Placeholders> farmland(Supplier<Block> dirt) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(dirt.get());
            ModelFile model = provider.withExistingParent(name.getPath(),
                    new ResourceLocation("minecraft", "template_farmland"))
                    .texture("particle", t)
                    .texture("dirt", t)
                    .texture("top", name);

            provider.defaultState(block, model);
        };
    }

    private static ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    public static BiConsumer<Block, Placeholders> grass(Supplier<Block> dirt) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(dirt.get());

                ModelFile model = provider.withExistingParent(name.getPath(),
                        new ResourceLocation("minecraft", "cube"))
                        .texture("particle", t)
                        .texture("down", t)
                        .texture("up", extend(name, "_top"))
                        .texture("north", extend(name, "_side"))
                        .texture("east", extend(name, "_side"))
                        .texture("south", extend(name, "_side"))
                        .texture("west", extend(name, "_side"));

                provider.getVariantBuilder(block).partialState().addModels(IntStream.of(0, 90, 180, 270).mapToObj(y ->
                        ConfiguredModel.builder().modelFile(model).rotationY(y).buildLast()
                ).toArray(ConfiguredModel[]::new));
        };
    }

    public static BiConsumer<SlabBlock, Placeholders> slab(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation t = provider.locationFor(texture.get());
            provider.slabBlock(block, texture.get().getRegistryName(), t);
        };
    }

    public static BiConsumer<StairsBlock, Placeholders> stairs(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(texture.get());
            provider.stairsBlock(block, name.getPath(), t);
        };
    }

    public static BiConsumer<AbstractButtonBlock, Placeholders> button(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(texture.get());

            ModelFile normal = provider.withExistingParent(name.getPath(),
                    new ResourceLocation("minecraft", "block/button"))
                    .texture("texture", t);
            ModelFile pressed = provider.withExistingParent(name.getPath() + "_pressed",
                    new ResourceLocation("minecraft", "block/button_pressed"))
                    .texture("texture", t);
            provider.getVariantBuilder(block).forAllStates(state -> {
                Direction facing = state.get(AbstractButtonBlock.HORIZONTAL_FACING);
                AttachFace face = state.get(AbstractButtonBlock.FACE);
                boolean powered = state.get(AbstractButtonBlock.POWERED);

                int y = (int) facing.getHorizontalAngle();
                if (face == AttachFace.CEILING) y += 180;

                return ConfiguredModel.builder()
                        .modelFile(powered ? pressed : normal)
                        .rotationX(face == AttachFace.WALL ? 90 : 0)
                        .rotationY(y % 360)
                        .uvLock(true)
                        .build();
            });
        };
    }

    private void create(Block block) {
        assert block instanceof IPlaceholder;
        try {
            ((IPlaceholder) block).getType().createModel(block, this);
        } catch(ClassCastException ex ) {
            DungeonMod.LOGGER.warn("Block '{}' tries to create model of invalid block super class", block.getRegistryName());
        } catch (Exception ex) {
            DungeonMod.LOGGER.warn("Block '{}' tries to use non existing resource, ({})", block.getRegistryName(), ex.getMessage());
        }
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
