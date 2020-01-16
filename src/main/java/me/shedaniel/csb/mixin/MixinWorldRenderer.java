package me.shedaniel.csb.mixin;

import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.PistonType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.shedaniel.csb.CSB.*;
import static me.shedaniel.csb.CSBConfig.*;

@SuppressWarnings("unused")
@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    
    @Unique private boolean render = false;
    @Unique private float r = 0f;
    @Unique private float g = 0f;
    @Unique private float b = 0f;
    @Unique private float a = 0f;
    @Unique private float blinkingAlpha = 0f;
    @Unique private VoxelShape lastShape = null;
    @Shadow private ClientWorld world;
    
    @Shadow
    private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
    }
    
    @Shadow @Final private MinecraftClient client;
    
    @Redirect(method = "render", at = @At(value = "INVOKE",
                                          target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
                                          ordinal = 0))
    private void onDrawShapeOutline(WorldRenderer worldRenderer, MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState) {
        if (!isEnabled()) {
            drawShapeOutline(matrixStack, vertexConsumer, blockState.getOutlineShape(world, blockPos, EntityContext.of(entity)), blockPos.getX() - d, blockPos.getY() - e, blockPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
            return;
        }
        r = getRed();
        g = getGreen();
        b = getBlue();
        a = getAlpha();
        if (rainbow) {
            final double millis = System.currentTimeMillis() % 10000L / 10000.0f;
            final int color = HSBtoRGB((float) millis, 0.8f, 0.8f);
            r = (color >> 16 & 255) / 255.0f;
            g = (color >> 8 & 255) / 255.0f;
            b = (color & 255) / 255.0f;
        }
        VoxelShape shape = blockState.getOutlineShape(world, blockPos, EntityContext.of(entity));
        if (adjustBoundingBoxByLinkedBlocks)
            shape = csb_adjustShapeByLinkedBlocks(world.getBlockState(blockPos), blockPos, shape);
        blinkingAlpha = getBlinkSpeed() > 0 ? getBlinkAlpha() * (float) Math.abs(Math.sin(System.currentTimeMillis() / 100.0D * getBlinkSpeed())) : getBlinkAlpha();
        lastShape = shape;
        render = true;
    }
    
    @Inject(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V",
                     shift = At.Shift.AFTER))
    private void renderWorldBorder(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        if (render) {
            BlockHitResult hitResult = (BlockHitResult) client.crosshairTarget;
            BlockPos blockPos = hitResult.getBlockPos();
            Vec3d pos = camera.getPos();
            drawNewOutlinedBoundingBox(null, null, lastShape, blockPos.getX() - pos.getX(), blockPos.getY() - pos.getY(), blockPos.getZ() - pos.getZ(), r, g, b, a);
            drawNewBlinkingBlock(lastShape, blockPos.getX() - pos.getX(), blockPos.getY() - pos.getY(), blockPos.getZ() - pos.getZ(), r, g, b, blinkingAlpha);
            render = false;
        }
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
                Direction direction = blockState.get(HorizontalFacingBlock.FACING);
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
        } catch (Exception ignored) {
        }
        return shape;
    }
    
}
