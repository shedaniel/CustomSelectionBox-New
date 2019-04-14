package me.shedaniel.csb.mixin;

import me.shedaniel.csb.gui.CSBSettingsGui;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ingame.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class MixinChatScreen extends Screen {
    
    @Shadow
    protected TextFieldWidget chatField;
    
    protected MixinChatScreen(TextComponent textComponent_1) {
        super(textComponent_1);
    }
    
    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;openScreen(Lnet/minecraft/client/gui/Screen;)V", ordinal = 1), cancellable = true)
    public void keyPressed(int int_1, int int_2, int int_3, CallbackInfoReturnable<Boolean> ci) {
        String[] split = this.chatField.getText().trim().toLowerCase().split(" ");
        if (split.length > 0 && split[0].contentEquals("/csbconfig") && minecraft.currentScreen instanceof CSBSettingsGui)
            ci.setReturnValue(true);
    }
    
}
