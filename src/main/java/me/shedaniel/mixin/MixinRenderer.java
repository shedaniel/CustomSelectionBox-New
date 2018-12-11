package me.shedaniel.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.PartiallyBrokenBlockEntry;
import net.minecraft.client.render.Renderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShapeContainer;
import net.minecraft.world.WorldListener;
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

@Mixin(Renderer.class)
public abstract class MixinRenderer implements WorldListener, AutoCloseable, ResourceReloadListener {
	
	@Shadow
	private Map<Integer, PartiallyBrokenBlockEntry> partiallyBrokenBlocks;
	@Shadow
	private MinecraftClient client;
	@Shadow
	private ClientWorld world;
	
	@Inject(method = "method_3294", at = @At("HEAD"), cancellable = true)
	public void drawSelectionBox(PlayerEntity player, HitResult trace, int execute, float partialTicks, CallbackInfo ci) {
		ci.cancel();
		if (execute == 0 && trace.type == HitResult.Type.BLOCK) {
			BlockPos blockPos = trace.getBlockPos();
			BlockState blockState = this.world.getBlockState(blockPos);
			if (!blockState.isAir() && this.world.getWorldBorder().contains(blockPos)) {
				float breakProgress = getBreakProgress(partiallyBrokenBlocks, player, trace);
				GlStateManager.enableBlend();
				GlStateManager.blendFuncSeparate(GlStateManager.SrcBlendFactor.SRC_ALPHA, GlStateManager.DstBlendFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcBlendFactor.ONE, GlStateManager.DstBlendFactor.ZERO);
				GlStateManager.lineWidth(Math.max(2.5F, (float) this.client.window.getWindowWidth() / 1920.0F * 2.5F));
				GlStateManager.disableTexture();
				GlStateManager.depthMask(false);
				GlStateManager.matrixMode(5889);
				GlStateManager.pushMatrix();
				GlStateManager.scalef(1.0F, 1.0F, 0.999F);
				
				//Get Shape
				double d0 = MathHelper.lerp((double) partialTicks, player.prevRenderX, player.x);
				double d1 = MathHelper.lerp((double) partialTicks, player.prevRenderY, player.y);
				double d2 = MathHelper.lerp((double) partialTicks, player.prevRenderZ, player.z);
//				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
//				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
//				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
				VoxelShapeContainer shape = blockState.getBoundingShape(this.world, blockPos);
				BoundingBox bb = shape.getBoundingBox().offset((double) blockPos.getX() - d0, (double) blockPos.getY() - d1, (double) blockPos.getZ() - d2);
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
				GlStateManager.enableTexture();
				GlStateManager.disableBlend();
			}
		}
	}
	
}
