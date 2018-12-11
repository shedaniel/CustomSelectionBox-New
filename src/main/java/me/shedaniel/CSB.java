package me.shedaniel;

import me.shedaniel.gui.CSBSettingsGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.PartiallyBrokenBlockEntry;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexBuffer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BoundingBox;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;

import static me.shedaniel.CSBConfig.*;

public class CSB {
	
	public static void drawBlinkingBlock(BoundingBox par1BoundingBox, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		
		if (alpha > 0.0F) {
			if (getBlinkSpeed() > 0 && breakAnimation != ALPHA)
				alpha *= (float) Math.abs(Math.sin(System.currentTimeMillis() / 100.0D * getBlinkSpeed()));
			
			if (usingRainbow()) {
				final double millis = System.currentTimeMillis() % 10000L / 10000.0f;
				final Color color = Color.getHSBColor((float) millis, 0.8f, 0.8f);
				GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
			} else
				GL11.glColor4f(getRed(), getGreen(), getBlue(), alpha);
			renderDown(par1BoundingBox);
			renderUp(par1BoundingBox);
			renderNorth(par1BoundingBox);
			renderSouth(par1BoundingBox);
			renderWest(par1BoundingBox);
			renderEast(par1BoundingBox);
		}
	}
	
	public static void drawOutlinedBoundingBox(BoundingBox boundingBox, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getVertexBuffer();
		worldRenderer.begin(1, VertexFormats.POSITION);
		
		if (color != -1) {
			worldRenderer.setQuadColor(color);
		}
		
		// OG: worldRenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		worldRenderer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		tessellator.draw();
		worldRenderer.begin(3, VertexFormats.POSITION);
		
		if (color != -1) {
			worldRenderer.setQuadColor(color);
		}
		
		worldRenderer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		tessellator.draw();
		worldRenderer.begin(1, VertexFormats.POSITION);
		
		if (color != -1) {
			worldRenderer.setQuadColor(color);
		}
		
		worldRenderer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.vertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
		worldRenderer.vertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
		worldRenderer.vertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
		tessellator.draw();
	}
	
	public static void renderUp(BoundingBox par1BoundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getVertexBuffer();
		
		worldRenderer.begin(GL11.GL_QUADS, VertexFormats.POSITION);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.minY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.minY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.minZ);
		tessellator.draw();
	}
	
	public static void renderDown(BoundingBox par1BoundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getVertexBuffer();
		
		worldRenderer.begin(GL11.GL_QUADS, VertexFormats.POSITION);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.maxY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.maxY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.maxY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.maxY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.maxY, par1BoundingBox.minZ);
		
		tessellator.draw();
	}
	
	public static void renderNorth(BoundingBox par1BoundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getVertexBuffer();
		
		worldRenderer.begin(GL11.GL_QUADS, VertexFormats.POSITION);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.maxY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.maxY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.minY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.minZ);
		tessellator.draw();
	}
	
	public static void renderSouth(BoundingBox par1BoundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getVertexBuffer();
		
		worldRenderer.begin(GL11.GL_QUADS, VertexFormats.POSITION);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.minY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.maxY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.maxY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.maxZ);
		tessellator.draw();
	}
	
	public static void renderWest(BoundingBox par1BoundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getVertexBuffer();
		
		worldRenderer.begin(GL11.GL_QUADS, VertexFormats.POSITION);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.maxY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.maxY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.minX, par1BoundingBox.minY, par1BoundingBox.minZ);
		tessellator.draw();
	}
	
	public static void renderEast(BoundingBox par1BoundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getVertexBuffer();
		
		worldRenderer.begin(GL11.GL_QUADS, VertexFormats.POSITION);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.minY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.maxY, par1BoundingBox.minZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.maxY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.minY, par1BoundingBox.maxZ);
		worldRenderer.vertex(par1BoundingBox.maxX, par1BoundingBox.minY, par1BoundingBox.minZ);
		tessellator.draw();
	}
	
	public static float getBreakProgress(Map<Integer, PartiallyBrokenBlockEntry> map, PlayerEntity player, HitResult block) {
		for (Map.Entry<Integer, PartiallyBrokenBlockEntry> entry : map.entrySet()) {
			PartiallyBrokenBlockEntry prg = entry.getValue();
			if (prg.getPos().equals(block.getBlockPos())) {
				if (prg.getStage() >= 0 && prg.getStage() <= 10)
					return prg.getStage() / 10f;
			}
		}
		return 0f;
	}
	
	public static void openSettingsGUI() {
		MinecraftClient client = MinecraftClient.getInstance();
		client.settings.write();
		client.openGui((Gui) null);
		client.openGui(new CSBSettingsGUI(client.currentGui));
	}
}
