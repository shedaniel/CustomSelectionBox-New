package me.shedaniel.csb.api;

import me.shedaniel.csb.CSBConfig;
import me.shedaniel.csb.gui.CSBInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

public interface CSBRenderer {
    /**
     * The priority of this renderer, smaller priorities are called first, and once it has been rendered,
     * all priorities that has a bigger priority in number will be ignored.
     *
     * @return the priority of this renderer in double.
     */
    default double getPriority() {
        return 1000d;
    }
    
    default MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
    
    @Deprecated
    default CSBInfo getInfo() {
        return (CSBInfo) getClient().worldRenderer;
    }
    
    default float getOutlineThickness() {
        return CSBConfig.getThickness() * Math.max(2.5F, (float)MinecraftClient.getInstance().getWindow().getFramebufferWidth() / 1920.0F * 2.5F);
    }
    
    default int getOutlineColor() {
        return (((int) (getOutlineAlpha() * 255)) & 255) << 24 | (((int) (getOutlineRed() * 255)) & 255) << 16 | (((int) (getOutlineGreen() * 255)) & 255) << 8 | (((int) (getOutlineBlue() * 255)) & 255);
    }
    
    default float getOutlineRed() {
        return getInfo().getOutlineRed();
    }
    
    default float getOutlineGreen() {
        return getInfo().getOutlineGreen();
    }
    
    default float getOutlineBlue() {
        return getInfo().getOutlineBlue();
    }
    
    default float getOutlineAlpha() {
        return getInfo().getOutlineAlpha();
    }
    
    default int getInnerColor() {
        return (((int) (getInnerAlpha() * 255)) & 255) << 24 | (((int) (getInnerRed() * 255)) & 255) << 16 | (((int) (getInnerGreen() * 255)) & 255) << 8 | (((int) (getInnerBlue() * 255)) & 255);
    }
    
    default float getInnerRed() {
        return getInfo().getInnerRed();
    }
    
    default float getInnerGreen() {
        return getInfo().getInnerGreen();
    }
    
    default float getInnerBlue() {
        return getInfo().getInnerBlue();
    }
    
    default float getInnerAlpha() {
        return getInfo().getInnerAlpha();
    }
    
    ActionResult render(ClientWorld world, Camera camera, BlockHitResult hitResult, float delta);
}
