package me.shedaniel.csb.gui;

import me.shedaniel.csb.CSBConfig;
import me.shedaniel.csb.utils.ConfigCache;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.io.FileNotFoundException;

import static me.shedaniel.csb.CSB.openSettingsGUI;
import static me.shedaniel.csb.CSBConfig.*;

public class CSBSettingsScreen extends Screen {
    
    private final Screen parent;
    private ConfigCache configCache;
    
    public CSBSettingsScreen(Screen parent) {
        super(new LiteralText("Custom Selection Box"));
        this.parent = parent;
    }
    
    @Override
    public boolean keyPressed(int int_1, int int_2, int int_3) {
        if (int_1 == 256 && this.shouldCloseOnEsc()) {
            onClose();
            client.setScreen(parent);
            return true;
        }
        return super.keyPressed(int_1, int_2, int_3);
    }
    
    @Override
    protected void init() {
        this.configCache = new ConfigCache(enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, rainbow, adjustBoundingBoxByLinkedBlocks);
        this.clearChildren();
        // left
        addDrawableChild(new CSBSliderWidget(1, 4, this.height / 2 - 62, getRed()));
        addDrawableChild(new CSBSliderWidget(2, 4, this.height / 2 - 38, getGreen()));
        addDrawableChild(new CSBSliderWidget(3, 4, this.height / 2 - 14, getBlue()));
        addDrawableChild(new CSBSliderWidget(4, 4, this.height / 2 + 10, getAlpha()));
        addDrawableChild(new CSBSliderWidget(5, 4, this.height / 2 + 34, getThickness() / 7.0F));
        
        // right
        addDrawableChild(new CSBSliderWidget(7, this.width - 154, this.height / 2 - 14, getBlinkAlpha()));
        addDrawableChild(new CSBSliderWidget(8, this.width - 154, this.height / 2 + 10, getBlinkSpeed()));
        addDrawableChild(new ButtonWidget(this.width - 154, this.height / 2 - 38, 150, 20, new LiteralText("Chroma: " + (usingRainbow() ? "ON" : "OFF")), widget -> {
            setIsRainbow(!usingRainbow());
            widget.setMessage(new LiteralText("Chroma: " + (usingRainbow() ? "ON" : "OFF")));
        }));
        addDrawableChild(new ButtonWidget(this.width - 154, this.height / 2 + 34, 150, 20, new LiteralText("Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF")), widget -> {
            setAdjustBoundingBoxByLinkedBlocks(!isAdjustBoundingBoxByLinkedBlocks());
            widget.setMessage(new LiteralText("Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF")));
        }));
        
        //below
        addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 48, 95, 20, new LiteralText("Enabled: " + (isEnabled() ? "True" : "False")), widget -> {
            setEnabled(!isEnabled());
            widget.setMessage(new LiteralText("Enabled: " + (isEnabled() ? "True" : "False")));
        }));
        addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 48, 95, 20, new LiteralText("Save"), widget -> {
            try {
                saveConfig();
                configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, rainbow, adjustBoundingBoxByLinkedBlocks);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            client.setScreen(parent);
        }));
        addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 24, 95, 20, new LiteralText("CSB defaults"), widget -> {
            try {
                reset(false);
                saveConfig();
                configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, rainbow, adjustBoundingBoxByLinkedBlocks);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            openSettingsGUI(client, parent);
        }));
        addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 24, 95, 20, new LiteralText("MC defaults"), widget -> {
            try {
                reset(true);
                saveConfig();
                configCache = new ConfigCache(CSBConfig.enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, rainbow, adjustBoundingBoxByLinkedBlocks);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            openSettingsGUI(client, parent);
        }));
    }
    
    @Override
    public void render(MatrixStack matrices, int par1, int par2, float par3) {
        if (this.client.world == null)
            this.renderBackgroundTexture(0);
        fillGradient(matrices, 0, 0, this.width, 48 - 4, -1072689136, -804253680); // top
        fillGradient(matrices, 0, this.height / 2 - 67, 158, this.height / 2 + 59, -1072689136, -804253680); // left
        fillGradient(matrices, this.width - 158, this.height / 2 - 43, this.width, this.height / 2 + 59, -1072689136, -804253680); // right
        fillGradient(matrices, 0, this.height - 48 - 4, this.width, this.height, -1072689136, -804253680); // bottom
        
        drawCenteredText(matrices, this.textRenderer, getTitle(), this.width / 2, (this.height - (this.height + 4 - 48)) / 2 - 4, 16777215);
        
        super.render(matrices, par1, par2, par3);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void onClose() {
        configCache.save();
        this.configCache = new ConfigCache(enabled, red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, disableDepthBuffer, rainbow, adjustBoundingBoxByLinkedBlocks);
    }
    
}