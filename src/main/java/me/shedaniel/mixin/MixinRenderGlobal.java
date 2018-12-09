package me.shedaniel.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldEventListener;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Map;

import static me.shedaniel.CSB.*;
import static me.shedaniel.CSBConfig.*;

@Mixin(WorldRenderer.class)
public abstract class MixinRenderGlobal implements IWorldEventListener, AutoCloseable, IResourceManagerReloadListener {
	
	@Shadow
	private Map<Integer, DestroyBlockProgress> damagedBlocks;
	@Shadow
	private Minecraft mc;
	@Shadow
	private WorldClient world;
	
	@Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
	public void drawSelectionBox(EntityPlayer player, RayTraceResult trace, int execute, float partialTicks, CallbackInfo ci) {
		ci.cancel();
		if (execute == 0 && trace.type == RayTraceResult.Type.BLOCK) {
			BlockPos blockPos = trace.getBlockPos();
			IBlockState blockState = this.world.getBlockState(blockPos);
			if (!blockState.isAir() && this.world.getWorldBorder().contains(blockPos)) {
				float breakProgress = getBreakProgress(damagedBlocks, player, trace);
				GlStateManager.enableBlend();
				GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.lineWidth(Math.max(2.5F, (float) mc.mainWindow.getWidth() / 1920.0F * 2.5F));
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);
				GlStateManager.matrixMode(5889);
				GlStateManager.pushMatrix();
				GlStateManager.scalef(1.0F, 1.0F, 0.999F);
				
				//Get Shape
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
				VoxelShape shape = blockState.getShape(this.world, blockPos).withOffset((double) blockPos.getX() - d0, (double) blockPos.getY() - d1, (double) blockPos.getZ() - d2);
				AxisAlignedBB bb = shape.getBoundingBox();
				if (breakAnimation == DOWN)
					bb = bb.expand(0f, -breakProgress, 0f).offset(0f, -breakProgress, 0f);
				if (breakAnimation == SHRINK)
					bb = bb.expand(-breakProgress / 2, -breakProgress / 2, -breakProgress / 2);
				
				//Draw Fillin
				drawBlinkingBlock(bb, (breakAnimation == ALPHA) ? breakProgress : getBlinkAlpha());
				
				//Colour
				if (rainbow) {
					final double millis = System.currentTimeMillis() % 10000L / 10000.0f;
					final Color color = Color.getHSBColor((float) millis, 0.8f, 0.8f);
					GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, getAlpha());
				} else
					GL11.glColor4f(getRed(), getGreen(), getBlue(), getAlpha());
				
				//Draw Outline
				drawOutlinedBoundingBox(bb, -1);
				
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5888);
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
			}
		}
	}
	
}
