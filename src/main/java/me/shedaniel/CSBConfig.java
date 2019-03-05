package me.shedaniel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class CSBConfig implements ModInitializer {

	@Override
	public void onInitialize() {
		try {
			loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean enabled;
	public static float red;
	public static float green;
	public static float blue;
	public static float alpha;
	public static float thickness;
	public static float blinkAlpha;
	public static float blinkSpeed;
	public static boolean disableDepthBuffer;
	public static boolean adjustBoundingBoxByLinkedBlocks;
	public static BreakAnimationType breakAnimation;
	public static boolean rainbow;

	private static File configFile = new File(FabricLoader.getInstance().getGameDirectory(), "config" + File.separator + "CSB" + File.separator + "config.json");

	private static void loadConfig() throws IOException {
		configFile.getParentFile().mkdirs();
		String content = configFile.exists() ? FileUtils.readFileToString(configFile, "utf-8") : "{}";
		JsonElement jsonElement = new JsonParser().parse(content);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		enabled = !jsonObject.has("enabled") || jsonObject.get("enabled").getAsBoolean();
		red = jsonObject.has("colourRed") ? jsonObject.get("colourRed").getAsInt() / 255.0F : 0F;
		green = jsonObject.has("colourGreen") ? jsonObject.get("colourGreen").getAsInt() / 255.0F : 0F;
		blue = jsonObject.has("colourBlue") ? jsonObject.get("colourBlue").getAsInt() / 255.0F : 0F;
		alpha = jsonObject.has("alpha") ? jsonObject.get("alpha").getAsInt() / 255.0F : 1F;
		thickness = jsonObject.has("thickness") ? jsonObject.get("thickness").getAsInt() : 4F;
		blinkSpeed = jsonObject.has("blinkSpeed") ? jsonObject.get("blinkSpeed").getAsInt() / 100.0F : 0.2F;
		blinkAlpha = jsonObject.has("blinkAlpha") ? jsonObject.get("blinkAlpha").getAsInt() / 255.0F : 0.390625F;
		disableDepthBuffer = jsonObject.has("disableDepthBuffer") && jsonObject.get("disableDepthBuffer").getAsBoolean();
		breakAnimation = jsonObject.has("breakAnimation") ? BreakAnimationType.getById(jsonObject.get("breakAnimation").getAsInt()) : BreakAnimationType.NONE;
		rainbow = jsonObject.has("rainbow") && jsonObject.get("rainbow").getAsBoolean();
		adjustBoundingBoxByLinkedBlocks = jsonObject.has("adjustBoundingBoxByLinkedBlocks") && jsonObject.get("adjustBoundingBoxByLinkedBlocks").getAsBoolean();

		saveConfig();
	}

	public static void saveConfig() throws FileNotFoundException {
		JsonObject object = new JsonObject();
		object.addProperty("enabled", enabled);
		object.addProperty("colourRed", (int) (red * 255));
		object.addProperty("colourGreen", (int) (green * 255));
		object.addProperty("colourBlue", (int) (blue * 255));
		object.addProperty("alpha", (int) (alpha * 255));
		object.addProperty("thickness", (int) thickness);
		object.addProperty("blinkSpeed", (int) (blinkSpeed * 100));
		object.addProperty("blinkAlpha", (int) (blinkAlpha * 255));
		object.addProperty("disableDepthBuffer", disableDepthBuffer);
		object.addProperty("breakAnimation", breakAnimation.getId());
		object.addProperty("rainbow", rainbow);
		object.addProperty("adjustBoundingBoxByLinkedBlocks", adjustBoundingBoxByLinkedBlocks);
		if (configFile.exists())
			configFile.delete();
		PrintWriter writer = new PrintWriter(configFile);
		writer.print(objectToString(object));
		writer.close();
	}

	private static String objectToString(JsonObject object) {
		try {
			StringWriter stringWriter = new StringWriter();
			JsonWriter jsonWriter = new JsonWriter(stringWriter);
			jsonWriter.setLenient(true);
			jsonWriter.setIndent("\t");
			Streams.write(object, jsonWriter);
			return stringWriter.toString();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}

	public static void reset(boolean mc) throws FileNotFoundException {
		setEnabled(true);
		setRed(0.0F);
		setGreen(0.0F);
		setBlue(0.0F);
		setAlpha(mc ? 0.4F : 1.0F);
		setThickness(mc ? 2.0F : 4.0F);
		setBlinkAlpha(mc ? 0.0F : 0.390625F);
		setBlinkSpeed(0.2F);
		disableDepthBuffer = false;
		setBreakAnimation(BreakAnimationType.NONE);
		setIsRainbow(false);
		setAdjustBoundingBoxByLinkedBlocks(false);
		saveConfig();
	}

	public static boolean isAdjustBoundingBoxByLinkedBlocks() {
		return adjustBoundingBoxByLinkedBlocks;
	}

	public static void setAdjustBoundingBoxByLinkedBlocks(boolean adjustBoundingBoxByLinkedBlocks) {
		CSBConfig.adjustBoundingBoxByLinkedBlocks = adjustBoundingBoxByLinkedBlocks;
	}

	public static boolean isEnabled() {
		return enabled;
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

	public static void setEnabled(boolean enabled) {
		CSBConfig.enabled = enabled;
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

	public static void setBreakAnimation(BreakAnimationType type) {
		breakAnimation = type;
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

	public enum BreakAnimationType {
		NONE(0, "None"), SHRINK(1, "Shrink"), DOWN(2, "Down"), UP(4, "Up"), ALPHA(3, "Alpha");

		private int id;
		private String text;

		BreakAnimationType(int id, String text) {
			this.id = id;
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public int getId() {
			return id;
		}

		public static int getLargestAnimationIndex() {
			return 4;
		}

		public static BreakAnimationType getById(int id) {
			for (BreakAnimationType type : values())
				if (type.getId() == id)
					return type;
			return null;
		}
	}

}
