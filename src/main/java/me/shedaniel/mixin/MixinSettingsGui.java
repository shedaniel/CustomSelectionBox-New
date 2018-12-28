package me.shedaniel.mixin;

import me.shedaniel.CSB;
import me.shedaniel.gui.CSBConfigButton;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.menu.SettingsGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SettingsGui.class)
public class MixinSettingsGui extends Gui {
    
    @Inject(method = "onInitialized()V", at = @At(value = "RETURN"))
    private void onInitialized(CallbackInfo ci) {
        addButton(new CSBConfigButton(404, this.width / 2 - 75, this.height / 6 + 24 - 6, 150, 20, "Custom Selection Box"));
    }
    
}
