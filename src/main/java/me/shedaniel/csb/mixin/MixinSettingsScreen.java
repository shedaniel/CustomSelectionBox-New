package me.shedaniel.csb.mixin;

import me.shedaniel.csb.gui.CSBConfigButton;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.menu.SettingsScreen;
import net.minecraft.text.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(SettingsScreen.class)
public abstract class MixinSettingsScreen extends Screen {
    
    protected MixinSettingsScreen(TextComponent textComponent_1) {
        super(textComponent_1);
    }
    
    @Inject(method = "init()V", at = @At(value = "RETURN"))
    private void onInitialized(CallbackInfo ci) {
        addButton(new CSBConfigButton(this.width / 2 - 75, this.height / 6 + 24 - 6, 150, 20, "Custom Selection Box"));
    }
    
}
