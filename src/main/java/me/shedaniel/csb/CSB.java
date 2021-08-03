package me.shedaniel.csb;

import me.shedaniel.csb.api.CSBRenderer;
import me.shedaniel.csb.gui.CSBSettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CSB implements ClientModInitializer {
    
    public static final List<CSBRenderer> RENDERERS = new ArrayList<>();
    
    public static void openSettingsGUI(MinecraftClient client, Screen parent) {
        client.setScreen(new CSBSettingsScreen(parent));
    }
    
    @Override
    public void onInitializeClient() {
        RENDERERS.clear();
        RENDERERS.addAll(FabricLoader.getInstance().getEntrypoints("csb_renderers", CSBRenderer.class));
        RENDERERS.sort(Comparator.comparingDouble(CSBRenderer::getPriority));
        if (RENDERERS.isEmpty()) {
            throw new IllegalStateException("No default CSB renderers!");
        }
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
}
