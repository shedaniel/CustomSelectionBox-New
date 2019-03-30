package me.shedaniel.csb.utils;

import me.shedaniel.csb.CSBConfig;

public class ConfigCache {
    
    private boolean enabled;
    private float red;
    private float green;
    private float blue;
    private float alpha;
    private float thickness;
    private float blinkAlpha;
    private float blinkSpeed;
    private boolean disableDepthBuffer;
    private CSBConfig.BreakAnimationType breakAnimation;
    private boolean rainbow;
    private boolean adjustBoundingBoxByLinkedBlocks;
    
    public ConfigCache(boolean enabled, float red, float green, float blue, float alpha, float thickness, float blinkAlpha, float blinkSpeed, boolean disableDepthBuffer, CSBConfig.BreakAnimationType breakAnimation, boolean rainbow, boolean adjustBoundingBoxByLinkedBlocks) {
        this.enabled = enabled;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.thickness = thickness;
        this.blinkAlpha = blinkAlpha;
        this.blinkSpeed = blinkSpeed;
        this.disableDepthBuffer = disableDepthBuffer;
        this.breakAnimation = breakAnimation;
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
        CSBConfig.breakAnimation = breakAnimation;
        CSBConfig.rainbow = rainbow;
        CSBConfig.adjustBoundingBoxByLinkedBlocks = adjustBoundingBoxByLinkedBlocks;
    }
    
}
