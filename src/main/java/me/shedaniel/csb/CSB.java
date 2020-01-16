package me.shedaniel.csb;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.csb.gui.CSBSettingsGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.shape.VoxelShape;

import static me.shedaniel.csb.CSBConfig.getThickness;

public class CSB {
    
    public static void drawNewOutlinedBoundingBox(Matrix4f matrix4f, VertexConsumer vertexConsumerD, VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.depthMask(false);
        RenderSystem.color4f(red, green, blue, alpha);
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();
        RenderSystem.disableTexture();
        RenderSystem.lineWidth(getThickness());
        vertexConsumer.begin(1, VertexFormats.POSITION);
        voxelShapeIn.forEachEdge((x1, y1, z1, x2, y2, z2) -> edge(vertexConsumer, x1, y1, z1, x2, y2, z2, xIn, yIn, zIn));
        tessellator.draw();
        RenderSystem.enableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.popMatrix();
    }
    
    private static void edge(BufferBuilder vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, double xIn, double yIn, double zIn) {
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).next();
    }
    
    public static void drawNewBlinkingBlock(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.depthMask(false);
        RenderSystem.color4f(red, green, blue, alpha);
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableCull();
        RenderSystem.disableTexture();
        voxelShapeIn.forEachBox((x1, y1, z1, x2, y2, z2) -> box(tessellator, vertexConsumer, x1, y1, z1, x2, y2, z2, xIn, yIn, zIn));
        RenderSystem.enableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.popMatrix();
    }
    
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0F) {
            r = g = b = (int) (brightness * 255.0F + 0.5F);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0F;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - saturation * (1.0F - f));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (t * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 1:
                    r = (int) (q * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 2:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (t * 255.0F + 0.5F);
                    break;
                case 3:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (q * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 4:
                    r = (int) (t * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 5:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (q * 255.0F + 0.5F);
            }
        }
        
        return -16777216 | r << 16 | g << 8 | b;
    }
    
    private static void box(Tessellator tessellator, BufferBuilder vertexConsumer, double x1, double y1, double z1, double x2, double y2, double z2, double xIn, double yIn, double zIn) {
        x1 -= 0.005;
        y1 -= 0.005;
        z1 -= 0.005;
        x2 += 0.005;
        y2 += 0.005;
        z2 += 0.005;
        vertexConsumer.begin(7, VertexFormats.POSITION);
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y1 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        tessellator.draw();
        
        //Down
        vertexConsumer.begin(7, VertexFormats.POSITION);
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y2 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y2 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y2 + yIn), (float) (z1 + zIn)).next();
        tessellator.draw();
        
        //North
        vertexConsumer.begin(7, VertexFormats.POSITION);
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y2 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y2 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        tessellator.draw();
        
        //South
        vertexConsumer.begin(7, VertexFormats.POSITION);
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y1 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z2 + zIn)).next();
        tessellator.draw();
        
        //West
        vertexConsumer.begin(7, VertexFormats.POSITION);
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y2 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x1 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        tessellator.draw();
        
        //East
        vertexConsumer.begin(7, VertexFormats.POSITION);
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y2 + yIn), (float) (z1 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y2 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y1 + yIn), (float) (z2 + zIn)).next();
        vertexConsumer.vertex((float) (x2 + xIn), (float) (y1 + yIn), (float) (z1 + zIn)).next();
        tessellator.draw();
    }
    
    public static void openSettingsGUI(MinecraftClient client, Screen parent) {
        client.openScreen(new CSBSettingsGui(parent));
    }
    
}
