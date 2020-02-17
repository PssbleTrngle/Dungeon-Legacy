package possibletriangle.dungeon.common.data.loot;

import com.google.gson.JsonArray;
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
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import possibletriangle.dungeon.DungeonMod;

import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class CanPlaceOn extends LootFunction {

    private final ResourceLocation[] blocks;

    private CanPlaceOn(ILootCondition[] conditions, ResourceLocation[] blocks) {
        super(conditions);
        this.blocks = blocks;
    }

    public static LootFunction.Builder<?> builder(Block... blocks) {
        ResourceLocation[] locations = Arrays.stream(blocks).map(Block::getRegistryName).filter(Objects::nonNull).toArray(ResourceLocation[]::new);
        return builder(conditions -> new CanPlaceOn(conditions, locations));
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {

        CompoundNBT nbt = stack.getOrCreateTag();
        
        ListNBT list = new ListNBT();

        Arrays.stream(this.blocks)
            .map(ResourceLocation::toString)
            .map(StringNBT::new)
            .forEach(list::add);

        nbt.put("CanPlaceOn", list);
        
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<CanPlaceOn> {
        public Serializer() {
            super(new ResourceLocation(DungeonMod.ID, "can_place_on"), CanPlaceOn.class);
        }

        public void serialize(JsonObject object, CanPlaceOn functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);

            JsonArray blocks = new JsonArray();
            Arrays.stream(functionClazz.blocks)
                .map(serializationContext::serialize)
                .forEach(blocks::add);

            object.add("blocks", blocks);
        }

        public CanPlaceOn deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            JsonArray array = JSONUtils.getJsonArray(object, "blocks", new JsonArray());
            ResourceLocation[] locations = StreamSupport
                    .stream(Spliterators.spliteratorUnknownSize(array.iterator(), Spliterator.ORDERED), false)
                    .map(e -> JSONUtils.deserializeClass(e, "block", deserializationContext, ResourceLocation.class))
                    .toArray(ResourceLocation[]::new);
            return new CanPlaceOn(conditionsIn, locations);
        }
    }
}
