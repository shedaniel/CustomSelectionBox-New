package me.shedaniel.utils;

import me.shedaniel.CSBConfig;
import me.shedaniel.RiftModList;
import me.shedaniel.listener.OpenModConfigListener;
import net.minecraft.client.Minecraft;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ConfigListener implements OpenModConfigListener {
    
    @Override
    public void openConfigGui(String s) {
        if (s.equals("csb")) {
            List<ConfigValue> list = new ArrayList<>();
            list.add(ConfigValue.createConfigValue("General", "Use 1.13-Like Block Rendering", ConfigValue.ValueType.BOOLEAN_BUTTON, CSBConfig.newBlockRendering));
            Minecraft.getInstance().displayGuiScreen(RiftModList.getConfigScreen(list, RiftModList.getModByID("csb"), values -> {
                values.forEach(configValue -> {
                    if (configValue.getName().equals("Use 1.13-Like Block Rendering")) {
                        CSBConfig.newBlockRendering = configValue.getAsBoolean();
                        try {
                            CSBConfig.saveConfig();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }));
        }
    }
    
    @Override
    public boolean hasConfigGui(String s) {
        return s.equals("csb");
    }
    
}
