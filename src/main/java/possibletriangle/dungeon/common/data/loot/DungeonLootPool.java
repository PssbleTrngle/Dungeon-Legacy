package possibletriangle.dungeon.data.loot;

public class DungeonLootPool {

    private final LootPool.Builder pool;

    public DungeonLootPool(String name, RandomValueRange rolls) {
        this.pool = LootPool.builder().name(name).rolls(rolls);
    }

    public DungeonLootPool add(LootPoolEntry.Builder group, int weight) {
        group.register(entry -> this.pool.addEntry(entry.weight(entry.getWeight() * weight)));
        return this;
    }

    public DungeonLootPool add(LootPoolEntry.Builder group) {
        return this.add(group, 1);
    }

    getPool() {
        return this.pool;
    }

}