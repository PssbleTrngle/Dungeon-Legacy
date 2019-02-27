package possibletriangle.dungeon.loot;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class ApplyModifier extends LootFunction {

    private final IAttribute[] modifiers;
    private double amount;

    protected ApplyModifier(double amount, IAttribute... modifiers) {
        super(new LootCondition[0]);
        this.modifiers = modifiers;
        this.amount = amount;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {

        EntityEquipmentSlot slot = stack.getItem().getEquipmentSlot(stack);
        if(stack.getItem() instanceof ItemArmor) slot = ((ItemArmor) stack.getItem()).armorType;
        if(slot == null)
            slot = EntityEquipmentSlot.MAINHAND;

        for(IAttribute m : modifiers)
            stack.addAttributeModifier(m.getName(), new AttributeModifier(m.getName(), amount, 2), slot);

        return stack;
    }
}
