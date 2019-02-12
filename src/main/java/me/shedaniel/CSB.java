package me.shedaniel;

import me.shedaniel.gui.CSBSettingsGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.PartiallyBrokenBlockEntry;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.shape.VoxelShape;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class CSB {
    
    public static void drawNewOutlinedBoundingBox(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBufferBuilder();
        bufferbuilder.begin(1, VertexFormats.POSITION_COLOR);
        voxelShapeIn.method_1104((p_195468_11_, p_195468_13_, p_195468_15_, p_195468_17_, p_195468_19_, p_195468_21_) -> {
            bufferbuilder.vertex(p_195468_11_ + xIn, p_195468_13_ + yIn, p_195468_15_ + zIn).color(red, green, blue, alpha).next();
            bufferbuilder.vertex(p_195468_17_ + xIn, p_195468_19_ + yIn, p_195468_21_ + zIn).color(red, green, blue, alpha).next();
        });
        tessellator.draw();
    }
    
    public static void drawNewBlinkingBlock(VoxelShape voxelShapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBufferBuilder();
        voxelShapeIn.method_1089((x1, y1, z1, x2, y2, z2) -> {
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
    
    public static float getBreakProgress(Map<Integer, PartiallyBrokenBlockEntry> map, Entity entity, HitResult block) {
        for(Map.Entry<Integer, PartiallyBrokenBlockEntry> entry : map.entrySet()) {
            PartiallyBrokenBlockEntry prg = entry.getValue();
            if (prg.getPos().equals(((BlockHitResult) block).getPos()))
                if (prg.getStage() >= 0 && prg.getStage() <= 10)
                    return prg.getStage() / 10f;
        }
        return 0f;
    }
    
    public static void openSettingsGUI() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.options.write();
        client.openScreen(new CSBSettingsGui(MinecraftClient.getInstance().currentScreen));
    }
    
}
