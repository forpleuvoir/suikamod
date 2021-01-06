package com.forpleuvoir.suika.mixin.server;

import com.forpleuvoir.suika.server.data.WarpPoint;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.mixin.server
 * @class_name ServerPlayerEntityMixin
 * @create_time 2020/11/23 14:19
 */
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {


    @Shadow
    public abstract void sendSystemMessage(Text message, UUID senderUuid);

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }


    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(CallbackInfo ci) {
        DimensionType type = this.world.getDimension();
        WarpPoint.setBack(getUuidAsString(), new WarpPoint.Pos(getPos(), WarpPoint.getDimension(type)));
        Text text = Texts.bracketed(new TranslatableText("输入 §c/back §r返回死亡地点")).styled((style) -> style.withColor(Formatting.WHITE).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/back")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.coordinates.tooltip"))));
        sendMessage(text,false);
    }

}
