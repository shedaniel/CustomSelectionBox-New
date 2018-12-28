package me.shedaniel.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.PistonType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.PartiallyBrokenBlockEntry;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.WorldListener;
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
public abstract class MixinWorldRenderer implements WorldListener, AutoCloseable, ResourceReloadListener {
    
    @Shadow
    private Map<Integer, PartiallyBrokenBlockEntry> partiallyBrokenBlocks;
    @Shadow
    private MinecraftClient client;
    @Shadow
    private ClientWorld world;
    
    @Inject(method = "drawHighlightedBlockOutline", at = @At("HEAD"), cancellable = true)
    public void drawHighlightedBlockOutline(PlayerEntity player, HitResult trace, int execute, float partialTicks, CallbackInfo ci) {
        ci.cancel();
        if (execute == 0 && trace.type == HitResult.Type.BLOCK) {
            BlockPos blockPos = trace.getBlockPos();
            BlockState blockState = this.world.getBlockState(blockPos);
            if (!blockState.isAir() && this.world.getWorldBorder().contains(blockPos)) {
                float breakProgress = getBreakProgress(partiallyBrokenBlocks, player, trace);
                
                GlStateManager.enableBlend();
                GlStateManager.blendFuncSeparate(GlStateManager.SrcBlendFactor.SRC_ALPHA, GlStateManager.DstBlendFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcBlendFactor.ONE, GlStateManager.DstBlendFactor.ZERO);
                GlStateManager.lineWidth(getThickness());
                GlStateManager.disableTexture();
                GlStateManager.depthMask(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.pushMatrix();
                GlStateManager.scalef(1.0F, 1.0F, 0.999F);
                double d0 = MathHelper.lerp(partialTicks, player.prevRenderX, player.x);
                double d1 = MathHelper.lerp(partialTicks, player.prevRenderY, player.y);
                double d2 = MathHelper.lerp(partialTicks, player.prevRenderZ, player.z);
                
                //Colour
                float red = getRed();
                float green = getGreen();
                float blue = getBlue();
                float alpha = getAlpha();
                if (rainbow) {
                    final double millis = System.currentTimeMillis() % 10000L / 10000.0f;
                    final Color color = Color.getHSBColor((float) millis, 0.8f, 0.8f);
                    red = color.getRed() / 255.0f;
                    green = color.getGreen() / 255.0f;
                    blue = color.getBlue() / 255.0f;
                }
                
                //Get Shape
                VoxelShape shape = blockState.getBoundingShape(this.world, blockPos);
                if (adjustBoundingBoxByLinkedBlocks)
                    shape = adjustShapeByLinkedBlocks(blockState, blockPos, shape);
                
                //Draw Fillin
                final float blinkingAlpha = (getBlinkSpeed() > 0 && !breakAnimation.equals(BreakAnimationType.ALPHA)) ?
                        alpha * (float) Math.abs(Math.sin(System.currentTimeMillis() / 100.0D * getBlinkSpeed())) : breakProgress;
                drawNewBlinkingBlock(shape, blockPos.getX() - d0, blockPos.getY() - d1, blockPos.getZ() - d2, red, green, blue, blinkingAlpha);
                
                //Draw Outside Shape
                drawNewOutlinedBoundingBox(shape, blockPos.getX() - d0, blockPos.getY() - d1, blockPos.getZ() - d2, red, green, blue, alpha);
                
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture();
                GlStateManager.disableBlend();
            }
        }
    }
    
    private VoxelShape adjustShapeByLinkedBlocks(BlockState blockState, BlockPos blockPos, VoxelShape shape) {
        try {
            if (blockState.getBlock() instanceof ChestBlock) {
                // Chests
                Block block = blockState.getBlock();
                Direction facing = ChestBlock.method_9758(blockState);
                BlockState anotherChestState = this.world.getBlockState(blockPos.offset(facing, 1));
                if (anotherChestState.getBlock() == block)
                    if (blockPos.offset(facing, 1).offset(ChestBlock.method_9758(anotherChestState)).equals(blockPos))
                        return VoxelShapes.union(shape, anotherChestState.getBoundingShape(world, blockPos).method_1096(facing.getOffsetX(), facing.getOffsetY(), facing.getOffsetZ()));
            } else if (blockState.getBlock() instanceof DoorBlock) {
                // Doors
                Block block = blockState.getBlock();
                if (blockState.get(DoorBlock.field_10946).equals(BlockHalf.LOWER) && world.getBlockState(blockPos.up(1)).getBlock() == block) {
                    BlockState otherState = world.getBlockState(blockPos.up(1));
                    if (otherState.get(DoorBlock.field_10940).equals(blockState.get(DoorBlock.field_10940)) &&
                            otherState.get(DoorBlock.field_10938).equals(blockState.get(DoorBlock.field_10938)) &&
                            otherState.get(DoorBlock.field_10941).equals(blockState.get(DoorBlock.field_10941))) {
                        return VoxelShapes.union(shape, otherState.getBoundingShape(world, blockPos).method_1096(0, 1, 0));
                    }
                }
                if (blockState.get(DoorBlock.field_10946).equals(BlockHalf.UPPER) && world.getBlockState(blockPos.down(1)).getBlock() == block) {
                    BlockState otherState = world.getBlockState(blockPos.down(1));
                    if (otherState.get(DoorBlock.field_10940).equals(blockState.get(DoorBlock.field_10940)) &&
                            otherState.get(DoorBlock.field_10938).equals(blockState.get(DoorBlock.field_10938)) &&
                            otherState.get(DoorBlock.field_10941).equals(blockState.get(DoorBlock.field_10941)))
                        return VoxelShapes.union(shape, otherState.getBoundingShape(world, blockPos).method_1096(0, -1, 0));
                }
            } else if (blockState.getBlock() instanceof BedBlock) {
                // Beds
                Block block = blockState.getBlock();
                Direction direction = blockState.get(HorizontalFacingBlock.field_11177);
                BlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (blockState.get(BedBlock.field_9967).equals(BedPart.FOOT) && otherState.getBlock() == block) {
                    if (otherState.get(BedBlock.field_9967).equals(BedPart.HEAD))
                        return VoxelShapes.union(shape, otherState.getBoundingShape(world, blockPos).method_1096(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()));
                }
                otherState = world.getBlockState(blockPos.offset(direction.getOpposite()));
                if (blockState.get(BedBlock.field_9967).equals(BedPart.HEAD) && otherState.getBlock() == block) {
                    if (otherState.get(BedBlock.field_9967).equals(BedPart.FOOT))
                        return VoxelShapes.union(shape, otherState.getBoundingShape(world, blockPos).method_1096(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()));
                }
            } else if (blockState.getBlock() instanceof PistonBlock && blockState.get(PistonBlock.field_12191)) {
                // Piston Base
                Block block = blockState.getBlock();
                Direction direction = blockState.get(FacingBlock.field_10927);
                BlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (otherState.get(PistonExtensionBlock.TYPE).equals(block == Blocks.PISTON ? PistonType.NORMAL : PistonType.STICKY) && direction.equals(otherState.get(FacingBlock.field_10927)))
                    return VoxelShapes.union(shape, otherState.getBoundingShape(world, blockPos).method_1096(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()));
            } else if (blockState.getBlock() instanceof PistonExtensionBlock) {
                // Piston Arm
                Block block = blockState.getBlock();
                Direction direction = blockState.get(PistonExtensionBlock.FACING).getOpposite();
                BlockState otherState = world.getBlockState(blockPos.offset(direction));
                if (direction.getOpposite().equals(otherState.get(FacingBlock.field_10927)) && otherState.get(PistonBlock.field_12191) && (otherState.getBlock() == (blockState.get(PistonExtensionBlock.TYPE).equals(PistonType.NORMAL) ? Blocks.PISTON : Blocks.STICKY_PISTON)))
                    return VoxelShapes.union(shape, otherState.getBoundingShape(world, blockPos).method_1096(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()));
            }
        } catch (Exception e) {
        
        }
        return shape;
    }
    
}
