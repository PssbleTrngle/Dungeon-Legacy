package possibletriangle.dungeon.common.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraftforge.fml.common.registry.GameRegistry;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.block.BreakableBlock;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.common.block.Type;
import possibletriangle.dungeon.common.world.room.StateProvider;
import possibletriangle.dungeon.helper.RandomCollection;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DungeonLoot extends LootTableProvider {

    public enum Rarity {
        COMMON, RARE, EPIC;

        public ResourceLocation path() {
            return new ResourceLocation(DungeonMod.MODID, name().toLowerCase());
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    protected final Map<Rarity, LootTable.Builder> lootTables = Maps.newHashMap();
    private final DataGenerator generator;

    public DungeonLoot(DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }

    private void register(ILootPool pool, Item... items) {
        int m = 2;
        int length = (int) Math.pow(items.length, m);
        IntStream.range(0, items.length).forEach(i -> pool.addEntry(
                ItemLootEntry.builder(items[i])
                        .weight(length - (int) Math.pow(i, 2))
        ));
    }

    private void registerEnchanting(ILootPool pool, HashMap<Item, Integer> items) {

        items.forEach((item, weight) -> pool.addEntry(
                ItemLootEntry.builder(item)
                        .weight(weight * 5)
                        .acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.2F, 0.8F)))
        ));

        items.forEach((item, weight) -> pool.addEntry(
                ItemLootEntry.builder(item)
                        .weight(weight)
                        .acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.6F, 0.9F)))
                        .acceptFunction(EnchantRandomly.func_215900_c())
        ));

    }

    private static LootPool.Builder keys(ILootPool pool) {

        /*
         * Find all blocks which could replace a placeholder seal block
         */
        List<StateProvider> providers = GameRegistry.findRegistry(Palette.class)
                .getValues().stream()
                .map(p -> p.blocksFor(Type.SEAL))
                .map(RandomCollection::all)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Block[] blocks = providers.stream()
                .map(p -> IntStream.range(0, Palette.MAX_VARIANT).mapToObj(p::apply))
                .flatMap(Function.identity())
                .filter(Objects::nonNull)
                .map(BlockState::getBlock)
                .distinct()
                .toArray(Block[]::new);;

        pool.addEntry(
            ItemLootEntry.builder(Items.STONE_BUTTON)
                .weight(5)
                .acceptFunction(CanPlaceOn.builder(blocks))
        );

        pool.addEntry(
            ItemLootEntry.builder(Items.LEVER)
                .weight(1)
                .acceptFunction(CanPlaceOn.builder(blocks))
        );
    }

    private static LootPool.Builder breakers(ILootPool pool) {

        new HashMap<>() {{

            put(Items.GOLDEN_PICKAXE, BreakableBlock.STONE);
            put(Items.GOLDEN_AXE, BreakableBlock.WOOD);
            put(Items.GOLDEN_SHOVEL, BreakableBlock.GRAVEL);

        }}.forEach((item, block) -> pool.addEntry(
                ItemLootEntry.builder(item)
                        .acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.6F, 0.9F)))
                        .acceptFunction(CanBreak.builder(block))
        ));
    }

    private static LootPool.Builder armor(ILootPool pool) {

        Map<Item, Integer> items = new HashMap<>() {{
            put(Items.LEATHER_BOOTS, 60);
            put(Items.LEATHER_HELMET, 40);
            put(Items.LEATHER_LEGGINGS, 40);
            put(Items.LEATHER_CHESTPLATE, 20);        
            put(Items.IRON_BOOTS, 15);
            put(Items.IRON_HELMET, 10);
            put(Items.IRON_LEGGINGS, 10);
            put(Items.IRON_CHESTPLATE, 5);     
            put(Items.DIAMOND_BOOTS, 3);
            put(Items.DIAMOND_HELMET, 2);
            put(Items.DIAMOND_LEGGINGS, 2);
            put(Items.DIAMOND_CHESTPLATE, 1);
        }};

        registerEnchanting(pool, items);
    }

    private static LootPool.Builder weapons(ILootPool pool) {

        Map<Item, Integer> items = new HashMap<>() {{
            put(Items.WOODEN_SWORD, 30);
            put(Items.STONE_SWORD, 15);
            put(Items.IRON_SWORD, 5);
            put(Items.DIAMOND_SWORD, 1);
            put(Items.WOODEN_AXE, 30);
            put(Items.STONE_AXE, 15);
            put(Items.IRON_AXE, 5);
            put(Items.DIAMOND_AXE, 1);
        }};
        
        items.put(Items.BOW, 2);
        pool.addEntry(
            ItemLootEntry.builder(Items.ARROW)
                .weight(10)
                .acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 5)))
        );

        registerEnchanting(pool, items);
    }

    private static LootPool.Builder gold(ILootPool pool) {

        pool.addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET)
                .acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 8)))
                .weight(10)
        );

        pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT)
                .acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 8)))
                .weight(2)
        );
    }

    private static LootPool.Builder tools(ILootPool pool) {

        pool.addEntry(ItemLootEntry.builder(Items.ENDER_PEARL)
                .acceptFunction(SetCount.func_215932_a(ConstantRange.of(1)))
                .weight(1)
        );
    }

    private static LootPool.Builder food(ILootPool pool) {
        
        pool.addEntry(ItemLootEntry.builder(Items.POTATO).weight(8).acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 6)));
        pool.addEntry(ItemLootEntry.builder(Items.APPLE).weight(5).acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 4)));
        pool.addEntry(ItemLootEntry.builder(Items.BREAD).weight(1).acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 2)));

    }

    private static LootPool.Builder potions(ILootPool pool) {
        /* TODO */
    }

    private static LootPool.Builder grenades(ILootPool pool) {
        /* TODO */
    }

    private void addTables() {

        lootTables.put(Rarity.COMMON, LootTable.builder()
                .addLootPool(new DungeonLootPool("shiny", new RandomValueRange(0, 1))
                    .add(DungeonLoot::gold)
                ).getPool()
                .addLootPool(new DungeonLootPool("food", ConstantRange.of(4))
                    .add(DungeonLoot::food)
                ).getPool()
                .addLootPool(new DungeonLootPool("battle", new RandomValueRange(1, 2))
                    .add(DungeonLoot::armor, 3)
                    .add(DungeonLoot::weapons, 3)
                    .add(DungeonLoot::grenades, 3)
                    .add(DungeonLoot::tools, 1)
                ).getPool()
        );

        lootTables.put(Rarity.RARE, LootTable.builder()
                .addLootPool(new DungeonLootPool("shiny", new RandomValueRange(2, 5))
                    .add(DungeonLoot::gold)
                ).getPool()
                .addLootPool(new DungeonLootPool("food", new RandomValueRange(0, 2))
                    .add(DungeonLoot::food)
                ).getPool()
                .addLootPool(new DungeonLootPool("battle", new RandomValueRange(1, 2))
                    .add(DungeonLoot::armor, 3)
                    .add(DungeonLoot::weapons, 3)
                    .add(DungeonLoot::potions, 1)
                ).getPool()
                .addLootPool(new DungeonLootPool("tools", new RandomValueRange(1, 2))
                    .add(DungeonLoot::tools, 2)
                    .add(DungeonLoot::keys, 1)
                    .add(DungeonLoot::breakers, 1)
                ).getPool()
        );

    }

    @Override
    public void act(DirectoryCache cache) {
        addTables();

        Map<ResourceLocation, LootTable> tables = Maps.newHashMap();
        lootTables.forEach((rarity, builder) ->
                tables.put(rarity.path(), builder.setParameterSet(LootParameterSets.CHEST).build()));

        writeTables(cache, tables);
    }

    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
            } catch (IOException e) {
                DungeonMod.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "Dungeon Loot";
    }

}
