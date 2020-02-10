package possibletriangle.dungeon.common.data.loot;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
<<<<<<< HEAD
=======
import possibletriangle.dungeon.DungeonMod;
>>>>>>> Fixing imports and some syntax errors (yes, again)

public class CanBreak extends LootFunction {

    private final Block block;

    private CanBreak(ILootCondition[] conditions, Block block) {
        super(conditions);
        this.block = block;
    }

    public static LootFunction.Builder<?> builder(Block block) {
        return builder(conditions -> new CanBreak(conditions, block));
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {

        CompoundNBT nbt = stack.getOrCreateTag();
<<<<<<< HEAD
        if(this.block != null)
            nbt.putString("CanBreak", this.block.getRegistryName().toString());
=======
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
>>>>>>> Rename MODID variable

        return stack;
    }
}
