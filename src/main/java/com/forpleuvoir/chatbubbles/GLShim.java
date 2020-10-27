package com.forpleuvoir.chatbubbles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author forpleuvoir
 * @belongsProject suikamod
 * @belongsPackage com.forpleuvoir.chatbubbles
 * @className GLShim
 * @createTime 2020/10/25 12:08
 */
public class GLShim {
    public static final int GL_ALPHA_TEST = 3008;
    public static final int GL_BLEND = 3042;
    public static final int GL_CLAMP = 10496;
    public static final int GL_CLAMP_TO_EDGE = 33071;
    public static final int GL_COLOR_BUFFER_BIT = 16384;
    public static final int GL_COLOR_CLEAR_VALUE = 3106;
    public static final int GL_CULL_FACE = 2884;
    public static final int GL_DEPTH_BUFFER_BIT = 256;
    public static final int GL_DST_ALPHA = 772;
    public static final int GL_DST_COLOR = 774;
    public static final int GL_FOG = 2912;
    public static final int GL_DEPTH_TEST = 2929;
    public static final int GL_FLAT = 7424;
    public static final int GL_FOG_DENSITY = 2914;
    public static final int GL_FOG_END = 2916;
    public static final int GL_FOG_MODE = 2917;
    public static final int GL_FOG_START = 2915;
    public static final int GL_GENERATE_MIPMAP = 33169;
    public static final int GL_GREATER = 516;
    public static final int GL_LIGHTING = 2896;
    public static final int GL_LINEAR = 9729;
    public static final int GL_LINES = 1;
    public static final int GL_LINEAR_MIPMAP_LINEAR = 9987;
    public static final int GL_LINEAR_MIPMAP_NEAREST = 9985;
    public static final int GL_MODELVIEW = 5888;
    public static final int GL_NEAREST = 9728;
    public static final int GL_NEAREST_MIPMAP_LINEAR = 9986;
    public static final int GL_NEAREST_MIPMAP_NEAREST = 9984;
    public static final int GL_NORMALIZE = 2977;
    public static final int GL_ONE = 1;
    public static final int GL_ONE_MINUS_DST_ALPHA = 773;
    public static final int GL_ONE_MINUS_DST_COLOR = 775;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_ONE_MINUS_SRC_COLOR = 769;
    public static final int GL_PACK_ALIGNMENT = 3333;
    public static final int GL_POLYGON_OFFSET_FILL = 32823;
    public static final int GL_PROJECTION = 5889;
    public static final int GL_PROJECTION_MATRIX = 2983;
    public static final int GL_QUADS = 7;
    public static final int GL_RGBA = 6408;
    public static final int GL_SMOOTH = 7425;
    public static final int GL_SCISSOR_TEST = 3089;
    public static final int GL_SRC_ALPHA = 770;
    public static final int GL_TEXTURE_2D = 3553;
    public static final int GL_TEXTURE_HEIGHT = 4097;
    public static final int GL_TEXTURE_MAG_FILTER = 10240;
    public static final int GL_TEXTURE_MIN_FILTER = 10241;
    public static final int GL_TEXTURE_WIDTH = 4096;
    public static final int GL_TEXTURE_WRAP_S = 10242;
    public static final int GL_TEXTURE_WRAP_T = 10243;
    public static final int GL_TRUE = 1;
    public static final int GL_TRANSFORM_BIT = 4096;
    public static final int GL_UNPACK_ALIGNMENT = 3317;
    public static final int GL_UNPACK_ROW_LENGTH = 3314;
    public static final int GL_UNPACK_SKIP_PIXELS = 3316;
    public static final int GL_UNPACK_SKIP_ROWS = 3315;
    public static final int GL_UNSIGNED_BYTE = 5121;
    public static final int GL_UNSIGNED_INT_8_8_8_8 = 32821;
    public static final int GL_VIEWPORT_BIT = 2048;
    public static final int GL_ZERO = 0;
    public static final int GL_BGRA = 32993;
    public static final int GL_RESCALE_NORMAL = 32826;
    public static final int GL_UNSIGNED_INT_8_8_8_8_REV = 33639;

    public GLShim() {
    }

    public static void glEnable(int attrib) {
        switch (attrib) {
            case 2884:
                RenderSystem.enableCull();
                break;
            case 2896:
                RenderSystem.enableLighting();
                break;
            case 2912:
                RenderSystem.enableFog();
                break;
            case 2929:
                RenderSystem.enableDepthTest();
                break;
            case 3008:
                RenderSystem.enableAlphaTest();
                break;
            case 3042:
                RenderSystem.enableBlend();
                break;
            case 3089:
                GL11.glEnable(3089);
                break;
            case 3553:
                RenderSystem.enableTexture();
                break;
            case 32823:
                RenderSystem.enablePolygonOffset();
                break;
            case 32826:
                RenderSystem.enableRescaleNormal();
        }

    }

    public static void glDisable(int attrib) {
        switch (attrib) {
            case 2884:
                RenderSystem.disableCull();
                break;
            case 2896:
                RenderSystem.disableLighting();
                break;
            case 2912:
                RenderSystem.disableFog();
                break;
            case 2929:
                RenderSystem.disableDepthTest();
                break;
            case 3008:
                RenderSystem.disableAlphaTest();
                break;
            case 3042:
                RenderSystem.disableBlend();
                break;
            case 3089:
                GL11.glDisable(3089);
                break;
            case 3553:
                RenderSystem.disableTexture();
                break;
            case 32823:
                RenderSystem.disablePolygonOffset();
                break;
            case 32826:
                RenderSystem.disableRescaleNormal();
        }

    }

    public static void glFogi(int pname, int param) {
        switch (pname) {
            case 2917:
                GlStateManager.FogMode fogMode = GlStateManager.FogMode.EXP;
                switch (param) {
                    case 2048:
                        fogMode = GlStateManager.FogMode.EXP;
                        break;
                    case 2049:
                        fogMode = GlStateManager.FogMode.EXP2;
                        break;
                    case 9729:
                        fogMode = GlStateManager.FogMode.LINEAR;
                }

                RenderSystem.fogMode(fogMode);
            default:
        }
    }

    public static void glFogf(int pname, float param) {
        switch (pname) {
            case 2914:
                RenderSystem.fogDensity(param);
                break;
            case 2915:
                RenderSystem.fogStart(param);
                break;
            case 2916:
                RenderSystem.fogEnd(param);
        }

    }

    public static void glAlphaFunc(int func, float ref) {
        RenderSystem.alphaFunc(func, ref);
    }

    public static void glBlendFunc(int sfactor, int dfactor) {
        RenderSystem.blendFunc(sfactor, dfactor);
    }

    public static void glBlendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
        RenderSystem.blendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
    }

    public static void glClear(int mask) {
        RenderSystem.clear(mask, MinecraftClient.IS_SYSTEM_MAC);
    }

    public static void glClearColor(float red, float green, float blue, float alpha) {
        RenderSystem.clearColor(red, green, blue, alpha);
    }

    public static void glClearDepth(double depth) {
        RenderSystem.clearDepth(depth);
    }

    public static void glColor3f(float red, float green, float blue) {
        RenderSystem.color3f(red, green, blue);
    }

    public static void glColor4f(float red, float green, float blue, float alpha) {
        RenderSystem.color4f(red, green, blue, alpha);
    }

    public static void glColor3ub(int red, int green, int blue) {
        RenderSystem.color4f((float) red / 255.0F, (float) green / 255.0F, (float) blue / 255.0F, 1.0F);
    }

    public static void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        RenderSystem.colorMask(red, green, blue, alpha);
    }

    public static void glColorMaterial(int face, int mode) {
        RenderSystem.colorMaterial(face, mode);
    }

    public static void glDeleteTextures(int id) {
        RenderSystem.deleteTexture(id);
    }

    public static void glDepthFunc(int func) {
        RenderSystem.depthFunc(func);
    }

    public static void glDepthMask(boolean flag) {
        RenderSystem.depthMask(flag);
    }

    public static int glGenTextures() {
        return GlStateManager.genTextures();
    }

    public static void glGetFloat(int pname, FloatBuffer params) {
        GlStateManager.getFloat(pname, params);
    }

    public static void glGetTexImage(int tex, int level, int format, int type, long pixels) {
        GlStateManager.getTexImage (tex, level, format, type, pixels);
    }

    public static int glGetTexLevelParameteri(int target, int level, int pname) {
        return GlStateManager.getTexLevelParameter (target, level, pname);
    }

    public static void glLoadIdentity() {
        RenderSystem.loadIdentity();
    }

    public static void glLogicOp(int opcode) {
        GlStateManager.logicOp (opcode);
    }

    public static void glMatrixMode(int mode) {
        RenderSystem.matrixMode(mode);
    }

    public static void glMultMatrix(FloatBuffer m) {
        GlStateManager.multMatrix (m);
    }

    public static void glNormal3f(float nx, float ny, float nz) {
        RenderSystem.normal3f(nx, ny, nz);
    }

    public static void glOrtho(double left, double right, double bottom, double top, double zNear, double zFar) {
        RenderSystem.ortho(left, right, bottom, top, zNear, zFar);
    }

    public static void glPixelStorei(int parameterName, int parameter) {
        RenderSystem.pixelStore(parameterName, parameter);
    }

    public static void glPolygonOffset(float factor, float units) {
        RenderSystem.polygonOffset(factor, units);
    }

    public static void glPopAttrib() {
        RenderSystem.popAttributes();
    }

    public static void glPopMatrix() {
        RenderSystem.popMatrix();
    }

    public static void glPushAttrib() {
        RenderSystem.pushLightingAttributes();
    }

    public static void glPushMatrix() {
        RenderSystem.pushMatrix();
    }

    public static void glRotatef(float angle, float x, float y, float z) {
        RenderSystem.rotatef(angle, x, y, z);
    }

    public static void glScaled(double x, double y, double z) {
        RenderSystem.scaled(x, y, z);
    }

    public static void glScalef(float x, float y, float z) {
        RenderSystem.scalef(x, y, z);
    }

    public static void glSetActiveTextureUnit(int texture) {
        RenderSystem.activeTexture(texture);
    }

    public static void glShadeModel(int mode) {
        RenderSystem.shadeModel(mode);
    }

    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
        GlStateManager.texImage2D (target, level, internalformat, width, height, border, format, type, pixels);
    }

    public static void glTexParameterf(int target, int pname, float param) {
        GlStateManager.texParameter (target, pname, param);
    }

    public static void glTexParameteri(int target, int pname, int param) {
        RenderSystem.texParameter(target, pname, param);
    }

    public static void glTexSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, long memAddress) {
        GlStateManager.texSubImage2D (target, level, xOffset, yOffset, width, height, format, type, memAddress);
    }

    public static void glTranslated(double x, double y, double z) {
        RenderSystem.translated(x, y, z);
    }

    public static void glTranslatef(float x, float y, float z) {
        RenderSystem.translatef(x, y, z);
    }

    public static void glViewport(int x, int y, int width, int height) {
        RenderSystem.viewport(x, y, width, height);
    }

    public static void glBegin(int mode) {
        GL11.glBegin(mode);
    }

    public static void glBindTexture(int target, int texture) {
        switch (target) {
            case 3553:
                RenderSystem.bindTexture(texture);
                break;
            default:
                GL11.glBindTexture(target, texture);
        }

    }

    public static void glEnd() {
        GL11.glEnd();
    }

    public static boolean glGetBoolean(int pname) {
        return GL11.glGetBoolean(pname);
    }

    public static void glGetTexImage(int tex, int level, int format, int type, ByteBuffer pixels) {
        GL11.glGetTexImage(tex, level, format, type, pixels);
    }

    public static void glGetTexImage(int tex, int level, int format, int type, IntBuffer pixels) {
        GL11.glGetTexImage(tex, level, format, type, pixels);
    }

    public static void glLoadMatrix(FloatBuffer buf) {
        GL11.glLoadMatrixf(buf);
    }

    public static void glPushAttrib(int mask) {
        GL11.glPushAttrib(mask);
    }

    public static void glScissor(int x, int y, int width, int height) {
        GL11.glScissor(x, y, width, height);
    }

    public static void glTexImage2D(int glTexture2d, int level, int glRgba, int width, int height, int border, int format, int type, ByteBuffer pixels) {
        GL11.glTexImage2D(glTexture2d, level, glRgba, width, height, border, format, type, pixels);
    }

    public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
        GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    public static void glVertex2f(float x, float y) {
        GL11.glVertex2f(x, y);
    }

    public static void glVertex3f(float x, float y, float z) {
        GL11.glVertex3f(x, y, z);
    }
}
