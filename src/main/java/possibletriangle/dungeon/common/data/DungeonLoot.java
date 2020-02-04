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

    private void register(LootPool.Builder pool, Item... items) {
        int m = 2;
        int length = (int) Math.pow(items.length, m);
        IntStream.range(0, items.length).forEach(i -> pool.addEntry(
                ItemLootEntry.builder(items[i])
                        .weight(length - (int) Math.pow(i, 2))
        ));
    }

    private void registerEnchanting(LootPool.Builder pool, HashMap<Item, Integer> items) {

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

    private LootPool.Builder keys() {
        LootPool.Builder pool = LootPool.builder().name("breakers");

        /*
         * Find all blocks which could replace a placeholder seal block
         */
        List<StateProvider> providers = GameRegistry.findRegistry(Palette.class)
                .getValues().stream()
                .map(p -> p.blocksFor(Type.SEAL))
                .map(RandomCollection::all)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        Function<StateProvider, Stream<BlockState>> extract = p -> IntStream.range(0, Palette.MAX_VARIANT).mapToObj(p::apply);

        Block[] blocks = providers.stream()
                .map(extract)
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

        return pool;
    }

    private LootPool.Builder breakers() {
        LootPool.Builder pool = LootPool.builder().name("breakers");

        HashMap<Item, Block> map = new HashMap<>();
        map.put(Items.GOLDEN_PICKAXE, BreakableBlock.STONE);
        map.put(Items.GOLDEN_AXE, BreakableBlock.WOOD);
        map.put(Items.GOLDEN_SHOVEL, BreakableBlock.GRAVEL);
        map.forEach((item, block) -> pool.addEntry(
                ItemLootEntry.builder(item)
                        .acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.6F, 0.9F)))
                        .acceptFunction(CanBreak.builder(block))
        ));

        return pool;
    }

    private LootPool.Builder armor() {
        LootPool.Builder pool = LootPool.builder().name("armor");

        HashMap<Item, Integer> items = Maps.newHashMap();
        
        items.put(Items.LEATHER_BOOTS, 60);
        items.put(Items.LEATHER_HELMET, 40);
        items.put(Items.LEATHER_LEGGINGS, 40);
        items.put(Items.LEATHER_CHESTPLATE, 20);
        
        items.put(Items.IRON_BOOTS, 15);
        items.put(Items.IRON_HELMET, 10);
        items.put(Items.IRON_LEGGINGS, 10);
        items.put(Items.IRON_CHESTPLATE, 5);
        
        items.put(Items.DIAMOND_BOOTS, 3);
        items.put(Items.DIAMOND_HELMET, 2);
        items.put(Items.DIAMOND_LEGGINGS, 2);
        items.put(Items.DIAMOND_CHESTPLATE, 1);

        registerEnchanting(pool, items);

        return pool;
    }

    private LootPool.Builder weapons() {
        LootPool.Builder pool = LootPool.builder().name("weapons");

        HashMap<Item, Integer> items = Maps.newHashMap();
        
        items.put(Items.WOODEN_SWORD, 30);
        items.put(Items.STONE_SWORD, 15);
        items.put(Items.IRON_SWORD, 5);
        items.put(Items.DIAMOND_SWORD, 1);
        
        items.put(Items.WOODEN_AXE, 30);
        items.put(Items.STONE_AXE, 15);
        items.put(Items.IRON_AXE, 5);
        items.put(Items.DIAMOND_AXE, 1);
        
        items.put(Items.BOW, 2);
        pool.addEntry(
            ItemLootEntry.builder(Items.ARROW)
                .weight(10)
                .acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 5)))
        );

        registerEnchanting(pool, items);

        return pool;
    }

    private LootPool.Builder shiny() {
        LootPool.Builder pool = LootPool.builder().name("shiny");

        pool.addEntry(ItemLootEntry.builder(Items.GOLD_NUGGET)
                .acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 8)))
                .weight(10)
        );

        pool.addEntry(ItemLootEntry.builder(Items.GOLD_INGOT)
                .acceptFunction(SetCount.func_215932_a(new RandomValueRange(1, 8)))
                .weight(2)
        );

        pool.addEntry(ItemLootEntry.builder(Items.ENDER_PEARL)
                .acceptFunction(SetCount.func_215932_a(ConstantRange.of(1)))
                .weight(1)
        );

        return pool;
    }

    private LootPool.Builder food() {
        LootPool.Builder pool = LootPool.builder().name("food");

        register(pool, Items.POTATO, Items.APPLE, Items.BREAD);

        return pool;
    }

    private void addTables() {

        lootTables.put(Rarity.COMMON, LootTable.builder()
                .addLootPool(shiny().rolls(new RandomValueRange(2, 4)))
                .addLootPool(weapons().rolls(new RandomValueRange(0, 2)))
                .addLootPool(food().rolls(ConstantRange.of(4)))
        );

        lootTables.put(Rarity.RARE, LootTable.builder()
                .addLootPool(shiny().rolls(ConstantRange.of(4)))
                .addLootPool(breakers().rolls(new RandomValueRange(0, 1)))
                .addLootPool(food().rolls(ConstantRange.of(3)))
                .addLootPool(keys().rolls(new RandomValueRange(0, 1)))
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
