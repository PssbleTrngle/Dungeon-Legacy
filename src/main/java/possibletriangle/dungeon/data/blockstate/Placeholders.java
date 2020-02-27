package possibletriangle.dungeon.data.blockstate;

import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.placeholder.IPlaceholder;

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
        provider.defaultState(block, provider.cubeAll(name.getPath(), name));
    }

    public static void cross(Block block, Placeholders provider) {
        ResourceLocation name = provider.locationFor(block);
        provider.defaultState(block, provider.cross(name.getPath(), name));
    }

    public static void pillar(RotatedPillarBlock block, Placeholders provider) {
        ResourceLocation name = provider.locationFor(block);
        provider.axisBlock(block, name);
    }

    public static BiConsumer<FenceBlock, Placeholders> fence(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation t = provider.locationFor(texture.get());
            provider.fenceBlock(block, t);

            provider.withExistingParent("item/" + block.getRegistryName().getPath(), provider.mcLoc("block/fence_inventory"))
                    .texture("texture", t);
        };
    }

    public static BiConsumer<WallBlock, Placeholders> wall(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation t = provider.locationFor(texture.get());
            provider.wallBlock(block, t);

            provider.withExistingParent("item/" + block.getRegistryName().getPath(), provider.mcLoc("block/wall_inventory"))
                    .texture("wall", t);
        };
    }

    public static void pillar(Block block, Placeholders provider) {
        ResourceLocation name = provider.locationFor(block);
        ModelFile model = provider.cubeColumn(name.getPath(), extend(name, "_side"), extend(name, "_end"));
        provider.defaultState(block, model);
    }

    public static BiConsumer<Block, Placeholders> pillar(Supplier<Block> end) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(end.get());
            ModelFile model = provider.cubeColumn(name.getPath(), extend(name, "_side"), t);
            provider.defaultState(block, model);
        };
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
                    provider.mcLoc( "template_farmland"))
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

            Stream<String> sides = Arrays.stream(Direction.values()).filter(d -> d.getAxis() != Direction.Axis.Y).map(Direction::getName);

            ModelFile model = sides.reduce(provider.withExistingParent(name.getPath(),
                    provider.mcLoc( "cube"))
                    .texture("particle", t)
                    .texture("down", t)
                    .texture("up", extend(name, "_top")),
                    (m, s) -> m.texture(s, extend(name, "_side")), (a, b) -> a);

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
            ModelFile stairs = provider.stairs(name.getPath(), t, t, t);
            ModelFile inner = provider.stairsInner(name.getPath() + "_inner", t, t, t);
            ModelFile outer = provider.stairsOuter(name.getPath() + "_outer", t, t, t);
            provider.stairsBlock(block, stairs, inner, outer);
        };
    }

    public static BiConsumer<AbstractButtonBlock, Placeholders> button(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(texture.get());

            ModelFile normal = provider.withExistingParent(name.getPath(),
                    provider.mcLoc( "block/button"))
                    .texture("texture", t);
            ModelFile pressed = provider.withExistingParent(name.getPath() + "_pressed",
                    provider.mcLoc( "block/button_pressed"))
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

    public static BiConsumer<PressurePlateBlock, Placeholders> plate(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(texture.get());

            ModelFile up = provider.withExistingParent(name.getPath(),
                    provider.mcLoc( "block/pressure_plate_up"))
                    .texture("texture", t);
            ModelFile down = provider.withExistingParent(name.getPath() + "_down",
                    provider.mcLoc( "block/pressure_plate_down"))
                    .texture("texture", t);
            provider.getVariantBuilder(block).forAllStates(state -> {
                boolean powered = state.get(PressurePlateBlock.POWERED);
                return ConfiguredModel.builder()
                        .modelFile(powered ? down : up)
                        .build();
            });
        };
    }

    public static BiConsumer<LeverBlock, Placeholders> lever(Supplier<Block> texture) {
        return (block, provider) -> {
            ResourceLocation name = provider.locationFor(block);
            ResourceLocation t = provider.locationFor(texture.get());

            ModelFile off = provider.withExistingParent(name.getPath(),
                    provider.mcLoc( "block/lever"))
                    .texture("base", t)
                    .texture("particle", t)
                    .texture("lever", name);
            ModelFile on = provider.withExistingParent(name.getPath() + "_on",
                    provider.mcLoc( "block/lever_on"))
                    .texture("base", t)
                    .texture("particle", t)
                    .texture("lever", name);

            provider.getVariantBuilder(block).forAllStates(state -> {
                Direction facing = state.get(LeverBlock.HORIZONTAL_FACING);
                AttachFace face = state.get(LeverBlock.FACE);
                boolean powered = state.get(LeverBlock.POWERED);

                int y = (int) facing.getHorizontalAngle();
                if (face == AttachFace.CEILING) y += 180;

                return ConfiguredModel.builder()
                        .modelFile(powered ? on : off)
                        .rotationX(face == AttachFace.WALL ? 90 : 0)
                        .rotationY(y % 360)
                        .uvLock(true)
                        .build();
            });
        };
    }

    private boolean create(Block block) {
        assert block instanceof IPlaceholder;
        try {

            ((IPlaceholder) block).getType().createModel(block, this);
            boolean created = existingFileHelper.exists(block.getRegistryName(), ResourcePackType.CLIENT_RESOURCES, ".json", "blockstates");
            if(!created) DungeonMod.LOGGER.warn("Block '{}' did not create a blockstate file", block.getRegistryName());
            return created;

        } catch(ClassCastException ex ) {
            DungeonMod.LOGGER.warn("Block '{}' tries to create model of invalid block super class", block.getRegistryName());
        } catch (Exception ex) {
            DungeonMod.LOGGER.warn("Block '{}' tries to use non existing resource, ({})", block.getRegistryName(), ex.getMessage());
        }

        return false;
    }

    @Override
    protected void registerStatesAndModels() {

        long count = GameRegistry.findRegistry(Block.class).getValues()
                .stream()
                .filter(IPlaceholder.class::isInstance)
                .map(this::create)
                .filter(b -> b)
                .count();

        DungeonMod.LOGGER.info("Created {} placeholder blockstates and their corresponding models", count);

    }

    @Nonnull
    @Override
    public String getName() {
        return "Placeholders";
    }
}
