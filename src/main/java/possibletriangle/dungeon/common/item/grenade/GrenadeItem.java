package possibletriangle.dungeon.common.item.grenade;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.entity.GrenadeEntity;

import java.util.Optional;

@ObjectHolder("dungeon")
public abstract class GrenadeItem extends Item {

    @ObjectHolder("frost_grenade")
    public static final Item FROST = null;

    @ObjectHolder("smoke_grenade")
    public static final Item SMOKE = null;

    @ObjectHolder("gravity_grenade")
    public static final Item GRAVITY = null;

    public GrenadeItem() {
        super(new Properties().group(DungeonMod.GROUP).maxStackSize(8));
    }

    public abstract void affect(LivingEntity entity);

    public boolean affects(LivingEntity entity) {
        return true;
    }

    @Override
    public final ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getHeldItem(hand);
        ItemStack thrown = user.abilities.isCreativeMode ? stack.copy() : stack.split(1);

        world.playSound(null, user.posX, user.posY, user.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            GrenadeEntity entity = new GrenadeEntity(world, user);
            entity.setItem(thrown);
            entity.shoot(user, user.rotationPitch, user.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.addEntity(entity);
        }

        user.getCooldownTracker().setCooldown(this, 20 * 3);
        user.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public abstract IParticleData getParticle();

}
