package com.forpleuvoir.chatbubbles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.AffineTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.chatbubbles
 * @className RenderPlayerChatBubbles
 * @createTime 2020/10/25 11:50
 */
public class RenderPlayerChatBubbles extends PlayerEntityRenderer {
    ArrayList<ChatBubbleMessage> relevantMessages;
    float r;
    float g;
    float b;
    int lineWidth;

    public RenderPlayerChatBubbles(EntityRenderDispatcher renderManager) {
        this(renderManager, false);
    }

    public RenderPlayerChatBubbles(EntityRenderDispatcher renderManager, boolean smallArms) {
        super(renderManager, smallArms);
        this.r = 0.0F;
        this.g = 0.0F;
        this.b = 0.0F;
        this.lineWidth = 4;
        System.out.println("**Chat Bubble Renderer Initialized**");
        this.relevantMessages = new ArrayList<>();
    }

    @Override
    public void renderLabelIfPresent(AbstractClientPlayerEntity par1EntityLivingBase, Text label, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int color) {
        super.renderLabelIfPresent(par1EntityLivingBase, label, matrixStack, vertexConsumerProvider, color);
        if (par1EntityLivingBase != this.getRenderManager().camera.getFocusedEntity()) {
            GLShim.glPushMatrix();
            GLShim.glRotatef(MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPitch(), 1.0F, 0.0F, 0.0F);
            GLShim.glRotatef(MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getYaw() - 180.0F, 0.0F, 1.0F, 0.0F);
            try {
                String myName = this.scrubCodes(par1EntityLivingBase.getName().asString());
                if (myName.length() > 0) {
                    this.r = (128.0F + (float) (myName.charAt(0) % 32 * 4)) / 256.0F;
                }

                if (myName.length() > 1) {
                    this.g = (128.0F + (float) (myName.charAt(1) % 32 * 4)) / 256.0F;
                }

                if (myName.length() > 2) {
                    this.b = (128.0F + (float) (myName.charAt(2) % 32 * 4)) / 256.0F;
                }
                int currentTime = ChatBubbles.instance.game.inGameHud.getTicks();
                this.relevantMessages = ChatBubbles.instance.getMessagesByAuthor(myName);
                int lines = 2;
                String[] messageLines;
                for (Iterator var9 = this.relevantMessages.iterator(); var9.hasNext(); lines = lines + messageLines.length + 1) {
                    ChatBubbleMessage message = ( ChatBubbleMessage) var9.next();
                    messageLines = message.getMessageLines();
                    float remainingTime = (float) ( ChatBubbles.instance.MESSAGELIFETIME - (currentTime - message.getUpdatedCounter()));
                    Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
                    float partialTicks = MinecraftClient.getInstance().getTickDelta();
                    assert cameraEntity != null;
                    double renderPosX = cameraEntity.lastRenderX + (cameraEntity.getX() - cameraEntity.lastRenderX) *  partialTicks;
                    double renderPosY = cameraEntity.lastRenderY + (cameraEntity.getY() - cameraEntity.lastRenderY) *  partialTicks;
                    double renderPosZ = cameraEntity.lastRenderZ + (cameraEntity.getZ() - cameraEntity.lastRenderZ) *  partialTicks;
                    double entityX = par1EntityLivingBase.lastRenderX + (par1EntityLivingBase.getX() - par1EntityLivingBase.lastRenderX) *  partialTicks;
                    double entityY = par1EntityLivingBase.lastRenderY + (par1EntityLivingBase.getY() - par1EntityLivingBase.lastRenderY) *  partialTicks;
                    double entityZ = par1EntityLivingBase.lastRenderZ + (par1EntityLivingBase.getZ() - par1EntityLivingBase.lastRenderZ) *  partialTicks;
                    this.renderMessage(lines, messageLines, remainingTime, par1EntityLivingBase, entityX - renderPosX, entityY - renderPosY, entityZ - renderPosZ);
                }
            } catch (Exception var27) {
                System.out.println("***Exception in bubbleRenderer***: " + var27);
            }
            GLShim.glPopMatrix();
        }
    }

    protected void renderMessage(int lines, String[] message, float remainingTime, PlayerEntity par1EntityPlayer, double par2, double par4, double par6) {
        double var10 = this.dispatcher.getSquaredDistanceToCamera(par1EntityPlayer);
        float var12 = par1EntityPlayer.isSneaking() ? 32.0F : 64.0F;
        int brightness = 15728880;
        int brightMod = brightness % 65536;
        int brightDiv = brightness / 65536;
        RenderSystem.glMultiTexCoord2f(33985, (float) brightMod / 1.0F, (float) brightDiv / 1.0F);
        if (var10 <=  (var12 * var12)) {
            TextRenderer fontRenderer = this.dispatcher.getTextRenderer();
            float var13 = 1.6F;
            float var14 = 0.016666668F * var13;
            GLShim.glPushMatrix();
            GLShim.glTranslatef((float) par2 + 0.0F, (float) par4 + 0.7F, (float) par6);
            GLShim.glNormal3f(0.0F, 1.0F, 0.0F);
            GLShim.glRotatef(-this.dispatcher.camera.getYaw(), 0.0F, 1.0F, 0.0F);
            GLShim.glRotatef(this.dispatcher.camera.getPitch(), 1.0F, 0.0F, 0.0F);
            GLShim.glScalef(-var14, -var14, var14);
            GLShim.glDisable(2896);
            GLShim.glDisable(2912);
            GLShim.glDepthMask(false);
            GLShim.glEnable(3042);
            GLShim.glBlendFunc(770, 771);
            int maxWidth = 8;

            int left;
            for (left = message.length - 1; left >= 0; --left) {
                int var17 = fontRenderer.getWidth(message[left]) / 2;
                if (var17 > maxWidth) {
                    maxWidth = var17;
                }
            }

            left = -maxWidth + 1;
            Tessellator var15 = Tessellator.getInstance();
            int top = -(lines + message.length - 1) * 9 + 2;
            int bottom = -lines * 9 + 6;
            float alphaFadeFactor = 1.0F;
            if (remainingTime < 20.0F) {
                alphaFadeFactor = remainingTime / 20.0F;
                alphaFadeFactor *= alphaFadeFactor;
            }

            GLShim.glEnable(32823);
            GLShim.glPolygonOffset(1.0F, 5.0F);
            GLShim.glEnable(3553);
            GLShim.glDisable(2929);
            this.drawBubble(1.0F, 1.0F, 1.0F, Math.min(alphaFadeFactor, 0.25F), (float) top, (float) bottom, (float) left, (float) maxWidth, var15, 5.0F);
            GLShim.glPolygonOffset(1.0F, 3.0F);
            GLShim.glEnable(2929);
            GLShim.glDepthMask(true);
            this.drawBubble(this.r, this.g, this.b, alphaFadeFactor, (float) top, (float) bottom, (float) left, (float) maxWidth, var15, 3.0F);

            for (int t = message.length - 1; t >= 0; --t) {
                GLShim.glPolygonOffset(1.0F, 1.0F);
                GLShim.glDisable(2929);
                int var16 = -lines * 9;
                alphaFadeFactor = Math.max(alphaFadeFactor, 0.016F);
                int textFade = (int) (alphaFadeFactor * 255.0F);
                int textFadeBG = Math.min(textFade, 127) << 24;
                textFade <<= 24;
                Matrix4f matrix4f = AffineTransformation.identity().getMatrix();
                VertexConsumerProvider.Immediate vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();
                fontRenderer.draw(message[t], (float) (-maxWidth), (float) var16, textFadeBG, false, matrix4f, vertexConsumerProvider, true, 0, 15728880);
                vertexConsumerProvider.draw();
                GLShim.glEnable(2929);
                GLShim.glDepthMask(true);
                fontRenderer.draw(new MatrixStack(), message[t], (float) (-maxWidth), (float) var16, textFade);
                ++lines;
            }

            GLShim.glDisable(32823);
            GLShim.glEnable(2912);
            GLShim.glEnable(2896);
            GLShim.glDisable(3042);
            GLShim.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GLShim.glPopMatrix();
        }

    }

    private void drawBubble(float r, float g, float b, float a, float top, float bottom, float left, float right, Tessellator var15, float offset) {
        this.img("images/chatbubble.png");
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        vertexBuffer. vertex( left,  top, 0.0D).texture (0.0625F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  bottom, 0.0D).texture (0.0625F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  bottom, 0.0D).texture (0.9375F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  top, 0.0D).texture (0.9375F, 0.125F).color (r, g, b, a).next ();
        var15.draw ();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( (left - (float) this.lineWidth),  top, 0.0D).texture (0.0F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (left - (float) this.lineWidth),  bottom, 0.0D).texture (0.0F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  bottom, 0.0D).texture (0.0625F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  top, 0.0D).texture (0.0625F, 0.125F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( right,  top, 0.0D).texture (0.9375F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  bottom, 0.0D).texture (0.9375F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (right + (float) this.lineWidth),  bottom, 0.0D).texture (1.0F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (right + (float) this.lineWidth),  top, 0.0D).texture (1.0F, 0.125F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( left,  (top - (float) this.lineWidth), 0.0D).texture (0.0625F, 0.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  top, 0.0D).texture (0.0625F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  top, 0.0D).texture (0.9375F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  (top - (float) this.lineWidth), 0.0D).texture (0.9375F, 0.0F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( left,  bottom, 0.0D).texture (0.0625F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  (bottom + (float) this.lineWidth), 0.0D).texture (0.0625F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex(-2.0D,  (bottom + (float) this.lineWidth), 0.0D).texture (0.5F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex(-2.0D,  bottom, 0.0D).texture (0.5F, 0.875F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex(-2.0D,  bottom, 0.0D).texture (0.5F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex(-2.0D,  (bottom + (float) this.lineWidth), 0.0D).texture (0.5F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex(6.0D,  (bottom + (float) this.lineWidth), 0.0D).texture (0.625F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex(6.0D,  bottom, 0.0D).texture (0.625F, 0.875F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex(6.0D,  bottom, 0.0D).texture (0.625F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex(6.0D,  (bottom + (float) this.lineWidth), 0.0D).texture (0.625F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  (bottom + (float) this.lineWidth), 0.0D).texture (0.9375F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  bottom, 0.0D).texture (0.9375F, 0.875F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( (left - (float) this.lineWidth),  (top - (float) this.lineWidth), 0.0D).texture (0.0F, 0.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (left - (float) this.lineWidth),  top, 0.0D).texture (0.0F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  top, 0.0D).texture (0.0625F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  (top - (float) this.lineWidth), 0.0D).texture (0.0625F, 0.0F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( right,  (top - (float) this.lineWidth), 0.0D).texture (0.9375F, 0.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  top, 0.0D).texture (0.9375F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (right + (float) this.lineWidth),  top, 0.0D).texture (1.0F, 0.125F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (right + (float) this.lineWidth),  (top - (float) this.lineWidth), 0.0D).texture (1.0F, 0.0F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( (left - (float) this.lineWidth),  bottom, 0.0D).texture (0.0F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (left - (float) this.lineWidth),  (bottom + (float) this.lineWidth), 0.0D).texture (0.0F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  (bottom + (float) this.lineWidth), 0.0D).texture (0.0625F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( left,  bottom, 0.0D).texture (0.0625F, 0.875F).color (r, g, b, a).next ();
        var15.draw();
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex( right,  bottom, 0.0D).texture (0.9375F, 0.875F).color (r, g, b, a).next ();
        vertexBuffer. vertex( right,  (bottom + (float) this.lineWidth), 0.0D).texture (0.9375F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (right + (float) this.lineWidth),  (bottom + (float) this.lineWidth), 0.0D).texture (1.0F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex( (right + (float) this.lineWidth),  bottom, 0.0D).texture (1.0F, 0.875F).color (r, g, b, a).next ();
        var15.draw();
        GLShim.glPolygonOffset(1.0F, offset - 1.0F);
        this.img("images/chatbubbletail.png");
        vertexBuffer.begin (7, VertexFormats.POSITION_TEXTURE_COLOR );
        vertexBuffer. vertex(-2.0D,  (bottom + (float) this.lineWidth), 0.0D).texture (0.0F, 0.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex(-2.0D,  (bottom + (float) this.lineWidth + 8.0F), 0.0D).texture (0.0F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex(6.0D,  (bottom + (float) this.lineWidth + 8.0F), 0.0D).texture (1.0F, 1.0F).color (r, g, b, a).next ();
        vertexBuffer. vertex(6.0D,  (bottom + (float) this.lineWidth), 0.0D).texture (1.0F, 0.0F).color (r, g, b, a).next ();
        var15.draw();
    }

    public void img(String paramStr) {
        this.dispatcher.textureManager.bindTexture(new Identifier("chatbubbles", paramStr));
    }

    private String scrubCodes(String string) {
        string = string.replaceAll("(\\xA7.)", "");
        return string;
    }
}
