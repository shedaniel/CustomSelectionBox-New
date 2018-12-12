package me.shedaniel.mixin;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.PistonType;
import net.minecraft.util.EnumFacing;
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
public abstract class MixinWorldRenderer implements IWorldEventListener, AutoCloseable, IResourceManagerReloadListener {
	
	@Shadow
	private Map<Integer, DestroyBlockProgress> damagedBlocks;
	@Shadow
	private Minecraft mc;
	@Shadow
	private WorldClient world;
	
	@Inject(method = "drawSelectionBox", at = @At(value = "HEAD"), cancellable = true, expect = -1)
	public void drawSelectionBox(EntityPlayer player, RayTraceResult trace, int execute, float partialTicks, CallbackInfo ci) {
		if (!enabled)
			return;
		ci.cancel();
		if (execute == 0 && trace.type == RayTraceResult.Type.BLOCK) {
			BlockPos blockPos = trace.getBlockPos();
			IBlockState blockState = this.world.getBlockState(blockPos);
			if (!blockState.isAir() && this.world.getWorldBorder().contains(blockPos)) {
				float breakProgress = getBreakProgress(damagedBlocks, player, trace);
				
				if (disableDepthBuffer) {
					GL11.glDisable(GL11.GL_DEPTH_TEST);
				}
				
				GlStateManager.enableBlend();
				GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.lineWidth(getThickness());
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
				if (isAdjustBoundingBoxByLinkedBlocks()) {
					bb = adjustBoundingBoxByLinkedBlocks(blockState, blockPos, bb);
				}
				if (breakAnimation.equals(BreakAnimationType.DOWN))
					bb = bb.contract(0f, breakProgress * (bb.maxY - bb.minY), 0f);
				if (breakAnimation.equals(BreakAnimationType.UP))
					bb = bb.contract(0f, breakProgress * (bb.maxY - bb.minY), 0f).offset(0f, breakProgress * (bb.maxY - bb.minY), 0f);
				if (breakAnimation.equals(BreakAnimationType.SHRINK))
					bb = bb.contract(breakProgress, breakProgress, breakProgress)
							.offset(breakProgress / 2, breakProgress / 2, breakProgress / 2);
				bb = bb.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D);
				
				//Draw Fillin
				drawBlinkingBlock(bb, (breakAnimation.equals(BreakAnimationType.ALPHA)) ? breakProgress : getBlinkAlpha());
				
				//Colour
				if (rainbow) {
					final double millis = System.currentTimeMillis() % 10000L / 10000.0f;
					final Color color = Color.getHSBColor((float) millis, 0.8f, 0.8f);
					GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, getAlpha());
				} else
					GL11.glColor4f(getRed(), getGreen(), getBlue(), getAlpha());
				
				//Draw Outline
				if (getThickness() > 0)
					drawOutlinedBoundingBox(bb, -1);
				
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5888);
				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();

				if (disableDepthBuffer) {
					GL11.glEnable(GL11.GL_DEPTH_TEST);
				}
			}
		}
	}
	
	private AxisAlignedBB adjustBoundingBoxByLinkedBlocks(IBlockState blockState, BlockPos blockPos, AxisAlignedBB bb) {
		try {
			// Chests
			if (blockState.getBlock() instanceof BlockChest) {
				Block block = blockState.getBlock();
				EnumFacing facing = BlockChest.getDirectionToAttached(blockState);
				IBlockState anotherChestState = this.world.getBlockState(blockPos.offset(facing, 1));
				if (anotherChestState.getBlock() == block) {
					if (blockPos.offset(facing, 1).offset(BlockChest.getDirectionToAttached(anotherChestState)).equals(blockPos)) {
						double xx = facing.getXOffset(), zz = facing.getZOffset();
						if (xx > 0) xx -= 0.0625;
						else if (xx < 0) xx += 0.0625;
						if (zz > 0) zz -= 0.0625;
						else if (zz < 0) zz += 0.0625;
						bb = bb.expand(xx, 0f, zz);
					}
				}
			}
			// Doors
			if (blockState.getBlock() instanceof BlockDoor) {
				Block block = blockState.getBlock();
				if (blockState.get(BlockDoor.HALF).equals(DoubleBlockHalf.LOWER) && world.getBlockState(blockPos.up(1)).getBlock() == block) {
					IBlockState otherState = world.getBlockState(blockPos.up(1));
					if (otherState.get(BlockDoor.POWERED).equals(blockState.get(BlockDoor.POWERED)) &&
							otherState.get(BlockDoor.FACING).equals(blockState.get(BlockDoor.FACING)) &&
							otherState.get(BlockDoor.HINGE).equals(blockState.get(BlockDoor.HINGE))) {
						bb = bb.expand(0, 1, 0);
						return bb;
					}
				}
				if (blockState.get(BlockDoor.HALF).equals(DoubleBlockHalf.UPPER) && world.getBlockState(blockPos.down(1)).getBlock() == block) {
					IBlockState otherState = world.getBlockState(blockPos.down(1));
					if (otherState.get(BlockDoor.POWERED).equals(blockState.get(BlockDoor.POWERED)) &&
							otherState.get(BlockDoor.FACING).equals(blockState.get(BlockDoor.FACING)) &&
							otherState.get(BlockDoor.HINGE).equals(blockState.get(BlockDoor.HINGE)))
						bb = bb.expand(0, -1, 0);
				}
			}
			// Beds
			if (blockState.getBlock() instanceof BlockBed) {
				Block block = blockState.getBlock();
				EnumFacing direction = blockState.get(BlockHorizontal.HORIZONTAL_FACING);
				IBlockState otherState = world.getBlockState(blockPos.offset(direction));
				if (blockState.get(BlockBed.PART).equals(BedPart.FOOT) && otherState.getBlock() == block) {
					if (otherState.get(BlockBed.PART).equals(BedPart.HEAD)) {
						bb = bb.expand(direction.getXOffset(), 0, direction.getZOffset());
						return bb;
					}
				}
				otherState = world.getBlockState(blockPos.offset(direction.getOpposite()));
				if (blockState.get(BlockBed.PART).equals(BedPart.HEAD) && otherState.getBlock() == block) {
					if (otherState.get(BlockBed.PART).equals(BedPart.FOOT)) {
						bb = bb.expand(direction.getOpposite().getXOffset(), 0, direction.getOpposite().getZOffset());
						return bb;
					}
				}
			}
			//Pistons
			if (blockState.getBlock() instanceof BlockPistonBase && blockState.get(BlockPistonBase.EXTENDED)) {
				Block block = blockState.getBlock();
				EnumFacing direction = blockState.get(BlockDirectional.FACING);
				IBlockState otherState = world.getBlockState(blockPos.offset(direction));
				if (otherState.get(BlockPistonExtension.TYPE).equals(block == Blocks.PISTON ? PistonType.DEFAULT : PistonType.STICKY) && direction.equals(otherState.get(BlockDirectional.FACING))) {
					double xx = direction.getXOffset(), yy = direction.getYOffset(), zz = direction.getZOffset();
					if (xx > 0) xx += 0.25;
					else if (xx < 0) xx -= 0.25;
					if (yy > 0) yy += 0.25;
					else if (yy < 0) yy -= 0.25;
					if (zz > 0) zz += 0.25;
					else if (zz < 0) zz -= 0.25;
					bb = bb.expand(xx, yy, zz);
					return bb;
				}
			}
			if (blockState.getBlock() instanceof BlockPistonExtension) {
				Block block = blockState.getBlock();
				EnumFacing direction = blockState.get(BlockDirectional.FACING).getOpposite();
				IBlockState otherState = world.getBlockState(blockPos.offset(direction));
				if (direction.getOpposite().equals(otherState.get(BlockDirectional.FACING)) && otherState.get(BlockPistonBase.EXTENDED) && (otherState.getBlock() == (blockState.get(BlockPistonExtension.TYPE).equals(PistonType.DEFAULT) ? Blocks.PISTON : Blocks.STICKY_PISTON))) {
					double xx = direction.getXOffset(), yy = direction.getYOffset(), zz = direction.getZOffset();
					if (xx > 0) xx -= 0.25;
					else if (xx < 0) xx += 0.25;
					if (yy > 0) yy -= 0.25;
					else if (yy < 0) yy += 0.25;
					if (zz > 0) zz -= 0.25;
					else if (zz < 0) zz += 0.25;
					bb = bb.expand(xx, yy, zz);
					return bb;
				}
			}
			return bb;
		} catch (Exception e) {
			return bb;
		}
	}
	
}
