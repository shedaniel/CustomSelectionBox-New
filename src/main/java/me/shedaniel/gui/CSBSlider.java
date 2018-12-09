package me.shedaniel.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import static me.shedaniel.CSBConfig.*;

public class CSBSlider extends GuiButton {
	private float sliderValue;
	public boolean dragging;
	
	// GuiOptionSlider
	public CSBSlider(int i, int x, int y, float f) {
		super(i, x, y, 150, 20, "");
		this.sliderValue = f;
		this.displayString = getDisplayString(i);
	}
	
	@Override
	public int getHoverState(boolean flag) {
		return 0;
	}
	
	@Override
	protected void renderBg(Minecraft minecraft, int p_mouseDragged_2_, int p_mouseDragged_3_) {
		if (this.visible) {
			if (this.dragging) {
				this.sliderValue = ((float) (p_mouseDragged_2_ - (this.x + 4)) / (float) (this.width - 8));
				if (this.sliderValue < 0.0F) {
					this.sliderValue = 0.0F;
				}
				
				if (this.sliderValue > 1.0F) {
					this.sliderValue = 1.0F;
				}
				
				updateValue(this.id);
				this.displayString = getDisplayString(this.id);
			}
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(x
							+ (int) (sliderValue * (width - 8)),
					this.y, 0, 66, 4, 20);
			drawTexturedModalRect(x
							+ (int) (sliderValue * (width - 8)) + 4,
					this.y, 196, 66, 4, 20);
		}
	}
	
	@Override
	public void onClick(double p_mousePressed_1_, double p_mousePressed_3_) {
		sliderValue = ((float) (p_mousePressed_1_ - (this.x + 4)) / (float) (width - 8));
		
		if (sliderValue < 0.0F) {
			sliderValue = 0.0F;
		}
		
		if (sliderValue > 1.0F) {
			sliderValue = 1.0F;
		}
		
		this.displayString = getDisplayString(this.id);
		this.dragging = true;
	}
	
	private void updateValue(int id) {
		switch (id) {
			case 1:
				setRed(sliderValue);
				break;
			case 2:
				setGreen(sliderValue);
				break;
			case 3:
				setBlue(sliderValue);
				break;
			case 4:
				setAlpha(sliderValue);
				break;
			case 5:
				setThickness(sliderValue * 7.0F);
				break;
			case 7:
				setBlinkAlpha(sliderValue);
				break;
			case 8:
				setBlinkSpeed(sliderValue);
		}
	}
	
	private String getDisplayString(int id) {
		switch (id) {
			case 1:
				return "Red: " + Math.round(sliderValue * 255.0F);
			case 2:
				return "Green: " + Math.round(sliderValue * 255.0F);
			case 3:
				return "Blue: " + Math.round(sliderValue * 255.0F);
			case 4:
				return "Alpha: " + Math.round(sliderValue * 255.0F);
			case 5:
				return "Thickness: " + Math.round(sliderValue * 7.0F);
			case 7:
				return "Blink Alpha: " + Math.round(sliderValue * 255.0F);
			case 8:
				return "Blink Speed: " + Math.round(sliderValue * 100.0F);
		}
		return "Option Error?! (" + id + ")";
	}
	
	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_3_2) {
		this.dragging = false;
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_3_2);
	}
}