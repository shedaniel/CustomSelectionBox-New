package me.shedaniel.gui;

import me.shedaniel.CSB;
import net.minecraft.client.gui.widget.ButtonWidget;

public class CSBConfigButton extends ButtonWidget {
    public CSBConfigButton(int int_1, int int_2, int int_3, int int_4, int int_5, String string_1) {
        super(int_1, int_2, int_3, int_4, int_5, string_1);
    }
    
    @Override
    public void onPressed(double var1, double var3) {
        CSB.openSettingsGUI();
    }
}
