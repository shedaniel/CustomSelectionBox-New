package me.shedaniel.csb.gui;

import me.shedaniel.csb.CSBConfig;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;

import static me.shedaniel.csb.CSBConfig.*;

public class CSBSliderWidget extends SliderWidget {
    
    private final int id;
    
    CSBSliderWidget(int i, int x, int y, float f) {
        super(x, y, 150, 20, LiteralText.EMPTY, f);
        this.id = i;
        this.setMessage(new LiteralText(getDisplayString(i)));
    }
    
    @Override
    protected void updateMessage() {
        setMessage(new LiteralText(getDisplayString(id)));
    }
    
    @Override
    protected void applyValue() {
        updateValue(id);
    }
    
    private void updateValue(int id) {
        switch (id) {
            case 1:
                setRed(getValue());
                break;
            case 2:
                setGreen(getValue());
                break;
            case 3:
                setBlue(getValue());
                break;
            case 4:
                CSBConfig.setAlpha(getValue());
                break;
            case 5:
                setThickness(getValue() * 7);
                break;
            case 7:
                setBlinkAlpha(getValue());
                break;
            case 8:
                setBlinkSpeed(getValue());
        }
    }
    
    private float getValue() {
        return (float) value;
    }
    
    private String getDisplayString(int id) {
        switch (id) {
            case 1:
                return "Red: " + Math.round(getValue() * 255.0F);
            case 2:
                return "Green: " + Math.round(getValue() * 255.0F);
            case 3:
                return "Blue: " + Math.round(getValue() * 255.0F);
            case 4:
                return "Outline Alpha: " + Math.round(getValue() * 255.0F);
            case 5:
                return "Outline Thickness: " + Math.round(getValue() * 7.0F);
            case 7:
                return "Blink Alpha: " + Math.round(getValue() * 255.0F);
            case 8:
                return "Blink Speed: " + Math.round(getValue() * 100.0F);
        }
        return "Option Error?! (" + id + ")";
    }
    
}