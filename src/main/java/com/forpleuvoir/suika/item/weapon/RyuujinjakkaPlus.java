package com.forpleuvoir.suika.item.weapon;

import com.forpleuvoir.suika.util.EntityUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.item.weapon
 * @class_name RyuujinjakkaPlus
 * @create_time 2020/12/6 17:18
 */

public class RyuujinjakkaPlus extends SwordItem {

    public RyuujinjakkaPlus(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        Random random = new Random();
        int a = random.nextInt(123456);
        if (world.isClient()) {
            if (selected) {
                if (a % 15 == 0) {
                    randomDisplayTick(world, entity);

                } else if (a % 5 == 0) {
                    westEffect(world, entity);
                }

            }
        } else {
            //该物品在背包里就要着火e
            entity.setOnFireFor(1);
            if (selected) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 233, 5));
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 233, 1));
                }
                if (a % 5 == 0) {
                    west(world, entity);
                }

            }
        }

    }


    @Override
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey(stack)).styled(style -> style.withColor(Formatting.DARK_RED));
    }

    @Override
    public Text getName() {
        return new TranslatableText(this.getTranslationKey()).styled(style -> style.withColor(Formatting.DARK_RED));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(60);
        target.setHealth(0);
        return super.postHit(stack, target, attacker);
    }

    //残火太刀-西 残日狱衣的特效
    @Environment(EnvType.CLIENT)
    private void westEffect(World world, Entity entity) {
        Vec3d pos = entity.getPos();
        double x = pos.getX();
        double y = pos.getY() + 1d;
        double z = pos.getZ();
        double r = 7D;
        for (int i = 0; i < 360; i += 2) {
            double x1 = x + (-r * (Math.cos((i * Math.PI) / 180)));
            double z1 = z + (-r * (Math.sin((i * Math.PI) / 180)));
            world.addParticle(ParticleTypes.FLAME, x1, y, z1, 0.0D, 0d, 0.0D);
        }
    }

    private void west(World world, Entity entity) {
        Vec3d pos = entity.getPos();
        ServerWorld serverWorld = (ServerWorld) world;
        double r = 7D;
        List<LivingEntity> entities = EntityUtil.getLivingEntities(serverWorld, r, (LivingEntity) entity);
        entities.forEach(entity1 -> {
            entity1.setOnFireFor(10);
        });

        List<ProjectileEntity> arrowEntities = EntityUtil.getProjectileEntities(serverWorld, r, (LivingEntity) entity);
        arrowEntities.forEach(entity1 -> {
            if (entity1 instanceof ShulkerBulletEntity) {
                ShulkerBulletEntity shulkerBulletEntity = (ShulkerBulletEntity) entity1;
                ((ServerWorld) world).spawnParticles(ParticleTypes.EXPLOSION, shulkerBulletEntity.getX(), shulkerBulletEntity.getY(), shulkerBulletEntity.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
                shulkerBulletEntity.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
                shulkerBulletEntity.remove();
            } else
                entity1.setOnFireFor(100);
        });


    }


    @Environment(EnvType.CLIENT)
    private void randomDisplayTick(World world, Entity entity) {
        Vec3d pos = entity.getPos();
        double x = pos.getX();
        double y = pos.getY() + 1.1d;
        double z = pos.getZ();
        float yaw = entity.getYaw(1.0f);
        if (yaw > 360) yaw = yaw % 360;
        yaw = yaw - 45;
        double r = 0.7d;
        double x1 = x + (-r * (Math.cos((yaw * Math.PI) / 180)));
        double z1 = z + (-r * (Math.sin((yaw * Math.PI) / 180)));
        world.addParticle(ParticleTypes.SMOKE, x1, y, z1, 0.0D, 0.05d, 0.0D);
    }
}

