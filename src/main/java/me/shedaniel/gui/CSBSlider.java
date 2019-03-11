package me.shedaniel.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;

import static me.shedaniel.CSBConfig.*;

public class CSBSlider extends ButtonWidget {
    private boolean dragging;
    private int id;
    private float sliderValue;
    
    // GuiOptionSlider
    CSBSlider(int i, int x, int y, float f) {
        super(x, y, 150, 20, "");
        this.id = i;
        this.sliderValue = f;
        this.setText(getDisplayString(i));
    }
    
    @Override
    public boolean isHovered() {
        return false;
    }
    
    @Override
    protected void drawBackground(MinecraftClient minecraft, int p_mouseDragged_2_, int p_mouseDragged_3_) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = ((float) (p_mouseDragged_2_ - (this.x + 4)) / (float) (this.width - 8));
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
            }
            
            updateValue(this.id);
            this.setText(getDisplayString(this.id));
            
            minecraft.getTextureManager().bindTexture(WIDGET_TEX);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedRect(this.x + (int) (this.sliderValue * (double) (this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedRect(this.x + (int) (this.sliderValue * (double) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }
    
    @Override
    protected int getTextureId(boolean boolean_1) {
        return 0;
    }
    
    @Override
    public void onPressed(double p_mousePressed_1_, double p_mousePressed_3_) {
        this.sliderValue = ((float) (p_mousePressed_1_ - (this.x + 4)) / (float) (width - 8));
        this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
        
        this.setText(getDisplayString(this.id));
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