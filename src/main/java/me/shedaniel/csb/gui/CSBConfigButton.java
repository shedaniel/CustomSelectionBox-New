package me.shedaniel.csb.gui;

import me.shedaniel.csb.CSB;
import net.minecraft.client.gui.widget.ButtonWidget;

public class CSBConfigButton extends ButtonWidget {
    public CSBConfigButton(int int_2, int int_3, int int_4, int int_5, String string_1) {
        super(int_2, int_3, int_4, int_5, string_1, widget -> CSB.openSettingsGUI());
    }
}
