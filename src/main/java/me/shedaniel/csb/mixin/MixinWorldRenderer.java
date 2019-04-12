package me.shedaniel.csb.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.PistonType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.PartiallyBrokenBlockEntry;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Map;

import static me.shedaniel.csb.CSB.*;
import static me.shedaniel.csb.CSBConfig.*;

@SuppressWarnings("unused")
@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    
    private BlockPos csb_pos;
    private float csb_breakProcess;
    
    @Shadow
    private ClientWorld world;
    
    @Shadow
    @Final
    private Map<Integer, PartiallyBrokenBlockEntry> partiallyBrokenBlocks;
    
    @Redirect(method = "drawHighlightedBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawShapeOutline(Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V", ordinal = 0))
    private void onDrawShapeOutline(VoxelShape shape, double x, double y, double z, float red, float green, float blue, float alpha) {
        if (!isEnabled()) {
            WorldRenderer.drawShapeOutline(shape, x, y, z, red, green, blue, alpha);
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
    
    private VoxelShape csb_adjustShapeByLinkedBlocks(BlockState blockState, BlockPos blockPos, VoxelShape shape) {
        try {
            if (blockState.getBlock() instanceof ChestBlock) {
                // Chests
                Block block = blockState.getBlock();
                Direction facing = ChestBlock.getFacing(blockState);
                BlockState anotherChestState = world.getBlockState(blockPos.offset(facing, 1));
                if (anotherChestState.getBlock() == block)
                    if (blockPos.offset(facing, 1).offset(ChestBlock.getFacing(anotherChestState)).equals(blockPos))
                        return VoxelShapes.union(shape, anotherChestState.getOutlineShape(world, blockPos).offset(facing.getOffsetX(), facing.getOffsetY(), facing.getOffsetZ()));
            } else if (blockState.getBlock() instanceof DoorBlock) {
                // Doors
                Block block = blockState.getBlock();
                if (world.getBlockState(blockPos.up(1)).getBlock() == block) {
                    BlockState otherState = world.getBlockState(blockPos.up(1));
                    if (otherState.get(DoorBlock.POWERED).equals(blockState.get(DoorBlock.POWERED)) && otherState.get(DoorBlock.FACING).equals(blockState.get(DoorBlock.FACING)) && otherState.get(DoorBlock.HINGE).equals(blockState.get(DoorBlock.HINGE))) {
                        return VoxelShapes.union(shape, otherState.getOutlineShape(world, blockPos).offset(0, 1, 0));
                    }
                }
                if (world.getBlockState(blockPos.down(1)).getBlock() == block) {
                    BlockState otherState = world.getBlockState(blockPos.down(1));
                    if (otherState.get(DoorBlock.POWERED).equals(blockState.get(DoorBlock.POWERED)) && otherState.get(DoorBlock.FACING).equals(blockState.get(DoorBlock.FACING)) && otherState.get(DoorBlock.HINGE).equals(blockState.get(DoorBlock.HINGE)))
                        return VoxelShapes.union(shape, otherState.getOutlineShape(world, blockPos).offset(0, -1, 0));
                }
            } else if (blockState.getBlock() instanceof BedBlock) {
                // Beds
                Block block = blockState.getBlock();
                Direction direction = blockState.get(HorizontalFacingBlock.field_11177);
                BlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (blockState.get(BedBlock.PART).equals(BedPart.FOOT) && otherState.getBlock() == block) {
                    if (otherState.get(BedBlock.PART).equals(BedPart.HEAD))
                        return VoxelShapes.union(shape, otherState.getOutlineShape(world, blockPos).offset(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()));
                }
                otherState = world.getBlockState(blockPos.offset(direction.getOpposite()));
                direction = direction.getOpposite();
                if (blockState.get(BedBlock.PART).equals(BedPart.HEAD) && otherState.getBlock() == block) {
                    if (otherState.get(BedBlock.PART).equals(BedPart.FOOT))
                        return VoxelShapes.union(shape, otherState.getOutlineShape(world, blockPos).offset(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()));
                }
            } else if (blockState.getBlock() instanceof PistonBlock && blockState.get(PistonBlock.EXTENDED)) {
                // Piston Base
                Block block = blockState.getBlock();
                Direction direction = blockState.get(FacingBlock.FACING);
                BlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (otherState.get(PistonHeadBlock.TYPE).equals(block == Blocks.PISTON ? PistonType.DEFAULT : PistonType.STICKY) && direction.equals(otherState.get(FacingBlock.FACING)))
                    return VoxelShapes.union(shape, otherState.getOutlineShape(world, blockPos).offset(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()));
            } else if (blockState.getBlock() instanceof PistonHeadBlock) {
                // Piston Arm
                Block block = blockState.getBlock();
                Direction direction = blockState.get(FacingBlock.FACING);
                BlockState otherState = world.getBlockState(blockPos.offset(direction.getOpposite()));
                if (otherState.getBlock() instanceof PistonBlock && direction == otherState.get(FacingBlock.FACING) && otherState.get(PistonBlock.EXTENDED))
                    return VoxelShapes.union(shape, otherState.getOutlineShape(world, blockPos.offset(direction.getOpposite())).offset(direction.getOpposite().getOffsetX(), direction.getOpposite().getOffsetY(), direction.getOpposite().getOffsetZ()));
            }
        } catch (Exception ignored) { }
        return shape;
    }
    
    @Inject(method = "drawHighlightedBlockOutline", at = @At("HEAD"))
    private void drawHighlightedBlockOutline(Camera camera, HitResult hitResult_1, int int_1, CallbackInfo ci) {
        if (hitResult_1 instanceof BlockHitResult) {
            this.csb_pos = ((BlockHitResult) hitResult_1).getBlockPos();
            this.csb_breakProcess = getBreakProgress(partiallyBrokenBlocks, hitResult_1);
        }
    }
    
}
