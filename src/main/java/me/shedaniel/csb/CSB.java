package me.shedaniel.csb;

import me.shedaniel.csb.gui.CSBSettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.PistonType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class CSB implements ClientModInitializer {
    
    public static void openSettingsGUI(MinecraftClient client, Screen parent) {
        client.setScreen(new CSBSettingsScreen(parent));
    }
    
    @Override
    public void onInitializeClient() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("csbconfig").executes(context -> {
            openSettingsGUI(context.getSource().getClient(), null);
            return 1;
        }));
    }
    
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0F) {
            r = g = b = (int) (brightness * 255.0F + 0.5F);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0F;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - saturation * (1.0F - f));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (t * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 1:
                    r = (int) (q * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 2:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (t * 255.0F + 0.5F);
                    break;
                case 3:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (q * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 4:
                    r = (int) (t * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 5:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (q * 255.0F + 0.5F);
            }
        }
        
        return -16777216 | r << 16 | g << 8 | b;
    }

    public static void drawBox(Tessellator tessellator, BufferBuilder bufferBuilder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        // Up
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex((float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        tessellator.draw();
        
        // Down
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex((float)x1, (float)y2, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y2, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y2, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y2, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y2, (float)z1).color(red, green, blue, alpha).next();
        tessellator.draw();
        
        // North
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex((float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y2, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y2, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        tessellator.draw();
        
        // South
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex((float)x1, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y2, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y2, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        tessellator.draw();
        
        // West
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex((float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y2, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y2, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        tessellator.draw();
        
        // East
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex((float)x2, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y2, (float)z1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y2, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y1, (float)z2).color(red, green, blue, alpha).next();
        bufferBuilder.vertex((float)x2, (float)y1, (float)z1).color(red, green, blue, alpha).next();
        tessellator.draw();
    }

    public static VoxelShape adjustShapeByLinkedBlocks(ClientWorld world, BlockState blockState, BlockPos blockPos, VoxelShape shape) {
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
                Direction direction = blockState.get(FacingBlock.FACING);
                BlockState otherState = world.getBlockState(blockPos.offset(direction.getOpposite()));
                if (otherState.getBlock() instanceof PistonBlock && direction == otherState.get(FacingBlock.FACING) && otherState.get(PistonBlock.EXTENDED))
                    return VoxelShapes.union(shape, otherState.getOutlineShape(world, blockPos.offset(direction.getOpposite())).offset(direction.getOpposite().getOffsetX(), direction.getOpposite().getOffsetY(), direction.getOpposite().getOffsetZ()));
            }
        } catch (Throwable ignored) {
        }
        return shape;
    }
}
