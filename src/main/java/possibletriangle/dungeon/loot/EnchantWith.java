package possibletriangle.dungeon.loot;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class EnchantWith extends LootFunction {

    private final Enchantment ench;
    private final RandomValueRange level;

    public EnchantWith(Enchantment ench, RandomValueRange level) {
        super(new LootCondition[0]);
        this.ench = ench;
        this.level = level;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        stack.addEnchantment(ench, level.generateInt(rand));
        return stack;
    }
}
