package me.shedaniel.utils;

import me.shedaniel.CSB;
import me.shedaniel.api.ConfigRegistry;

public class ConfigSoftRegistry {
    
    public static void register() {
        ConfigRegistry.registerConfig("csb", () -> CSB.openSettingsGUI());
    }
    
}
