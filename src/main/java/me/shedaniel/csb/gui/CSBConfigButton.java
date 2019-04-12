package me.shedaniel.csb.gui;

import me.shedaniel.csb.CSB;
import net.minecraft.client.gui.widget.ButtonWidget;

public class CSBConfigButton extends ButtonWidget {
    public CSBConfigButton(int x, int y, int z, int height, String message) {
        super(x, y, z, height, message, widget -> CSB.openSettingsGUI());
    }
}
