package possibletriangle.dungeon.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.item.spells.Spell;
import possibletriangle.dungeon.item.spells.SpellStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ScrollItem extends Item {
    
    @ObjectHolder("dungeon:scroll")
    public static final Item SCROLL = null;
    public static final ResourceLocation MODEL_EMPTY = new ResourceLocation(DungeonMod.ID, "empty");

    public ScrollItem() {
        super(new Properties().group(DungeonMod.GROUP).maxStackSize(1));
        addPropertyOverride(MODEL_EMPTY, (stack, world, entity) -> {
            SpellStack spell = getSpell(stack);
            return spell.isEmpty() ? 1 : 0;
        });
    }
    
    public static void setSpell(ItemStack stack, SpellStack spell) {
        stack.getOrCreateTag().put("spell", spell.serializeNBT());
    }

    public static SpellStack getSpell(ItemStack stack) {
        return new SpellStack(stack.getOrCreateTag().getCompound("spell"));
    }

    public static ItemStack scrollOf(SpellStack spell) {
        ItemStack stack = new ItemStack(SCROLL, 1);
        setSpell(stack, spell);
        return stack;
    }

    public static Optional<LivingEntity> hitEntity(RayTraceResult result) {
        if(result instanceof EntityRayTraceResult) {
            Entity hit = ((EntityRayTraceResult) result).getEntity();
            if(hit instanceof LivingEntity) return Optional.of((LivingEntity) hit);
        }
        return Optional.empty();
    }

    public static RayTraceResult trace(World world, LivingEntity user, Spell spell) {
        Vec3d start = user.getPositionVector().add(0, user.getEyeHeight(), 0);
        Vec3d look = user.getLookVec().normalize().scale(spell.getRange());
        return world.rayTraceBlocks(new RayTraceContext(start, start.add(look), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, user));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity user) {
        SpellStack spell = getSpell(stack);

        if(!spell.isEmpty() && world instanceof ServerWorld) {
            RayTraceResult result = trace(world, user, spell.getSpell());
            if(result.getType() != RayTraceResult.Type.MISS) {
                Spell.UseContext context = new Spell.UseContext(result.getHitVec(), (ServerWorld) world, user, hitEntity(result), spell);
                spell.getSpell().use(context);
            }
        }

        if(!(user instanceof PlayerEntity && ((PlayerEntity) user).abilities.isCreativeMode))
            stack.shrink(1);

        return stack;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == ItemGroup.SEARCH || group == DungeonMod.GROUP)
            GameRegistry.findRegistry(Spell.class).getValues()
                    .stream()
                    .map(spell -> IntStream.rangeClosed(1, spell.maxPower())
                        .mapToObj(power -> new SpellStack(spell, power))
                    ).flatMap(Function.identity())
                    .map(ScrollItem::scrollOf)
                    .forEach(items::add);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        SpellStack spell = getSpell(stack);
        return spell.isEmpty() ? super.getUseDuration(stack) : spell.getSpell().getCharge();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        SpellStack spell = getSpell(stack);
        return spell.isEmpty() ? super.getUseAction(stack) : UseAction.BOW;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        World world = player.getEntityWorld();
        SpellStack spell = getSpell(stack);

        if(!spell.isEmpty()) {
            RayTraceResult result = trace(world, player, spell.getSpell());
            if (result.getType() != RayTraceResult.Type.MISS) {
                if(world instanceof ServerWorld) {
                    Vec3d vec = result.getHitVec();
                    ((ServerWorld) world).spawnParticle(ParticleTypes.ENCHANT, vec.x, vec.y, vec.z, 4, 0, 0, 0, 1);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        player.setActiveHand(hand);

        ItemStack stack = player.getHeldItem(hand);
        SpellStack spell = getSpell(stack);
        if(spell.isEmpty())
            return new ActionResult<>(ActionResultType.PASS, stack);

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public String getTranslationKey(ItemStack stack) {
        SpellStack spell = getSpell(stack);
        return spell.getTranslationKey();
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        SpellStack spell = getSpell(stack);
        tooltip.add(new TranslationTextComponent(spell.getTranslationKey() + ".description").applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("spell.power", spell.getPower()).applyTextStyle(TextFormatting.GRAY));
    }
}
