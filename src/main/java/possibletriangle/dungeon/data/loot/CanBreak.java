package possibletriangle.dungeon.data.loot;

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

public class CanBreak extends LootFunction {

    private final ResourceLocation block;

    private CanBreak(ILootCondition[] conditions, ResourceLocation block) {
        super(conditions);
        this.block = block;
    }

    public static LootFunction.Builder<?> builder(Block block) {
        return builder(conditions -> new CanBreak(conditions, block.getRegistryName()));
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {

        CompoundNBT nbt = stack.getOrCreateTag();
        if(this.block != null) {
            ListNBT list = new ListNBT();
            list.add(new StringNBT(this.block.toString()));
            nbt.put("CanDestroy", list);
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<CanBreak> {
        public Serializer() {
            super(new ResourceLocation(DungeonMod.ID, "can_break"), CanBreak.class);
        }

        public void serialize(JsonObject object, CanBreak functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);
            object.add("block", serializationContext.serialize(functionClazz.block));
        }

        public CanBreak deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new CanBreak(conditionsIn, JSONUtils.deserializeClass(object, "block", deserializationContext, ResourceLocation.class));
        }
    }
}
