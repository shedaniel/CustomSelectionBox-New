package me.shedaniel.csb;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.csb.gui.CSBSettingsScreen;

public class CSBModMenuApiImpl implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return (screen) -> new CSBSettingsScreen(screen);
	}
}
