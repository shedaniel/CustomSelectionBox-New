package me.shedaniel.mixin;

import me.shedaniel.gui.CSBConfigButton;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.SettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SettingsScreen.class)
public class MixinSettingsScreen extends Screen {
    
    @Inject(method = "onInitialized()V", at = @At(value = "RETURN"))
    private void onInitialized(CallbackInfo ci) {
        addButton(new CSBConfigButton(404, this.width / 2 - 75, this.height / 6 + 24 - 6, 150, 20, "Custom Selection Box"));
    }
    
}
