package me.shedaniel.csb;

import me.shedaniel.csb.gui.CSBSettingsGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.PartiallyBrokenBlockEntry;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class CSB {
    
    public static void drawNewOutlinedBoundingBox(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBufferBuilder();
        bufferbuilder.begin(1, VertexFormats.POSITION_COLOR);
        voxelShapeIn.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
        });
        tessellator.draw();
    }
    
    public static void drawNewBlinkingBlock(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBufferBuilder();
        voxelShapeIn.forEachBox((x1, y1, z1, x2, y2, z2) -> {
            //Up
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            tessellator.draw();
            
            //Down
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            tessellator.draw();
            
            //North
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            tessellator.draw();
            
            //South
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            tessellator.draw();
            
            //West
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x1 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            tessellator.draw();
            
            //East
            bufferbuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
            bufferbuilder.vertex(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y2 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y2 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y1 + yIn, z2 + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(x2 + xIn, y1 + yIn, z1 + zIn).color(red, green, blue, alpha).next();
            tessellator.draw();
        });
    }
    
    public static float getBreakProgress(Map<Integer, PartiallyBrokenBlockEntry> map, HitResult block) {
        for(Map.Entry<Integer, PartiallyBrokenBlockEntry> entry : map.entrySet()) {
            PartiallyBrokenBlockEntry prg = entry.getValue();
            if (prg.getPos().equals(new BlockPos(block.getPos())) && prg.getStage() >= 0 && prg.getStage() <= 10)
                return prg.getStage() / 10f;
        }
        return 0f;
    }
    
    public static void openSettingsGUI(MinecraftClient client, Screen parent) {
        client.openScreen(new CSBSettingsGui(parent));
    }
    
}
