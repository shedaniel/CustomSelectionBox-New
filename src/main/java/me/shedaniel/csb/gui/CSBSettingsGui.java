package me.shedaniel.csb.gui;

import me.shedaniel.csb.CSBConfig;
import me.shedaniel.csb.utils.ConfigCache;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.StringTextComponent;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static me.shedaniel.csb.CSB.openSettingsGUI;
import static me.shedaniel.csb.CSBConfig.*;

public class CSBSettingsGui extends Screen {
    
    private Screen parent;
    private ConfigCache configCache;
    
    public CSBSettingsGui(Screen parent) {
        super(new StringTextComponent("Custom Selection Box"));
        this.parent = parent;
    }
    
    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == 256 && this.shouldCloseOnEsc()) {
            onClose();
            minecraft.openScreen(parent);
            return true;
        }
        return super.keyPressed(int_1, int_2, int_3);
    }
    
    @Override
    protected void init() {
        this.configCache = new ConfigCache(enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
        this.buttons.clear();
        // left
        addButton(new CSBSlider(1, 4, this.height / 2 - 62, getRed()));
        addButton(new CSBSlider(2, 4, this.height / 2 - 38, getGreen()));
        addButton(new CSBSlider(3, 4, this.height / 2 - 14, getBlue()));
        addButton(new CSBSlider(4, 4, this.height / 2 + 10, getAlpha()));
        addButton(new CSBSlider(5, 4, this.height / 2 + 34, getThickness() / 7.0F));
        
        // right
        addButton(new ButtonWidget(this.width - 154, this.height / 2 - 38, 150, 20, "Break Animation: " + breakAnimation.getText(), widget -> {
            setBreakAnimation(breakAnimation.equals(BreakAnimationType.values()[BreakAnimationType.values().length - 1]) ? BreakAnimationType.NONE : BreakAnimationType.values()[Arrays.asList(BreakAnimationType.values()).indexOf(breakAnimation) + 1]);
            widget.setMessage("Break Animation: " + breakAnimation.getText());
        }));
        addButton(new CSBSlider(7, this.width - 154, this.height / 2 - 14, getBlinkAlpha()));
        addButton(new CSBSlider(8, this.width - 154, this.height / 2 + 10, getBlinkSpeed()));
        addButton(new ButtonWidget(this.width - 154, this.height / 2 - 62, 150, 20, "Chroma: " + (usingRainbow() ? "ON" : "OFF"), widget -> {
            setIsRainbow(!usingRainbow());
            widget.setMessage("Chroma: " + (usingRainbow() ? "ON" : "OFF"));
        }));
        addButton(new ButtonWidget(this.width - 154, this.height / 2 + 34, 150, 20, "Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF"), widget -> {
            setAdjustBoundingBoxByLinkedBlocks(!isAdjustBoundingBoxByLinkedBlocks());
            widget.setMessage("Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF"));
        }));
        
        //below
        addButton(new ButtonWidget(this.width / 2 - 100, this.height - 48, 95, 20, "Enabled: " + (isEnabled() ? "True" : "False"), widget -> {
            setEnabled(!isEnabled());
            widget.setMessage("Enabled: " + (isEnabled() ? "True" : "False"));
        }));
        addButton(new ButtonWidget(this.width / 2 + 5, this.height - 48, 95, 20, "Save", widget -> {
            try {
                saveConfig();
                configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            minecraft.openScreen(parent);
        }));
        addButton(new ButtonWidget(this.width / 2 - 100, this.height - 24, 95, 20, "CSB defaults", widget -> {
            try {
                reset(false);
                saveConfig();
                configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            openSettingsGUI(minecraft, parent);
        }));
        addButton(new ButtonWidget(this.width / 2 + 5, this.height - 24, 95, 20, "MC defaults", widget -> {
            try {
                reset(true);
                saveConfig();
                configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            openSettingsGUI(minecraft, parent);
        }));
    }
    
    @Override
    public void render(int par1, int par2, float par3) {
        if (this.minecraft.world == null)
            this.renderDirtBackground(0);
        fillGradient(0, 0, this.width, 48 - 4, -1072689136, -804253680); // top
        fillGradient(0, this.height / 2 - 67, 158, this.height / 2 + 59, -1072689136, -804253680); // left
        fillGradient(this.width - 158, this.height / 2 - 67, this.width, this.height / 2 + 59, -1072689136, -804253680); // right
        fillGradient(0, this.height - 48 - 4, this.width, this.height, -1072689136, -804253680); // bottom
        
        drawCenteredString(this.font, getTitle().getFormattedText(), this.width / 2, (this.height - (this.height + 4 - 48)) / 2 - 4, 16777215);
        
        super.render(par1, par2, par3);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClose() {
        configCache.save();
        this.configCache = new ConfigCache(enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
    }
    
}