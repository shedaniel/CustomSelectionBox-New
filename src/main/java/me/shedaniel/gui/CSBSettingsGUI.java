package me.shedaniel.gui;

import me.shedaniel.utils.ConfigCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static me.shedaniel.CSB.openSettingsGUI;
import static me.shedaniel.CSBConfig.*;

public class CSBSettingsGUI extends GuiScreen {
	GuiScreen parent;
	private ConfigCache configCache;
	
	public CSBSettingsGUI(GuiScreen p) {
		this.parent = p;
	}
	
	@SuppressWarnings("unchecked")
	public void initGui() {
		this.configCache = new ConfigCache(red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, diffButtonLoc, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
		this.buttons.clear();
		// left
		addButton(new CSBSlider(1, 4, this.height / 2 - 62, getRed()));
		addButton(new CSBSlider(2, 4, this.height / 2 - 38, getGreen()));
		addButton(new CSBSlider(3, 4, this.height / 2 - 14, getBlue()));
		addButton(new CSBSlider(4, 4, this.height / 2 + 10, getAlpha()));
		addButton(new CSBSlider(5, 4, this.height / 2 + 34, getThickness() / 7.0F));
		
		// right
		addButton(new GuiButton(10, this.width - 154, this.height / 2 - 38, 150, 20, "Break Animation: " + breakAnimation.getText()) {
			@Override
			public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
				setBreakAnimation(breakAnimation.equals(BreakAnimationType.values()[BreakAnimationType.values().length - 1]) ? BreakAnimationType.NONE :
						BreakAnimationType.values()[Arrays.asList(BreakAnimationType.values()).indexOf(breakAnimation) + 1]);
				displayString = "Break Animation: " + breakAnimation.getText();
			}
		});
		addButton(new CSBSlider(7, this.width - 154, this.height / 2 - 14, getBlinkAlpha()));
		addButton(new CSBSlider(8, this.width - 154, this.height / 2 + 10, getBlinkSpeed()));
		addButton(new GuiButton(6, this.width - 154, this.height / 2 - 62, 150, 20, "Chroma: " + (usingRainbow() ? "ON" : "OFF")) {
			@Override
			public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
				setIsRainbow(!usingRainbow());
				displayString = "Chroma: " + (usingRainbow() ? "ON" : "OFF");
			}
		});
		addButton(new GuiButton(6, this.width - 154, this.height / 2 + 34, 150, 20, "Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF")) {
			@Override
			public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
				setAdjustBoundingBoxByLinkedBlocks(!isAdjustBoundingBoxByLinkedBlocks());
				displayString = "Link Blocks: " + (isAdjustBoundingBoxByLinkedBlocks() ? "ON" : "OFF");
			}
		});
		
		//below
		addButton(new GuiButton(20, this.width / 2 - 100, this.height - 48, "Save") {
			@Override
			public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
				try {
					saveConfig();
					configCache = new ConfigCache(red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, diffButtonLoc, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Minecraft.getInstance().displayGuiScreen((GuiScreen) null);
			}
		});
		addButton(new GuiButton(21, this.width / 2 - 100, this.height - 24, 95, 20, "CSB defaults") {
			@Override
			public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
				try {
					reset(false);
					saveConfig();
					configCache = new ConfigCache(red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, diffButtonLoc, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				openSettingsGUI();
			}
		});
		addButton(new GuiButton(22, this.width / 2 + 5, this.height - 24, 95, 20, "MC defaults") {
			@Override
			public void onClick(double p_mouseClicked_1_, double p_mouseClicked_3_) {
				try {
					reset(true);
					saveConfig();
					configCache = new ConfigCache(red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, diffButtonLoc, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				openSettingsGUI();
			}
		});
	}
	
	public void render(int par1, int par2, float par3) {
		drawGradientRect(0, 0, this.width, 48 - 4, -1072689136, -804253680); // top
		drawGradientRect(0, this.height / 2 - 67, 158, this.height / 2 + 59, -1072689136, -804253680); // left
		drawGradientRect(this.width - 158, this.height / 2 - 67, this.width, this.height / 2 + 59, -1072689136, -804253680); // right
		drawGradientRect(0, this.height - 48 - 4, this.width, this.height, -1072689136, -804253680); // bottom
		
		drawCenteredString(this.fontRenderer, "Custom Selection Box", this.width / 2, (this.height - (this.height + 4 - 48)) / 2 - 4, 16777215);
		
		super.render(par1, par2, par3);
	}
	
	@Override
	public void onGuiClosed() {
		configCache.save();
		this.configCache = new ConfigCache(red, green, blue, alpha, thickness, blinkAlpha, blinkSpeed, diffButtonLoc, disableDepthBuffer, breakAnimation, rainbow, adjustBoundingBoxByLinkedBlocks);
	}
	
}