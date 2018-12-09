package me.shedaniel;

import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.FileUtils;
import org.dimdev.riftloader.listener.InitializationListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class CSBConfig implements InitializationListener {
	
	@Override
	public void onInitialization() {
		try {
			loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static float red;
	public static float green;
	public static float blue;
	public static float alpha;
	public static float thickness;
	public static float blinkAlpha;
	public static float blinkSpeed;
	public static boolean diffButtonLoc;
	public static boolean disableDepthBuffer;
	public static int breakAnimation;
	public static boolean rainbow;
	
	public static int NONE = 0;
	public static int SHRINK = 1;
	public static int DOWN = 2;
	public static int ALPHA = 3;
	public static int lastAnimationIndex = 3;
	
	public static File configFile = new File(Launch.minecraftHome, "config" + File.separator + "CSB" + File.separator + "config.json");
	
	public static void loadConfig() throws IOException, JSONException {
		configFile.getParentFile().mkdirs();
		String content = configFile.exists() ? FileUtils.readFileToString(configFile, "utf-8") : "{}";
		JSONObject jsonObject = new JSONObject(content);
		red = jsonObject.has("colourRed") ? jsonObject.getInt("colourRed") / 255.0F : 0F;
		green = jsonObject.has("colourGreen") ? jsonObject.getInt("colourGreen") / 255.0F : 0F;
		blue = jsonObject.has("colourBlue") ? jsonObject.getInt("colourBlue") / 255.0F : 0F;
		alpha = jsonObject.has("alpha") ? jsonObject.getInt("alpha") / 255.0F : 1F;
		thickness = jsonObject.has("thickness") ? jsonObject.getInt("thickness") : 4F;
		blinkSpeed = jsonObject.has("blinkSpeed") ? jsonObject.getInt("blinkSpeed") / 100.0F : 0.3F;
		blinkAlpha = jsonObject.has("blinkAlpha") ? jsonObject.getInt("blinkAlpha") / 255.0F : 100.0F / 255.0F;
		diffButtonLoc = jsonObject.has("diffButtonLoc") ? jsonObject.getBoolean("diffButtonLoc") : false;
		disableDepthBuffer = jsonObject.has("disableDepthBuffer") ? jsonObject.getBoolean("disableDepthBuffer") : false;
		breakAnimation = jsonObject.has("breakAnimation") ? jsonObject.getInt("breakAnimation") : 0;
		rainbow = jsonObject.has("rainbow") ? jsonObject.getBoolean("rainbow") : false;
		
		saveConfig();
	}
	
	public static void saveConfig() throws FileNotFoundException, JSONException {
		JSONObject object = new JSONObject();
		object.put("colourRed", (int) (red * 255));
		object.put("colourGreen", (int) (green * 255));
		object.put("colourBlue", (int) (blue * 255));
		object.put("alpha", (int) (alpha * 255));
		object.put("thickness", (int) thickness);
		object.put("blinkSpeed", (int) (blinkSpeed * 100));
		object.put("blinkAlpha", (int) (blinkAlpha * 255));
		object.put("diffButtonLoc", diffButtonLoc);
		object.put("disableDepthBuffer", disableDepthBuffer);
		object.put("breakAnimation", breakAnimation);
		object.put("rainbow", rainbow);
		if (configFile.exists())
			configFile.delete();
		PrintWriter writer = new PrintWriter(configFile);
		writer.print(object.toString(4));
		writer.close();
	}
	
	public static void reset(boolean mc) throws FileNotFoundException, JSONException {
		setRed(0.0F);
		setGreen(0.0F);
		setBlue(0.0F);
		setAlpha(mc ? 0.4F : 1.0F);
		setThickness(mc ? 2.0F : 4.0F);
		setBlinkAlpha(mc ? 0.0F : 0.390625F);
		setBlinkSpeed(0.2F);
		disableDepthBuffer = false;
		setBreakAnimation(0);
		setIsRainbow(false);
		saveConfig();
	}
	
	public static float getRed() {
		return between(red, 0.0F, 1.0F);
	}
	
	public static float getGreen() {
		return between(green, 0.0F, 1.0F);
	}
	
	public static float getBlue() {
		return between(blue, 0.0F, 1.0F);
	}
	
	public static float getAlpha() {
		return between(alpha, 0.0F, 1.0F);
	}
	
	public static float getThickness() {
		return between(thickness, 0.1F, 7.0F);
	}
	
	public static float getBlinkAlpha() {
		return between(blinkAlpha, 0.0F, 1.0F);
	}
	
	public static float getBlinkSpeed() {
		return between(blinkSpeed, 0.0F, 1.0F);
	}
	
	public static void setRed(float r) {
		red = between(r, 0.0F, 1.0F);
	}
	
	public static void setGreen(float g) {
		green = between(g, 0.0F, 1.0F);
	}
	
	public static void setBlue(float b) {
		blue = between(b, 0.0F, 1.0F);
	}
	
	public static void setAlpha(float a) {
		alpha = between(a, 0.0F, 1.0F);
	}
	
	public static void setThickness(float t) {
		thickness = between(t, 0.1F, 7.0F);
	}
	
	public static void setBlinkAlpha(float ba) {
		blinkAlpha = between(ba, 0.0F, 1.0F);
	}
	
	public static void setBlinkSpeed(float s) {
		blinkSpeed = between(s, 0.0F, 1.0F);
	}
	
	public static void setBreakAnimation(int index) {
		breakAnimation = between(index, 0, lastAnimationIndex);
	}
	
	public static void setIsRainbow(boolean b) {
		rainbow = b;
	}
	
	public static boolean usingRainbow() {
		return rainbow;
	}
	
	public static int getRedInt() {
		return Math.round(getRed() * 256.0F);
	}
	
	public static int getGreenInt() {
		return Math.round(getGreen() * 256.0F);
	}
	
	public static int getBlueInt() {
		return Math.round(getBlue() * 256.0F);
	}
	
	public static int getAlphaInt() {
		return Math.round(getAlpha() * 256.0F);
	}
	
	public static int getThicknessInt() {
		return Math.round(getThickness());
	}
	
	public static int getBlinkAlphaInt() {
		return Math.round(getBlinkAlpha() * 256.0F);
	}
	
	public static int getBlinkSpeedInt() {
		return Math.round(getBlinkSpeed() * 100.0F);
	}
	
	private static float between(float i, float x, float y) {
		if (i < x)
			i = x;
		if (i > y)
			i = y;
		return i;
	}
	
	private static int between(int i, int x, int y) {
		if (i < x)
			i = x;
		if (i > y)
			i = y;
		return i;
	}
	
}
