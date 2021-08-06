package me.shedaniel.csb.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.csb.CSB;
import me.shedaniel.csb.CSBConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.PistonType;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.ActionResult;
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

import java.util.Objects;

@SuppressWarnings("unused")
@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Unique private boolean csbRender = false;
    @Unique private VoxelShape csbShape;
    @Unique private float csbRed;
    @Unique private float csbGreen;
    @Unique private float csbBlue;
    @Unique private float csbAlpha;
    @Shadow private ClientWorld world;

    @Shadow
    private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
    }

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Redirect(method = "render", at = @At(value = "INVOKE",
                                          target = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
                                          ordinal = 0))
    private void onDrawShapeOutline(WorldRenderer worldRenderer, MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos blockPos, BlockState blockState) {
        var shape = blockState.getOutlineShape(world, blockPos, ShapeContext.of(entity));
        var offsetX = blockPos.getX() - cameraX;
        var offsetY = blockPos.getY() - cameraY;
        var offsetZ = blockPos.getZ() - cameraZ;

        if (CSBConfig.isEnabled()) {
            if (CSBConfig.rainbow) {
                final double millis = System.currentTimeMillis() % 10000L / 10000.0f;
                final int color = CSB.HSBtoRGB((float) millis, 0.8f, 0.8f);
                csbRed = (color >> 16 & 255) / 255.0f;
                csbGreen = (color >> 8 & 255) / 255.0f;
                csbBlue = (color & 255) / 255.0f;
            } else {
                csbRed = CSBConfig.getRed();
                csbGreen = CSBConfig.getGreen();
                csbBlue = CSBConfig.getBlue();
            }
            csbAlpha = CSBConfig.getAlpha();
            csbShape = shape;
            if (CSBConfig.adjustBoundingBoxByLinkedBlocks) {
                csbShape = CSB.adjustShapeByLinkedBlocks(world, blockState, blockPos, shape);
            }
            csbShape = csbShape.offset(offsetX, offsetY, offsetZ);
        
            MatrixStack.Entry entry = matrixStack.peek();
            csbShape.forEachEdge((k, l, m, n, o, p)  -> {
                float q1 = (float)(n - k);
                float r1 = (float)(o - l);
                float s1 = (float)(p - m);
                float t = (float)Math.sqrt(q1 * q1 + r1 * r1 + s1 * s1);
                q1 /= t;
                r1 /= t;
                s1 /= t;
                vertexConsumer.vertex(entry.getModel(), (float)k, (float)l, (float)m).color(csbRed, csbGreen, csbBlue, csbAlpha).normal(entry.getNormal(), q1, r1, s1).next();
                vertexConsumer.vertex(entry.getModel(), (float)n, (float)o, (float)p).color(csbRed, csbGreen, csbBlue, csbAlpha).normal(entry.getNormal(), q1, r1, s1).next();
            });
            csbRender = true;
        } else {
            drawShapeOutline(matrixStack, vertexConsumer, shape, offsetX, offsetY, offsetZ, 0.0F, 0.0F, 0.0F, 0.4F);
        }
    }

    @Inject(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V",
                     shift = At.Shift.AFTER))
    private void renderWorldBorder(MatrixStack matrices, float delta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci) {
        if (csbRender) {
            var blinkingSpeed = CSBConfig.getBlinkSpeed();
            var blinkingAlpha = CSBConfig.getBlinkAlpha();
            if (blinkingSpeed > 0) {
                blinkingAlpha *= (float) Math.abs(Math.sin(System.currentTimeMillis() / 100.0D * blinkingSpeed));
            }
            final var blinkingAlpha2 = blinkingAlpha;

            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            csbShape.forEachEdge((k, l, m, n, o, p)  -> {
                bufferBuilder.vertex((float)k, (float)l, (float)m).color(csbRed, csbGreen, csbBlue, csbAlpha).next();
                bufferBuilder.vertex((float)n, (float)o, (float)p).color(csbRed, csbGreen, csbBlue, csbAlpha).next();
            });
            tessellator.draw();

            csbShape = csbShape.getBoundingBoxes().stream()
                .map(box -> box.expand(0.002D, 0.002D, 0.002D))
                .map(VoxelShapes::cuboid)
                .reduce(VoxelShapes::union)
                .orElse(VoxelShapes.empty()).simplify();
            csbShape.forEachBox((k, l, m, n, o, p) -> {
                CSB.drawBox(tessellator, bufferBuilder, k, l, m, n, o, p, csbRed, csbGreen, csbBlue, blinkingAlpha2);
            });

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);

            csbRender = false;
        }
    }
}
