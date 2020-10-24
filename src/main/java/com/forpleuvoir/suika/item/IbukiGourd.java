package com.forpleuvoir.suika.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author forpleuvoir
 * @create 2020/10/13 18:55
 */
public class IbukiGourd extends Item {

    private static final Integer SEC=20;

    public IbukiGourd(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("suika.ibuki_gourd.tooltip"));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        world.createExplosion(player, player.getX(), player.getY(), player.getZ(), 1, false, Explosion.DestructionType.NONE);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity && !world.isClient) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,60*SEC));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,60*SEC));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,60*SEC,2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,60*SEC,2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH,600*SEC,5));
        }
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 64;
    }
}
