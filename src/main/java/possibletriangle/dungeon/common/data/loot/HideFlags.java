package possibletriangle.dungeon.common.data.loot;

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
        public Serializer() {
            super(new ResourceLocation(DungeonMod.ID, "hide_flags"), HideFlags.class);
        }

        public void serialize(JsonObject object, HideFlags functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);
            object.addProperty("flags", functionClazz.flags);
            object.addProperty("replace", functionClazz.replace);
        }

        public HideFlags deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new HideFlags(conditionsIn, JSONUtils.getInt(object, "flags", 0), JSONUtils.getBoolean(object, "replace", false));
        }
    }
}
