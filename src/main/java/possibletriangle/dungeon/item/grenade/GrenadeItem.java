package possibletriangle.dungeon.item.grenade;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.entity.GrenadeEntity;

public abstract class GrenadeItem extends Item {

    public static final RegistryObject<Item> FROST = DungeonMod.ITEMS.register("frost_grenade", GrenadeFrost::new);
    public static final RegistryObject<Item> SMOKE = DungeonMod.ITEMS.register("frost_grenade", GrenadeSmoke::new);
    public static final RegistryObject<Item> GRAVITY = DungeonMod.ITEMS.register("frost_grenade", GrenadeGravity::new);

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

        world.playSound(null, user.prevPosX, user.prevPosY, user.prevPosZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote) {
            GrenadeEntity entity = new GrenadeEntity(world, user);
            entity.setItem(thrown);
            entity.func_234612_a_(user, user.rotationPitch, user.rotationYaw, -20.0F, 0.5F, 1.0F);
            world.addEntity(entity);
        }

        user.getCooldownTracker().setCooldown(this, 20 * 3);
        user.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public abstract IParticleData getParticle();

}
