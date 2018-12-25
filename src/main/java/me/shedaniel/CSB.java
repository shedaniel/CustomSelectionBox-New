package me.shedaniel;

import me.shedaniel.gui.CSBSettingsGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import org.dimdev.riftloader.listener.InitializationListener;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.awt.*;
import java.util.Map;

import static me.shedaniel.CSBConfig.*;

public class CSB implements InitializationListener {
    
    @Override
    public void onInitialization() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.csb.json");
    }
    
    public static void drawBlinkingBlock(AxisAlignedBB boundingBox, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        
        if (alpha > 0.0F) {
            if (getBlinkSpeed() > 0 && !breakAnimation.equals(BreakAnimationType.ALPHA))
                alpha *= (float) Math.abs(Math.sin(System.currentTimeMillis() / 100.0D * getBlinkSpeed()));
            
            if (usingRainbow()) {
                final float millis = System.currentTimeMillis() % 10000L / 10000.0f;
                final Color color = Color.getHSBColor(millis, 0.8f, 0.8f);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
            } else
                GL11.glColor4f(getRed(), getGreen(), getBlue(), alpha);
            renderDown(boundingBox);
            renderUp(boundingBox);
            renderNorth(boundingBox);
            renderSouth(boundingBox);
            renderWest(boundingBox);
            renderEast(boundingBox);
        }
    }
    
    public static void drawNewOutlinedBoundingBox(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        voxelShapeIn.forEachEdge((p_195468_11_, p_195468_13_, p_195468_15_, p_195468_17_, p_195468_19_, p_195468_21_) ->
        {
            bufferbuilder.pos(p_195468_11_ + xIn, p_195468_13_ + yIn, p_195468_15_ + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(p_195468_17_ + xIn, p_195468_19_ + yIn, p_195468_21_ + zIn).color(red, green, blue, alpha).endVertex();
        });
        tessellator.draw();
    }
    
    public static void drawNewBlinkingBlock(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        voxelShapeIn.forEachBox((x1, y1, z1, x2, y2, z2) -> {
            //Up
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            
            //Down
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            
            //North
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            
            //South
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            
            //West
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            
            //East
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
        });
    }
    
    public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        
        if (color != -1) {
            worldRenderer.putColor4(color);
        }
        
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        
        if (color != -1) {
            worldRenderer.putColor4(color);
        }
        
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        
        if (color != -1) {
            worldRenderer.putColor4(color);
        }
        
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldRenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldRenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldRenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }
    
    public static void renderUp(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        tessellator.draw();
    }
    
    public static void renderDown(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        
        tessellator.draw();
    }
    
    public static void renderNorth(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        tessellator.draw();
    }
    
    public static void renderSouth(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }
    
    public static void renderWest(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        tessellator.draw();
    }
    
    public static void renderEast(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ).endVertex();
        tessellator.draw();
    }
    
    public static float getBreakProgress(Map<Integer, DestroyBlockProgress> map, EntityPlayer player, RayTraceResult block) {
        for(Map.Entry<Integer, DestroyBlockProgress> entry : map.entrySet()) {
            DestroyBlockProgress prg = entry.getValue();
            if (prg.getPosition().equals(block.getBlockPos())) {
                if (prg.getPartialBlockDamage() >= 0 && prg.getPartialBlockDamage() <= 10)
                    return prg.getPartialBlockDamage() / 10f;
            }
        }
        return 0f;
    }
    
    public static void openSettingsGUI() {
        Minecraft mc = Minecraft.getInstance();
        try {
            mc.currentScreen.close();
        } catch (Exception e) {
        }
        mc.gameSettings.saveOptions();
        mc.displayGuiScreen(new CSBSettingsGUI(null));
    }
}
