package possibletriangle.dungeon.loot;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.*;
import net.minecraftforge.fml.common.Loader;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.ModBlocks;
import possibletriangle.dungeon.pallete.Pallete;

import java.util.ArrayList;

public class LootManager {

    public static LootTable COMMON, EPIC, RELICS;

    public static LootTable get(String name) {
        if("common".equals(name)) return COMMON;
        if("epic".equals(name)) return EPIC;
        if("relics".equals(name)) return RELICS;
        return null;
    }

    public static void reload() {

        NBTTagCompound BREAK = canBreak(ModBlocks.BREAKABLE_ROCK);
        NBTTagCompound BREAK2 = canBreak(ModBlocks.BREAKABLE_ROCK, ModBlocks.BREAKABLE_ROCK_HARDER);

        COMMON = new LootTable(new LootPool[0]);
        EPIC = new LootTable(new LootPool[0]);
        RELICS = new LootTable(new LootPool[0]);

        COMMON.addPool(new LootPool(new LootEntry[] {
                create(Items.ENDER_PEARL, 0, new RandomValueRange(1, 4), 2, 0),
                create(Items.STICK, 0, new RandomValueRange(1, 2), 4, 0),
                create(Items.STRING, 0, new RandomValueRange(1, 2), 2, 0),
                create(Items.IRON_INGOT, 0, new RandomValueRange(1, 2), 5, 0),
                create(Items.DIAMOND, 0, new RandomValueRange(1), 1, 0),
                create(Items.EMERALD, 0, new RandomValueRange(1), 1, 0),
                create(Items.EXPERIENCE_BOTTLE, 0, new RandomValueRange(1), 1, 0)
        }, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(0), "materials"));

        COMMON.addPool(new LootPool(new LootEntry[] {
                createTool(Items.WOODEN_PICKAXE, new RandomValueRange(0.5F, 1F), 1, 3, 0, BREAK, enchantTool()),
                createTool(Items.GOLDEN_PICKAXE, new RandomValueRange(0.5F, 1F),1, 1, 0, BREAK2, enchantTool())
        }, new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "tools"));

        COMMON.addPool(new LootPool(new LootEntry[] {
                create(Items.WOODEN_AXE, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 30, 0, enchantWeapon()),
                create(Items.WOODEN_SWORD, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 30, 0, enchantWeapon()),
                create(Items.STONE_AXE, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 20, 0, enchantWeapon()),
                create(Items.STONE_SWORD, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 20, 0, enchantWeapon()),
                create(Items.GOLDEN_AXE, new RandomValueRange(0.2F, 0.6F), new RandomValueRange(1), 5, 0, enchantWeapon()),
                create(Items.GOLDEN_SWORD, new RandomValueRange(0.2F, 0.6F), new RandomValueRange(1), 5, 0, enchantWeapon()),
                create(Items.IRON_SWORD, new RandomValueRange(0.3F, 1F), new RandomValueRange(1), 1, 0, enchantWeapon()),
                create(Items.IRON_SWORD, new RandomValueRange(0.3F, 1F), new RandomValueRange(1), 1, 0, enchantWeapon()),
                create(Items.BOW, new RandomValueRange(0.2F, 1F), new RandomValueRange(1), 2, 0, enchantBow()),
                create(Items.ARROW, 0, new RandomValueRange(3, 7), 5, 0, enchantBow())
        }, new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "weapons"));

        COMMON.addPool(new LootPool(new LootEntry[] {
                create(Items.LEATHER_HELMET, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 15, 0, enchantArmor()),
                create(Items.LEATHER_CHESTPLATE, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 15, 0, enchantArmor()),
                create(Items.LEATHER_LEGGINGS, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 15, 0, enchantArmor()),
                create(Items.LEATHER_BOOTS, new RandomValueRange(0.5F, 1F), new RandomValueRange(1), 15, 0, enchantArmor()),
                create(Items.GOLDEN_HELMET, new RandomValueRange(0.2F, 0.6F), new RandomValueRange(1), 4, 0, enchantArmor()),
                create(Items.GOLDEN_CHESTPLATE, new RandomValueRange(0.2F, 0.6F), new RandomValueRange(1), 4, 0, enchantArmor()),
                create(Items.GOLDEN_LEGGINGS, new RandomValueRange(0.2F, 0.6F), new RandomValueRange(1), 4, 0, enchantArmor()),
                create(Items.GOLDEN_BOOTS, new RandomValueRange(0.2F, 0.6F), new RandomValueRange(1), 4, 0, enchantArmor()),
                create(Items.IRON_HELMET, new RandomValueRange(0.3F, 1F), new RandomValueRange(1), 1, 0, enchantArmor()),
                create(Items.IRON_CHESTPLATE, new RandomValueRange(0.3F, 1F), new RandomValueRange(1), 1, 0, enchantArmor()),
                create(Items.IRON_LEGGINGS, new RandomValueRange(0.3F, 1F), new RandomValueRange(1), 1, 0, enchantArmor()),
                create(Items.IRON_BOOTS, new RandomValueRange(0.3F, 1F), new RandomValueRange(1), 1, 0, enchantArmor())
        }, new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "armor"));

        if(Loader.isModLoaded("spartanshields")) {
            COMMON.addPool(new LootPool(new LootEntry[]{
                    create(Item.REGISTRY.getObject(new ResourceLocation("spartanshields", "shield_basic_wood")), new RandomValueRange(0.4F, 1), new RandomValueRange(1), 6, 0, enchant(Enchantment.REGISTRY.getObject(new ResourceLocation("spartanshields", "ssenchspikes")))),
                    create(Item.REGISTRY.getObject(new ResourceLocation("spartanshields", "shield_basic_gold")), new RandomValueRange(0.2F, 0.6F), new RandomValueRange(1), 1, 0, enchant(Enchantment.REGISTRY.getObject(new ResourceLocation("spartanshields", "ssenchspikes")))),
                    create(Item.REGISTRY.getObject(new ResourceLocation("spartanshields", "shield_basic_stone")), new RandomValueRange(0.4F, 1), new RandomValueRange(1), 3, 0, enchant(Enchantment.REGISTRY.getObject(new ResourceLocation("spartanshields", "ssenchspikes"))))
            }, new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "shields"));
        }

        if(Loader.isModLoaded("natura")) {
            COMMON.addPool(new LootPool(new LootEntry[]{
                    create(Item.REGISTRY.getObject(new ResourceLocation("natura", "imp_armor_boots")), new RandomValueRange(0.4F, 1), new RandomValueRange(1), 1, 0, new EnchantWith(Enchantments.BINDING_CURSE, new RandomValueRange(1))),
                    create(Item.REGISTRY.getObject(new ResourceLocation("natura", "imp_armor_chestplate")), new RandomValueRange(0.4F, 1), new RandomValueRange(1), 1, 0, new EnchantWith(Enchantments.BINDING_CURSE, new RandomValueRange(1))),
                    create(Item.REGISTRY.getObject(new ResourceLocation("natura", "imp_armor_leggings")), new RandomValueRange(0.4F, 1), new RandomValueRange(1), 1, 0, new EnchantWith(Enchantments.BINDING_CURSE, new RandomValueRange(1))),
                    create(Item.REGISTRY.getObject(new ResourceLocation("natura", "imp_armor_boots")), new RandomValueRange(0.4F, 1), new RandomValueRange(1), 1, 0, new EnchantWith(Enchantments.BINDING_CURSE, new RandomValueRange(1)))
                }, new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "impskin"));
        }

        COMMON.addPool(new LootPool(new LootEntry[] {
                create(Items.APPLE, 0, new RandomValueRange(1, 3), 20, 0),
                create(Items.CARROT, 0, new RandomValueRange(1, 3), 20, 0),
                create(Items.POTATO, 0, new RandomValueRange(1, 3), 20, 0),
                create(Items.POISONOUS_POTATO, 0, new RandomValueRange(1, 3), 15, 0),
                create(Items.BREAD, 0, new RandomValueRange(1, 4), 25, 0),
                create(Items.PUMPKIN_PIE, 0, new RandomValueRange(1, 3), 5, 0),
                create(Items.GOLDEN_APPLE, 0, new RandomValueRange(1), 2, 0),
                create(Items.GOLDEN_APPLE, 1, new RandomValueRange(1), 1, 0)
        }, new LootCondition[0], new RandomValueRange(1, 2), new RandomValueRange(0), "food"));

        COMMON.addPool(new LootPool(new LootEntry[] {
                createTool(Items.FIRE_CHARGE, new RandomValueRange(0), 1, 1, 0, canPlaceOn(Blocks.MAGMA)),
                createTool(Item.getItemFromBlock(Blocks.STONE_BUTTON), new RandomValueRange(0), 1, 1, 0, canPlaceOn(Pallete.allBlocksFor(Pallete.Type.KEY_STONE))),
                createTool(Item.getItemFromBlock(Blocks.LEVER), new RandomValueRange(0), 1, 1, 0, canPlaceOn(Pallete.allBlocksFor(Pallete.Type.KEY_STONE)))
        }, new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "keys"));

        COMMON.addPool(new LootPool(potionsEntries(), new LootCondition[0], new RandomValueRange(0, 2), new RandomValueRange(0), "potions"));

        EPIC.addPool(new LootPool(new LootEntry[] {
                create(Items.ENDER_PEARL, 0, new RandomValueRange(2, 5), 5, 0),
                create(Items.IRON_INGOT, 0, new RandomValueRange(2, 4), 3, 0),
                create(Items.DIAMOND, 0, new RandomValueRange(1, 3), 1, 0),
                create(Items.EMERALD, 0, new RandomValueRange(1, 3), 1, 0),
                create(Items.EXPERIENCE_BOTTLE, 0, new RandomValueRange(1, 2), 2, 0)
        }, new LootCondition[0], new RandomValueRange(2, 4), new RandomValueRange(0), "materials"));

        EPIC.addPool(new LootPool(potionsEntries(), new LootCondition[0], new RandomValueRange(1, 3), new RandomValueRange(0), "potions"));

        for(LootTable TABLE : new LootTable[]{EPIC, RELICS}) {

            TABLE.addPool(new LootPool(ItemGenerator.enchanted(), new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "objects"));
            //TABLE.addPool(new LootPool(ItemGenerator.cursed(), new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "cursed"));

            if (Loader.isModLoaded("heroicarmory")) {

                ArrayList<LootEntry> list = new ArrayList<>();
                for (Item i : Item.REGISTRY)
                    if (i.getRegistryName().getResourceDomain().equals("heroicarmory"))
                        list.add(create(i, new RandomValueRange(1), new RandomValueRange(1), 1, 0, new EnchantWith(Enchantments.VANISHING_CURSE, new RandomValueRange(1))));

                TABLE.addPool(new LootPool(list.toArray(new LootEntry[0]), new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "heroicarmory"));

            }
        }

        Dungeon.LOGGER.info("Loaded LootTable");

    }

    public static LootEntryItem createTool(Item i, RandomValueRange meta, int count, int weight, int quality, NBTTagCompound nbt, LootFunction... functions) {
        LootFunction[] f = new LootFunction[functions.length+1];
        f[functions.length] = new SetNBT(new LootCondition[0], nbt);
        System.arraycopy(functions, 0, f, 0, functions.length);
        return create(i, meta, new RandomValueRange(count), weight, quality, f);
    }

    public static LootEntryItem create(Item i, int meta, RandomValueRange count, int weight, int quality, LootFunction... functions) {
        return create(i, new RandomValueRange(meta), count, weight, quality, functions);
    }

    public static LootEntryItem create(Item i, RandomValueRange meta, RandomValueRange count, int weight, int quality, LootFunction... functions) {
        if(i == null)
            return null;

        LootFunction[] f = new LootFunction[2 + functions.length];
        if(i.isDamageable())
            f[0] = new SetDamage(new LootCondition[0], new RandomValueRange(meta.getMin(), meta.getMax()));
        else
            f[0] = new SetMetadata(new LootCondition[0], meta);

        f[1] = new SetCount(new LootCondition[0], count);

        System.arraycopy(functions, 0, f, 2, functions.length);

        return new LootEntryItem(i, weight, 5, f, new LootCondition[0], i.getRegistryName() + ":" + i);
    }

    public static NBTTagCompound canBreak(Block... block) {

        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for(Block b : block)
            list.appendTag(new NBTTagString(b.getRegistryName().toString()));
        nbt.setTag("CanDestroy", list);
        return nbt;

    }

    public static NBTTagCompound canPlaceOn(Block... block) {

        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for(Block b : block)
            list.appendTag(new NBTTagString(b.getRegistryName().toString()));
        nbt.setTag("CanPlaceOn", list);
        return nbt;

    }

    public static LootFunction enchantTool() {
        return enchant(
                Enchantments.EFFICIENCY);
    }

    public static LootFunction enchantBow() {
        return enchant(
                Enchantments.FLAME,
                Enchantments.PUNCH,
                Enchantment.REGISTRY.getObject(new ResourceLocation("cofhcore", "multishot")));
    }

    public static LootFunction enchantArmor() {
        return enchant(
                Enchantments.THORNS,
                Enchantments.PROTECTION,
                Enchantments.PROJECTILE_PROTECTION,
                Enchantments.BLAST_PROTECTION,
                Enchantments.RESPIRATION,
                Enchantments.FEATHER_FALLING);
    }

    public static LootFunction enchantWeapon() {
        return enchant(
                Enchantments.SHARPNESS,
                Enchantments.FIRE_ASPECT,
                Enchantments.SWEEPING,
                Enchantments.KNOCKBACK,
                Enchantments.BANE_OF_ARTHROPODS,
                Enchantment.REGISTRY.getObject(new ResourceLocation("cofhcore", "vorpal")),
                Enchantment.REGISTRY.getObject(new ResourceLocation("cofhcore", "leech")));
    }

    public static LootFunction enchant(Enchantment... enchantments) {

        ArrayList<Enchantment> list = new ArrayList();
        for(Enchantment e : enchantments)
            if(e != null) list.add(e);

        RandomChance cond = new RandomChance(0.3F);
        return new EnchantRandomly(new LootCondition[]{cond}, list);

    }

    public static LootEntry[] potionsEntries() {

        ArrayList<LootEntry> list = new ArrayList<>();

        for(Potion p : Potion.REGISTRY) if(hasPotionType(p.getRegistryName().toString())) {

            for(ItemStack stack : createPotion(p).getMatchingStacks())
                list.add(createTool(stack.getItem(), new RandomValueRange(stack.getMetadata()), 1, 1, 0, stack.getTagCompound()));
        }

        return list.toArray(new LootEntry[0]);

    }

    public static Ingredient createPotion(Potion p) {

        ArrayList<ItemStack> potion = new ArrayList<>();

        for (PotionType type : PotionType.REGISTRY)
            if (type.getEffects().size() == 1 && type.getEffects().get(0).getPotion().equals(p)) {
                potion.add(getItemStackOfPotion(Items.POTIONITEM, type));
                potion.add(getItemStackOfPotion(Items.SPLASH_POTION, type));
                potion.add(getItemStackOfPotion(Items.LINGERING_POTION, type));
                //potion.add(getItemStackOfPotion(Items.TIPPED_ARROW, type));
            }

        return Ingredient.fromStacks(potion.toArray(new ItemStack[0]));
    }

    public static ItemStack getItemStackOfPotion(Item it, PotionType pt) {

        ItemStack res = new ItemStack(it);
        res.setTagCompound(new NBTTagCompound());
        PotionUtils.addPotionToItemStack(res, pt);
        return res;
    }

    public static boolean hasPotionType(String name) {
        if (Potion.getPotionFromResourceLocation(name) == null)
            return false;
        return createPotion(Potion.getPotionFromResourceLocation(name)).getMatchingStacks().length > 0;
    }

}
