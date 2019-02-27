package possibletriangle.dungeon.loot;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class SetName extends LootFunction {

    private final String name;

    public SetName(String name) {
        super(new LootCondition[0]);
        this.name = name;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {

        stack.setStackDisplayName(name);

        return stack;
    }
}
