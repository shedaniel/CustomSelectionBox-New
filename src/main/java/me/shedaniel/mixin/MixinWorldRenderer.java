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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorldEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Map;

import static me.shedaniel.CSB.*;
import static me.shedaniel.CSBConfig.*;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer implements IWorldEventListener, AutoCloseable, IResourceManagerReloadListener {
    
    private BlockPos csb_pos;
    private float csb_breakProcess;
    
    @Shadow
    private Map<Integer, DestroyBlockProgress> damagedBlocks;
    @Shadow
    private Minecraft mc;
    @Shadow
    private WorldClient world;
    
    @Redirect(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;drawShape(Lnet/minecraft/util/math/shapes/VoxelShape;DDDFFFF)V", ordinal = 0))
    private void onDrawShapeOutline(VoxelShape shape, double x, double y, double z, float red, float green, float blue, float alpha) {
        if (!isEnabled()) {
            WorldRenderer.drawShape(shape, x, y, z, red, green, blue, alpha);
            return;
        }
        GlStateManager.lineWidth(getThickness());
        red = getRed();
        green = getGreen();
        blue = getBlue();
        alpha = getAlpha();
        if (rainbow) {
            final double millis = System.currentTimeMillis() % 10000L / 10000.0f;
            final Color color = Color.getHSBColor((float) millis, 0.8f, 0.8f);
            red = color.getRed() / 255.0f;
            green = color.getGreen() / 255.0f;
            blue = color.getBlue() / 255.0f;
        }
        if (adjustBoundingBoxByLinkedBlocks)
            shape = csb_adjustShapeByLinkedBlocks(world.getBlockState(csb_pos), csb_pos, shape);
        final float blinkingAlpha = (getBlinkSpeed() > 0 && !breakAnimation.equals(BreakAnimationType.ALPHA)) ? getBlinkAlpha() * (float) Math.abs(Math.sin(System.currentTimeMillis() / 100.0D * getBlinkSpeed())) : csb_breakProcess;
        drawNewBlinkingBlock(shape, x, y, z, red, green, blue, blinkingAlpha);
        drawNewOutlinedBoundingBox(shape, x, y, z, red, green, blue, alpha);
    }
    
    private VoxelShape csb_adjustShapeByLinkedBlocks(IBlockState blockState, BlockPos blockPos, VoxelShape shape) {
        try {
            if (blockState.getBlock() instanceof BlockChest) {
                // Chests
                Block block = blockState.getBlock();
                EnumFacing facing = BlockChest.getDirectionToAttached(blockState);
                IBlockState anotherChestState = this.world.getBlockState(blockPos.offset(facing, 1));
                if (anotherChestState.getBlock() == block)
                    if (blockPos.offset(facing, 1).offset(BlockChest.getDirectionToAttached(anotherChestState)).equals(blockPos))
                        return VoxelShapes.or(shape, anotherChestState.getShape(world, blockPos).withOffset(facing.getXOffset(), facing.getYOffset(), facing.getZOffset()));
            } else if (blockState.getBlock() instanceof BlockDoor) {
                // Doors
                Block block = blockState.getBlock();
                if (blockState.get(BlockDoor.HALF).equals(DoubleBlockHalf.LOWER) && world.getBlockState(blockPos.up(1)).getBlock() == block) {
                    IBlockState otherState = world.getBlockState(blockPos.up(1));
                    if (otherState.get(BlockDoor.POWERED).equals(blockState.get(BlockDoor.POWERED)) && otherState.get(BlockDoor.FACING).equals(blockState.get(BlockDoor.FACING)) && otherState.get(BlockDoor.HINGE).equals(blockState.get(BlockDoor.HINGE))) {
                        return VoxelShapes.or(shape, otherState.getShape(world, blockPos).withOffset(0, 1, 0));
                    }
                }
                if (blockState.get(BlockDoor.HALF).equals(DoubleBlockHalf.UPPER) && world.getBlockState(blockPos.down(1)).getBlock() == block) {
                    IBlockState otherState = world.getBlockState(blockPos.down(1));
                    if (otherState.get(BlockDoor.POWERED).equals(blockState.get(BlockDoor.POWERED)) && otherState.get(BlockDoor.FACING).equals(blockState.get(BlockDoor.FACING)) && otherState.get(BlockDoor.HINGE).equals(blockState.get(BlockDoor.HINGE)))
                        return VoxelShapes.or(shape, otherState.getShape(world, blockPos).withOffset(0, -1, 0));
                }
            } else if (blockState.getBlock() instanceof BlockBed) {
                // Beds
                Block block = blockState.getBlock();
                EnumFacing direction = blockState.get(BlockHorizontal.HORIZONTAL_FACING);
                IBlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (blockState.get(BlockBed.PART).equals(BedPart.FOOT) && otherState.getBlock() == block) {
                    if (otherState.get(BlockBed.PART).equals(BedPart.HEAD))
                        return VoxelShapes.or(shape, otherState.getShape(world, blockPos).withOffset(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));
                }
                otherState = world.getBlockState(blockPos.offset(direction.getOpposite()));
                if (blockState.get(BlockBed.PART).equals(BedPart.HEAD) && otherState.getBlock() == block) {
                    if (otherState.get(BlockBed.PART).equals(BedPart.FOOT))
                        return VoxelShapes.or(shape, otherState.getShape(world, blockPos).withOffset(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));
                }
            } else if (blockState.getBlock() instanceof BlockPistonBase && blockState.get(BlockPistonBase.EXTENDED)) {
                // Piston Base
                Block block = blockState.getBlock();
                EnumFacing direction = blockState.get(BlockDirectional.FACING);
                IBlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (otherState.get(BlockPistonExtension.TYPE).equals(block == Blocks.PISTON ? PistonType.DEFAULT : PistonType.STICKY) && direction.equals(otherState.get(BlockDirectional.FACING)))
                    return VoxelShapes.or(shape, otherState.getShape(world, blockPos).withOffset(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));
            } else if (blockState.getBlock() instanceof BlockPistonExtension) {
                // Piston Arm
                Block block = blockState.getBlock();
                EnumFacing direction = blockState.get(BlockDirectional.FACING).getOpposite();
                IBlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (direction.getOpposite().equals(otherState.get(BlockDirectional.FACING)) && otherState.get(BlockPistonBase.EXTENDED) && (otherState.getBlock() == (blockState.get(BlockPistonExtension.TYPE).equals(PistonType.DEFAULT) ? Blocks.PISTON : Blocks.STICKY_PISTON)))
                    return VoxelShapes.or(shape, otherState.getShape(world, blockPos).withOffset(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));
            }
        } catch (Exception e) {
        
        }
        return shape;
    }
    
    @Inject(method = "drawSelectionBox", at = @At("HEAD"))
    private void drawHighlightedBlockOutline(EntityPlayer entity, RayTraceResult trace, int execute, float delta, CallbackInfo ci) {
        if (execute == 0 && trace.type == RayTraceResult.Type.BLOCK) {
            this.csb_pos = trace.getBlockPos();
            this.csb_breakProcess = getBreakProgress(damagedBlocks, entity, trace);
        }
    }
    
}
