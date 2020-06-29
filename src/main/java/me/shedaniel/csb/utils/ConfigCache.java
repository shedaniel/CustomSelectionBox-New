package me.shedaniel.csb.utils;

import me.shedaniel.csb.CSBConfig;

public class ConfigCache {
    
    private final boolean enabled;
    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;
    private final float thickness;
    private final float blinkAlpha;
    private final float blinkSpeed;
    private final boolean disableDepthBuffer;
    private final boolean rainbow;
    private final boolean adjustBoundingBoxByLinkedBlocks;
    
    public ConfigCache(boolean enabled, float red, float green, float blue, float alpha, float thickness, float blinkAlpha, float blinkSpeed, boolean disableDepthBuffer, boolean rainbow, boolean adjustBoundingBoxByLinkedBlocks) {
        this.enabled = enabled;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.thickness = thickness;
        this.blinkAlpha = blinkAlpha;
        this.blinkSpeed = blinkSpeed;
        this.disableDepthBuffer = disableDepthBuffer;
        this.rainbow = rainbow;
        this.adjustBoundingBoxByLinkedBlocks = adjustBoundingBoxByLinkedBlocks;
    }
    
    public void save() {
        CSBConfig.enabled = enabled;
        CSBConfig.red = red;
        CSBConfig.green = green;
        CSBConfig.blue = blue;
        CSBConfig.alpha = alpha;
        CSBConfig.thickness = thickness;
        CSBConfig.blinkAlpha = blinkAlpha;
        CSBConfig.blinkSpeed = blinkSpeed;
        CSBConfig.disableDepthBuffer = disableDepthBuffer;
        CSBConfig.rainbow = rainbow;
        CSBConfig.adjustBoundingBoxByLinkedBlocks = adjustBoundingBoxByLinkedBlocks;
    }
    
}
