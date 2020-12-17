package com.forpleuvoir.suika.item.weapon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

/**
 * 流刃若火
 *
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.item.weapon
 * @class_name Ryuujinjakka
 * @create_time 2020/12/6 13:55
 */

public class Ryuujinjakka extends SwordItem {

    public Ryuujinjakka(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient()) {
            if (selected) {
                Random random = new Random();
                int a = random.nextInt(123456);
                if (a % 5 == 0) {
                    randomDisplayTick(world, entity);
                }

            }
        } else {
            //该物品在背包里就要着火
            entity.setOnFireFor(1);
        }

    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(10);
        return super.postHit(stack, target, attacker);
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
        world.addParticle(ParticleTypes.FLAME, x1, y, z1, 0.0D, 0.05d, 0.0D);
    }
}
