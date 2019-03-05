package me.shedaniel.mixin;

import me.shedaniel.gui.CSBConfigButton;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.SettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(SettingsScreen.class)
public abstract class MixinSettingsScreen extends Screen {
    
    @Inject(method = "onInitialized()V", at = @At(value = "RETURN"))
    private void onInitialized(CallbackInfo ci) {
        addButton(new CSBConfigButton(this.screenWidth / 2 - 75, this.screenHeight / 6 + 24 - 6, 150, 20, "Custom Selection Box"));
    }
    
}
