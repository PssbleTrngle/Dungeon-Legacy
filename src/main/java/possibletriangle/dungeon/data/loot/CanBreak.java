package possibletriangle.dungeon.data.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.data.DataGenerators;

public class CanBreak extends LootFunction {

    private final ResourceLocation block;

    private CanBreak(ILootCondition[] conditions, ResourceLocation block) {
        super(conditions);
        this.block = block;
    }

    @Override
    public LootFunctionType func_230425_b_() {
        return DataGenerators.CAN_BREAK;
    }

    public static LootFunction.Builder<?> builder(Block block) {
        return builder(conditions -> new CanBreak(conditions, block.getRegistryName()));
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {

        CompoundNBT nbt = stack.getOrCreateTag();
        if(this.block != null) {
            ListNBT list = new ListNBT();
            list.add(StringNBT.valueOf(this.block.toString()));
            nbt.put("CanDestroy", list);
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<CanBreak> {

        public void func_230424_a_(JsonObject object, CanBreak functionClazz, JsonSerializationContext context) {
            super.func_230424_a_(object, functionClazz, context);
            object.add("block", context.serialize(functionClazz.block));
        }

        public CanBreak deserialize(JsonObject object, JsonDeserializationContext context, ILootCondition[] conditionsIn) {
            return new CanBreak(conditionsIn, JSONUtils.deserializeClass(object, "block", context, ResourceLocation.class));
        }
    }
}
