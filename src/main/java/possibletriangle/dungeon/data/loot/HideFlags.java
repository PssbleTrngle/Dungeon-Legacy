package possibletriangle.dungeon.data.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.data.DataGenerators;

/**
 * Hide certain info on ItemStacks
 * from <a href='https://minecraft.gamepedia.com/Tutorials/Command_NBT_tags'>Minecraft Wiki</a>
 *
 * 1st Bit: Enchantments
 * 2nd Bit: Attributes modifiers
 * 3rd Bit: Unbreakable
 * 4th Bit: CanDestroy
 * 5th Bit: CanPlaceOn
 * 6th Bit: Others, such as potion effects & shield pattern info
 *
 * Example: 0b00101
 * -> Will hide Enchantments & Unbreakable
 *
 */
public class HideFlags extends LootFunction {

    private final int flags;
    private final boolean replace;

    @Override
    public LootFunctionType func_230425_b_() {
        return DataGenerators.HIDE_FLAGS;
    }

    private HideFlags(ILootCondition[] conditions, int flags, boolean replace) {
        super(conditions);
        this.flags = flags;
        this.replace = replace;
    }

    public static Builder<?> builder(int flags) {
        return builder(flags, false);
    }

    public static Builder<?> builder(int flags, boolean replace) {
        return builder(conditions -> new HideFlags(conditions, flags, replace));
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {

        CompoundNBT nbt = stack.getOrCreateTag();
        int current = nbt.getInt("HideFlags");

        if(replace) nbt.putInt("HideFlags", this.flags);
        else nbt.putInt("HideFlags", this.flags | current);

        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<HideFlags> {

        public void func_230424_a_(JsonObject object, HideFlags functionClazz, JsonSerializationContext context) {
            super.func_230424_a_(object, functionClazz, context);
            object.addProperty("flags", functionClazz.flags);
            object.addProperty("replace", functionClazz.replace);
        }

        public HideFlags deserialize(JsonObject object, JsonDeserializationContext context, ILootCondition[] conditions) {
            return new HideFlags(conditions, JSONUtils.getInt(object, "flags", 0), JSONUtils.getBoolean(object, "replace", false));
        }
    }
}
