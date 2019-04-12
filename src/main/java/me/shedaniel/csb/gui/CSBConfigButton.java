package me.shedaniel.csb.gui;

import me.shedaniel.csb.CSB;
import net.minecraft.client.gui.widget.ButtonWidget;

public class CSBConfigButton extends ButtonWidget {
    public CSBConfigButton(int x, int y, int width, int height, String message) {
        super(x, y, width, height, message, widget -> CSB.openSettingsGUI());
    }
}
