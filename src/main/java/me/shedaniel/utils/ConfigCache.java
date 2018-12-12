package me.shedaniel.utils;

import me.shedaniel.CSBConfig;

public class ConfigCache {
	
	public boolean enabled;
	public float red;
	public float green;
	public float blue;
	public float alpha;
	public float thickness;
	public float blinkAlpha;
	public float blinkSpeed;
	public boolean diffButtonLoc;
	public boolean disableDepthBuffer;
	public CSBConfig.BreakAnimationType breakAnimation;
	public boolean rainbow;
	public boolean adjustBoundingBoxByLinkedBlocks;
	
	public ConfigCache(boolean enabled, float red, float green, float blue, float alpha, float thickness, float blinkAlpha, float blinkSpeed,
	                   boolean diffButtonLoc, boolean disableDepthBuffer, CSBConfig.BreakAnimationType breakAnimation, boolean rainbow, boolean adjustBoundingBoxByLinkedBlocks) {
		this.enabled = enabled;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		this.thickness = thickness;
		this.blinkAlpha = blinkAlpha;
		this.blinkSpeed = blinkSpeed;
		this.diffButtonLoc = diffButtonLoc;
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
		CSBConfig.diffButtonLoc = diffButtonLoc;
		CSBConfig.disableDepthBuffer = disableDepthBuffer;
		CSBConfig.breakAnimation = breakAnimation;
		CSBConfig.rainbow = rainbow;
		CSBConfig.adjustBoundingBoxByLinkedBlocks = adjustBoundingBoxByLinkedBlocks;
	}
	
}
