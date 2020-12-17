package com.forpleuvoir.suika.item.weapon;

import com.forpleuvoir.suika.util.EntityUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.item.weapon
 * @class_name Raikiri
 * @create_time 2020/12/7 14:40
 */

public class Raikiri extends SwordItem {

    private static final int MAX_USE_TIME = 20 * 5;
    private static final double MAX_DISTANCE = 20D;


    public Raikiri(Settings settings) {
        super(ToolMaterials.NETHERITE, 10, 1.0f, settings);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient() && selected) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.isUsingItem() && livingEntity.getMainHandStack().equals(stack)) {
                int remainingUseTicks = livingEntity.getItemUseTimeLeft();
                double r = (((double) MAX_USE_TIME - (double) remainingUseTicks) / (double) MAX_USE_TIME) * MAX_DISTANCE;
                //显示技能范围
                skillRage(world, entity, r);
                List<LivingEntity> list = EntityUtil.getLivingEntities((ClientWorld) world, entity, r);
                list.forEach(target->{
                    Vec3d pos = target.getPos();
                    double x = pos.getX();
                    double y = pos.getY() + target.getHeight()+0.2;
                    double z = pos.getZ();
                    world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0D, 0d, 0.0D);
                });
            }
        }
    }


    @Environment(EnvType.CLIENT)
    private void skillRage(World world, Entity entity, double r) {
        Vec3d pos = entity.getPos();
        double x = pos.getX();
        double y = pos.getY() + 1d;
        double z = pos.getZ();
        for (int i = 0; i < 360; i += 20) {
            double x1 = x + (-r * (Math.cos((i * Math.PI) / 180)));
            double z1 = z + (-r * (Math.sin((i * Math.PI) / 180)));
            world.addParticle(ParticleTypes.FLAME, x1, y, z1, 0.0D, 0d, 0.0D);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (!world.isClient()) {
            double distance;
            distance = (((double) MAX_USE_TIME - (double) remainingUseTicks) / (double) MAX_USE_TIME) * MAX_DISTANCE;
            skill(user, (ServerWorld) world, distance);
        }
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        onStoppedUsing(stack, world, user, user.getItemUseTimeLeft());
        return super.finishUsing(stack, world, user);
    }

    private void skill(LivingEntity user, ServerWorld world, double distance) {
        List<LivingEntity> list = EntityUtil.getLivingEntities(world, distance, user);
        list.forEach(entity -> skill(world, entity));
    }


    private void skill(ServerWorld world, LivingEntity target) {
        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
        assert lightningEntity != null;
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(target.getBlockPos()));
        lightningEntity.setCosmetic(false);
        world.spawnEntity(lightningEntity);
    }


    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = target.getEntityWorld();
        if (world instanceof ServerWorld) {
            skill((ServerWorld) world, target);
        }

        return super.postHit(stack, target, attacker);
    }
}
