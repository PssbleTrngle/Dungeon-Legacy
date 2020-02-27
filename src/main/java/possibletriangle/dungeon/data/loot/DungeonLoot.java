package possibletriangle.dungeon.data.loot;

import com.google.common.collect.ImmutableSet;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.*;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.BreakableBlock;
import possibletriangle.dungeon.palette.Palette;
import possibletriangle.dungeon.block.placeholder.Type;
import possibletriangle.dungeon.item.grenade.GrenadeItem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DungeonLoot extends LootTableProvider {

    public enum Rarity {
        COMMON, RARE, EPIC;

        public ResourceLocation path() {
            return new ResourceLocation(DungeonMod.ID, name().toLowerCase());
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    protected final Map<Rarity, LootTable.Builder> lootTables = Maps.newHashMap();
    private final DataGenerator generator;

    public DungeonLoot(DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }

    private static void register(BiConsumer<ItemLootEntry.Builder, Integer> pool, Item... items) {
        int m = 2;
        int length = (int) Math.pow(items.length, m);
        IntStream.range(0, items.length).forEach(i -> pool.accept(
                ItemLootEntry.builder(items[i]), length - (int) Math.pow(i, 2)
        ));
    }

    private static void registerEnchanting(BiConsumer<ItemLootEntry.Builder, Integer> pool, HashMap<Item, Integer> items) {

        items.forEach((item, weight) -> pool.accept(
                ItemLootEntry.builder(item)
                        .acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.2F, 0.8F))),
                weight * 5
        ));

        items.forEach((item, weight) -> pool.accept(
                ItemLootEntry.builder(item)
                        .acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.6F, 0.9F)))
                        .acceptFunction(EnchantRandomly.func_215900_c()),
                weight
        ));

    }

    private static void keys(BiConsumer<ItemLootEntry.Builder, Integer> pool) {

        /*
         * Find all blocks which could replace a placeholder seal block
         */
        Block[] blocks = Palette.values().stream()
                .map(p -> p.blocksFor(Type.SEAL))
                .flatMap(Function.identity())
                .filter(Objects::nonNull)
                .map(BlockState::getBlock)
                .toArray(Block[]::new);

        ILootFunction.IBuilder lore = () -> new SetLore(new ILootCondition[0], true, Arrays.asList(
                new StringTextComponent(""),
                new TranslationTextComponent("model.canPlace").applyTextStyles(TextFormatting.RESET, TextFormatting.GRAY),
                new TranslationTextComponent("tooltip.dungeon.keystone").applyTextStyles(TextFormatting.RESET, TextFormatting.DARK_GRAY)
        ), null);

        pool.accept(
            ItemLootEntry.builder(Items.STONE_BUTTON)
                    .acceptFunction(CanPlaceOn.builder(blocks))
                    .acceptFunction(HideFlags.builder(0b10000))
                    .acceptFunction(lore),
                5
        );

        pool.accept(
            ItemLootEntry.builder(Items.LEVER)
                    .acceptFunction(CanPlaceOn.builder(blocks))
                    .acceptFunction(HideFlags.builder(0b10000))
                    .acceptFunction(lore),
                1
        );
    }

    private static void breakers(BiConsumer<ItemLootEntry.Builder, Integer> pool) {

        new HashMap<Item,Block>() {{

            put(Items.GOLDEN_PICKAXE, BreakableBlock.STONE);
            put(Items.GOLDEN_AXE, BreakableBlock.WOOD);
            put(Items.GOLDEN_SHOVEL, BreakableBlock.GRAVEL);

        }}.forEach((item, block) -> pool.accept(
                ItemLootEntry.builder(item)
                        .acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.6F, 0.9F)))
                        .acceptFunction(CanBreak.builder(block)),
                1
        ));
    }

    private static void armor(BiConsumer<ItemLootEntry.Builder, Integer> pool) {
        
        registerEnchanting(pool, new HashMap<Item, Integer>() {{
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
        }});
        
    }

    private static void weapons(BiConsumer<ItemLootEntry.Builder, Integer> pool) {

        registerEnchanting(pool, new HashMap<Item, Integer>() {{
            put(Items.WOODEN_SWORD, 30);
            put(Items.STONE_SWORD, 15);
            put(Items.IRON_SWORD, 5);
            put(Items.DIAMOND_SWORD, 1);
            put(Items.WOODEN_AXE, 30);
            put(Items.STONE_AXE, 15);
            put(Items.IRON_AXE, 5);
            put(Items.DIAMOND_AXE, 1);
            put(Items.BOW, 2);
        }});

        pool.accept(
            ItemLootEntry.builder(Items.ARROW)
                .acceptFunction(SetCount.builder(new RandomValueRange(1, 5))),
                10
        );

    }

    private static void gold(BiConsumer<ItemLootEntry.Builder, Integer> pool) {

        pool.accept(ItemLootEntry.builder(Items.GOLD_NUGGET)
                .acceptFunction(SetCount.builder(new RandomValueRange(1, 8)))
                , 10
        );

        pool.accept(ItemLootEntry.builder(Items.GOLD_INGOT)
                .acceptFunction(SetCount.builder(new RandomValueRange(1, 8)))
                , 2
        );
    }

    private static void tools(BiConsumer<ItemLootEntry.Builder, Integer> pool) {

        pool.accept(ItemLootEntry.builder(Items.ENDER_PEARL)
                .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                , 1
        );
    }

    private static void food(BiConsumer<ItemLootEntry.Builder, Integer> pool) {
        
        pool.accept(ItemLootEntry.builder(Items.POTATO).acceptFunction(SetCount.builder(new RandomValueRange(1, 6))), 8);
        pool.accept(ItemLootEntry.builder(Items.APPLE).acceptFunction(SetCount.builder(new RandomValueRange(1, 4))), 5);
        pool.accept(ItemLootEntry.builder(Items.BREAD).acceptFunction(SetCount.builder(new RandomValueRange(1, 2))), 1);

    }

    private static Stream<ItemLootEntry.Builder> createPotions(Item potionItem, Collection<Potion> potions) {
        return potions.stream().map(potion -> {
            ItemStack stack = new ItemStack(potionItem, 1);
            PotionUtils.addPotionToItemStack(stack, potion);
            return stack.getOrCreateTag();
        }).map(SetNBT::func_215952_a).map(nbt ->
                ItemLootEntry.builder(potionItem).acceptFunction(nbt)
        );
    }

    private static void potions(BiConsumer<ItemLootEntry.Builder, Integer> pool) {
        createPotions(Items.POTION, ImmutableSet.of(
                Potions.FIRE_RESISTANCE,
                Potions.HEALING,
                Potions.INVISIBILITY,
                Potions.LONG_NIGHT_VISION,
                Potions.LUCK,
                Potions.SWIFTNESS,
                Potions.STRENGTH,
                Potions.NIGHT_VISION,
                Potions.STRONG_HEALING,
                Potions.STRONG_TURTLE_MASTER
        )).forEach(p -> pool.accept(p, 5));

        createPotions(Items.SPLASH_POTION, ImmutableSet.of(
                Potions.HARMING,
                Potions.LONG_WEAKNESS,
                Potions.WEAKNESS,
                Potions.STRONG_POISON,
                Potions.STRONG_HARMING,
                Potions.SLOWNESS,
                Potions.POISON
        )).forEach(p -> pool.accept(p, 5));
    }

    private static void grenades(BiConsumer<ItemLootEntry.Builder, Integer> pool) {
        new HashMap<Item, Integer>() {{
            put(GrenadeItem.SMOKE, 3);
            put(GrenadeItem.FROST, 1);
            put(GrenadeItem.GRAVITY, 1);
        }}.forEach((item, weight) ->
                pool.accept(ItemLootEntry.builder(item)
                            .acceptFunction(SetCount.builder(ConstantRange.of(1)))
                        , weight)
        );
    }

    private void addTables() {

        lootTables.put(Rarity.COMMON, LootTable.builder()
                .addLootPool(new DungeonLootPool("shiny", new RandomValueRange(0, 1))
                    .add(DungeonLoot::gold)
                        .getPool())
                .addLootPool(new DungeonLootPool("food", ConstantRange.of(2))
                    .add(DungeonLoot::food)
                        .getPool())
                .addLootPool(new DungeonLootPool("battle", new RandomValueRange(1, 2))
                    .add(DungeonLoot::armor, 3)
                    .add(DungeonLoot::weapons, 3)
                    .add(DungeonLoot::grenades, 3)
                    .add(DungeonLoot::tools, 1)
                        .getPool())
        );

        lootTables.put(Rarity.RARE, LootTable.builder()
                .addLootPool(new DungeonLootPool("shiny", new RandomValueRange(2, 4))
                    .add(DungeonLoot::gold)
                .getPool())
                .addLootPool(new DungeonLootPool("food", new RandomValueRange(0, 2))
                    .add(DungeonLoot::food)
                .getPool())
                .addLootPool(new DungeonLootPool("battle", new RandomValueRange(2, 5))
                    .add(DungeonLoot::armor, 3)
                    .add(DungeonLoot::weapons, 3)
                    .add(DungeonLoot::potions, 2)
                .getPool())
                .addLootPool(new DungeonLootPool("tools", new RandomValueRange(1, 2))
                    .add(DungeonLoot::tools, 2)
                    .add(DungeonLoot::keys, 1)
                    .add(DungeonLoot::breakers, 1)
                .getPool())
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
