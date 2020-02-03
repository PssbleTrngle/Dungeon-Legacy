package possibletriangle.dungeon.common.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.kinds.Const;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.SetContents;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.CommonProxy;
import possibletriangle.dungeon.common.block.BreakableBlock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

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
                        .acceptFunction(EnchantRandomly.func_215900_c())
        ));
    }

    private LootPool.Builder breakers() {
        LootPool.Builder pool = LootPool.builder()
                .name("breakers")
                .rolls(new RandomValueRange(0, 1));

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

    private LootPool.Builder weapons() {
        LootPool.Builder pool = LootPool.builder()
                .name("weapons")
                .rolls(new RandomValueRange(0, 2));

        register(pool, Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD);
        register(pool, Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE);

        return pool;
    }

    private LootPool.Builder shiny() {
        LootPool.Builder pool = LootPool.builder()
                .name("shiny")
                .rolls(new RandomValueRange(2, 4));

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
        LootPool.Builder pool = LootPool.builder()
                .name("food")
                .rolls(ConstantRange.of(4));
        register(pool, Items.POTATO, Items.APPLE, Items.BREAD);

        return pool;
    }

    private void addTables() {

        lootTables.put(Rarity.COMMON, LootTable.builder()
                .addLootPool(shiny())
                .addLootPool(weapons())
                .addLootPool(breakers())
                .addLootPool(food())
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
