package me.shedaniel.utils;

import me.shedaniel.CSBConfig;

public class ConfigCache {
	
	public float red;
	public float green;
	public float blue;
	public float alpha;
	public float thickness;
	public float blinkAlpha;
	public float blinkSpeed;
	public boolean diffButtonLoc;
	public boolean disableDepthBuffer;
	public int breakAnimation;
	public boolean rainbow;
	
	public int NONE = 0;
	public int SHRINK = 1;
	public int DOWN = 2;
	public int ALPHA = 3;
	public int lastAnimationIndex = 3;
	
	public ConfigCache(float red, float green, float blue, float alpha, float thickness, float blinkAlpha, float blinkSpeed, boolean diffButtonLoc, boolean disableDepthBuffer, int breakAnimation, boolean rainbow) {
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
	}
	
	public void save() {
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
	}
	
}
