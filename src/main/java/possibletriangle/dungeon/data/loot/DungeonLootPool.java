package possibletriangle.dungeon.data.loot;

import net.minecraft.world.storage.loot.IRandomRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DungeonLootPool {

    private final LootPool.Builder pool;

    public DungeonLootPool(String name, IRandomRange rolls) {
        this.pool = LootPool.builder().name(name).rolls(rolls);
    }

    public DungeonLootPool add(Consumer<BiConsumer<ItemLootEntry.Builder, Integer>> group, int weight) {
        group.accept((entry, w) -> this.pool.addEntry(entry.weight(w * weight)));
        return this;
    }

    public DungeonLootPool add(Consumer<BiConsumer<ItemLootEntry.Builder, Integer>> group) {
        return this.add(group, 1);
    }

    LootPool.Builder getPool() {
        return this.pool;
    }

}