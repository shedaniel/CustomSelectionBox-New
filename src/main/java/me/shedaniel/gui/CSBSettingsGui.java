package me.shedaniel.gui;

import me.shedaniel.CSBConfig;
import me.shedaniel.utils.ConfigCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static me.shedaniel.CSB.openSettingsGUI;
import static me.shedaniel.CSBConfig.*;

public class CSBSettingsGui extends Screen {
    
    private Screen parent;
    private ConfigCache configCache;
    
    public CSBSettingsGui(Screen p) {
        this.parent = p;
    }
    
    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == 256 && this.doesEscapeKeyClose()) {
            MinecraftClient.getInstance().openScreen(parent);
            return true;
        }
        return super.keyPressed(int_1, int_2, int_3);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void onInitialized() {
        this.configCache = new ConfigCache(enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
        this.buttons.clear();
        // left
        addButton(new CSBSlider(1, 4, this.height / 2 - 62, getRed()));
        addButton(new CSBSlider(2, 4, this.height / 2 - 38, getGreen()));
        addButton(new CSBSlider(3, 4, this.height / 2 - 14, getBlue()));
        addButton(new CSBSlider(4, 4, this.height / 2 + 10, getAlpha()));
        addButton(new CSBSlider(5, 4, this.height / 2 + 34, getThickness() / 7.0F));
        
        // right
        addButton(new ButtonWidget(10, this.width - 154, this.height / 2 - 38, 150, 20, "Break Animation: " + breakAnimation.getText()) {
            @Override
            public void onPressed(double double_1, double double_2) {
                setBreakAnimation(breakAnimation.equals(BreakAnimationType.values()[BreakAnimationType.values().length - 1]) ? BreakAnimationType.NONE : BreakAnimationType.values()[Arrays.asList(BreakAnimationType.values()).indexOf(breakAnimation) + 1]);
                method_2060("Break Animation: " + breakAnimation.getText());
            }
        });
        addButton(new CSBSlider(7, this.width - 154, this.height / 2 - 14, getBlinkAlpha()));
        addButton(new CSBSlider(8, this.width - 154, this.height / 2 + 10, getBlinkSpeed()));
        addButton(new ButtonWidget(6, this.width - 154, this.height / 2 - 62, 150, 20, "Chroma: " + (usingRainbow() ? "ON" : "OFF")) {
            @Override
            public void onPressed(double double_1, double double_2) {
                setIsRainbow(!usingRainbow());
                method_2060("Chroma: " + (usingRainbow() ? "ON" : "OFF"));
            }
        });
        addButton(new ButtonWidget(6, this.width - 154, this.height / 2 + 34, 150, 20, "Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF")) {
            @Override
            public void onPressed(double double_1, double double_2) {
                setAdjustBoundingBoxByLinkedBlocks(!isAdjustBoundingBoxByLinkedBlocks());
                method_2060("Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF"));
            }
        });
        
        //below
        addButton(new ButtonWidget(20, this.width / 2 - 100, this.height - 48, 95, 20, "Enabled: " + (isEnabled() ? "True" : "False")) {
            @Override
            public void onPressed(double double_1, double double_2) {
                setEnabled(!isEnabled());
                method_2060("Enabled: " + (isEnabled() ? "True" : "False"));
            }
        });
        addButton(new ButtonWidget(20, this.width / 2 + 5, this.height - 48, 95, 20, "Save") {
            @Override
            public void onPressed(double double_1, double double_2) {
                try {
                    saveConfig();
                    configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                MinecraftClient.getInstance().openScreen(null);
            }
        });
        addButton(new ButtonWidget(21, this.width / 2 - 100, this.height - 24, 95, 20, "CSB defaults") {
            @Override
            public void onPressed(double double_1, double double_2) {
                try {
                    reset(false);
                    saveConfig();
                    configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                openSettingsGUI();
            }
        });
        addButton(new ButtonWidget(22, this.width / 2 + 5, this.height - 24, 95, 20, "MC defaults") {
            @Override
            public void onPressed(double double_1, double double_2) {
                try {
                    reset(true);
                    saveConfig();
                    configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                openSettingsGUI();
            }
        });
    }
    
    @Override
    public void draw(int par1, int par2, float par3) {
        if (this.client.world == null)
            this.drawTextureBackground(0);
        drawGradientRect(0, 0, this.width, 48 - 4, -1072689136, -804253680); // top
        drawGradientRect(0, this.height / 2 - 67, 158, this.height / 2 + 59, -1072689136, -804253680); // left
        drawGradientRect(this.width - 158, this.height / 2 - 67, this.width, this.height / 2 + 59, -1072689136, -804253680); // right
        drawGradientRect(0, this.height - 48 - 4, this.width, this.height, -1072689136, -804253680); // bottom
        
        drawStringCentered(this.fontRenderer, "Custom Selection Box", this.width / 2, (this.height - (this.height + 4 - 48)) / 2 - 4, 16777215);
        
        super.draw(par1, par2, par3);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClosed() {
        configCache.save();
        this.configCache = new ConfigCache(enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
    }
    
}