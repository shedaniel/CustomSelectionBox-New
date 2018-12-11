package me.shedaniel.mixin;

import me.shedaniel.CSB;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.menu.SettingsGui;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.shedaniel.CSBConfig.diffButtonLoc;

@Mixin(SettingsGui.class)
public abstract class MixinGuiOptions extends Gui {
	
	@Inject(method = "onInitialized", at = @At("RETURN"))
	protected void onInitialized(CallbackInfo ci) {
		if (diffButtonLoc)
			addButton(new ButtonWidget(404, width - 150, 0, 150, 20, "Custom Selection Box") {
				@Override
				public void onPressed(double p_mousePressed_1_, double p_mousePressed_3_) {
					CSB.openSettingsGUI();
				}
			});
		else  // see GuiOptions.initGui
			addButton(new ButtonWidget(404, width / 2 - 75, height / 6 + 24 - 6, 150, 20, "Custom Selection Box") {
				@Override
				public void onPressed(double p_mousePressed_1_, double p_mousePressed_3_) {
					CSB.openSettingsGUI();
				}
			});
	}
	
}
