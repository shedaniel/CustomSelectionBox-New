package me.shedaniel;

import me.shedaniel.gui.CSBSettingsGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class CSB {
    
    public static void drawNewOutlinedBoundingBox(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        voxelShapeIn.forEachEdge((p_195468_11_, p_195468_13_, p_195468_15_, p_195468_17_, p_195468_19_, p_195468_21_) -> {
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
