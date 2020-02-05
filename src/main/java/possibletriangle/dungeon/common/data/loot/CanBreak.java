package possibletriangle.dungeon.common.data;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

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
        if(this.block != null)
            nbt.putString("CanBreak", this.block.getRegistryName().toString());

        return stack;
    }
}
