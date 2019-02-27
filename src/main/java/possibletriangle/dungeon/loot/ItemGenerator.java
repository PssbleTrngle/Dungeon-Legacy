package possibletriangle.dungeon.loot;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.fml.common.Loader;
import possibletriangle.dungeon.helper.RandomCollection;

import java.util.ArrayList;
import java.util.Random;

public class ItemGenerator {

    public static LootEntry[] cursed() {

        ArrayList<LootEntry> entries = new ArrayList<>();

        entries.add(new LootEntryItem(Item.getItemFromBlock(Blocks.PUMPKIN), 1, 0,
                new LootFunction[]{new SetName("Put me on lol"), new EnchantWith(Enchantments.BINDING_CURSE, new RandomValueRange(1))}, new LootCondition[0], "pumpkin"));

        return entries.toArray(new LootEntry[0]);

    }

    public static LootEntry[] enchanted() {

        ArrayList<LootEntry> entries = new ArrayList<>();

        ArrayList<Entry> objects = new ArrayList<>();

        objects.add(new Entry("golden_axe")
                .enchant("fire_aspect")
                .s1("Blazing", "Burning", "Flaming", "Ignited")
                .s3("the golden flame", "the morning sun"));

        objects.add(new Entry("thermalfoundation:tool.sword_silver", "golden_sword")
                .enchant("smite")
                .s1("Silver", "Holy", "Jesus'")
                .s3("holy water", "the living"));

        objects.add(new Entry("thermalfoundation:armor.boots_aluminum", "iron_boots")
                .enchant("feather_falling")
                .attribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                .s1("Very fast", "Rapid", "Quick")
                .s3("rapid movement", "fluffy bunnies"));

        String[] a1 = new String[]{"helmet", "plate", "legs", "boots"};
        String[] a2 = new String[]{"helmet", "chestplate", "leggings", "boots"};
        for(int i = 0; i < a1.length; i++) {
            objects.add(new Entry("thermalfoundation:armor." + a1[i] + "_tin", "iron_" + a2[i])
                    .enchant("fire_protection")
                    .s1("Freezing", "Chilled")
                    .s3("winter", "frost")
                    .weight(0.25));

            objects.add(new Entry("thermalfoundation:armor." + a1[i] + "_steel", "chainmail_" + a2[i])
                    .enchant("blast_protection")
                    .attribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE)
                    .s1("Granite", "Solid")
                    .s3("stronk material", "an immovable force")
                    .weight(0.25));

            objects.add(new Entry("thermalfoundation:armor." + a1[i] + "_platinum", "diamond_" + a2[i])
                    .enchant("aqua_affinity")
                    .attribute(SharedMonsterAttributes.MAX_HEALTH)
                    .s1("Crystal water", "Fresh")
                    .s3("clearest waters", "the fountain of youth")
                    .weight(0.25));
        }

        if(Loader.isModLoaded("cofhcore"))
        objects.add(new Entry("natura:fusewood_bow", "bow")
                .enchant("cofhcore:multishot")
                .s1("Multilayered", "Plexing")
                .s3("the hydra"));

        if(Loader.isModLoaded("cofhcore"))
        objects.add(new Entry("natura:bloodwood_sword", "stone_sword")
                .enchant("cofhcore:leech")
                .s1("Vampire", "Dracula's")
                .s3("bloodthirst", "a hundred moscitos"));

        int i = 0;
        for(Entry e : objects) {

            if(e.item() != null && (e.enchantment != null || e.attribute != null)) {

                LootFunction name = new LootFunction(new LootCondition[0]) {
                    @Override
                    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {

                        String s1 = e.s1.next(rand);
                        String s3 = e.s3.next(rand);
                        String s2 = "Object";

                        if(stack.getItem() instanceof ItemArmor) {
                            switch (((ItemArmor) stack.getItem()).armorType) {
                                case HEAD: s2 = "Helmet"; break;
                                case CHEST: s2 = "Chestplate"; break;
                                case LEGS: s2 = "Leggings"; break;
                                case FEET: s2 = "Boots"; break;
                            }
                        }

                        else if(!stack.getItem().getToolClasses(stack).isEmpty())
                            s2 = stack.getItem().getToolClasses(stack).iterator().next();

                        else if(stack.getItem() instanceof ItemSword)
                            s2 = new RandomCollection<>("Sword", "Saber", "Broadsword", "Blade").next(rand);
                        else if(stack.getItem() instanceof ItemBow)
                            s2 = "Bow";

                        String name = TextFormatting.RESET + (rand.nextBoolean() ? (s1 + " " + s2) : (s2 + " of " + s3));
                        stack.setStackDisplayName(name);

                        return stack;
                    }
                };

                ArrayList<LootFunction> functions = new ArrayList<>();
                if(e.attribute != null) functions.add(new ApplyModifier(0.4, e.attribute));
                if(e.enchantment != null) functions.add(new EnchantWith(e.enchantment, new RandomValueRange(Math.max(e.enchantment.getMaxLevel()-1, e.enchantment.getMinLevel()), e.enchantment.getMaxLevel())));
                functions.add(new EnchantWith(Enchantments.VANISHING_CURSE, new RandomValueRange(1)));
                functions.add(name);

                entries.add(new LootEntryItem(e.item(), e.weight, 0, functions.toArray(new LootFunction[0]), new LootCondition[0], "epic_" + i++));

                entries.add(new LootEntryItem(Item.getItemFromBlock(Blocks.PUMPKIN), Math.max(1, entries.size() / 10), 0,
                        new LootFunction[]{new SetName("Put me on lol"), new EnchantWith(Enchantments.BINDING_CURSE, new RandomValueRange(1))}, new LootCondition[0], "pumpkin"));


            }
        }

        return entries.toArray(new LootEntry[0]);

    }

    public static class Entry {

        public Enchantment enchantment;
        public IAttribute attribute;
        private final Item item, fallback;
        public int weight = 24;

        public Item item() {
            return item == null ? fallback : item;
        }

        public RandomCollection<String> s1 = new RandomCollection<>();
        public RandomCollection<String> s3 = new RandomCollection<>();

        public Entry(String item) {
            this(item, item);
        }

        public Entry(String item, String fallback) {
            this.item = Item.REGISTRY.getObject(new ResourceLocation(item));
            this.fallback = Item.REGISTRY.getObject(new ResourceLocation(fallback));
        }

        public Entry enchant(String e) {
            enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation(e));
            return this;
        }

        public Entry attribute(IAttribute a) {
            attribute = a;
            return this;
        }

        public Entry weight(double w) {
            weight = (int) (24 * w);
            return this;
        }

        public Entry s1(String... ss) {
            for(String s : ss)
                s1.add(1, s);
            return this;
        }

        public Entry s3(String... ss) {
            for(String s : ss)
                s3.add(1, s);
            return this;
        }

    }

}
