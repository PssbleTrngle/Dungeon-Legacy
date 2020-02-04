package possibletriangle.dungeon.common.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.SetDamage;
import possibletriangle.dungeon.DungeonMod;

public class CanPlaceOn extends LootFunction {

    private final ResourceLocation[] blocks;

    private CanPlaceOn(ILootCondition[] conditions, ResourceLocation[] blocks) {
        super(conditions);
        this.blocks = block;
    }

    public static LootFunction.Builder<?> builder(Block... blocks) {
        ResourceLocation[] locations = blocks.stream().map(Block::getRegistryName).filter(Object::notNull).toArray(ResourceLocation[]::new));
        return builder(conditions -> new CanPlaceOn(conditions, locations);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {

        CompoundNBT nbt = stack.getOrCreateTag();
        
        ListNBT list = new ListNBT();

        this.blocks.stream()
            .map(ResourceLocation::toString)
            .map(StringNBT::new)
            .forEach(list::add);

        nbt.put("CanPlaceOn", list);
        
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<CanPlaceOn> {
        public Serializer() {
            super(new ResourceLocation(DungeonMod.MODID, "can_place_on"), CanPlaceOn.class);
        }

        public void serialize(JsonObject object, CanPlaceOn functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);

            JsonArray blocks = new JsonArray();
            this.blocks.stream()
                .map(serializationContext::serialize)
                .forEach(blocks::add);

            object.add("blocks", blocks);
        }

        public CanPlaceOn deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            JsonArray blocks = JSONUtils.getArray(object, "blocks", new JsonArray());
            ResourceLocation[] locations = ???;
            return new CanPlaceOn(conditionsIn, locations);
        }
    }
}
