package me.shedaniel.mixin;

import me.shedaniel.CSB;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.shedaniel.CSBConfig.diffButtonLoc;

@Mixin(GuiOptions.class)
public abstract class MixinGuiOptions extends GuiScreen {
	
	@Inject(method = "initGui", at = @At("RETURN"))
	protected void init(CallbackInfo ci) {
		if (diffButtonLoc)
			addButton(new GuiButton(404, width - 150, 0, 150, 20, "Custom Selection Box") {
				@Override
				public void onClick(double p_mousePressed_1_, double p_mousePressed_3_) {
					CSB.openSettingsGUI();
				}
			});
		else  // see GuiOptions.initGui
			addButton(new GuiButton(404, width / 2 - 75, height / 6 + 24 - 6, 150, 20, "Custom Selection Box") {
				@Override
				public void onClick(double p_mousePressed_1_, double p_mousePressed_3_) {
					CSB.openSettingsGUI();
				}
			});
	}
	
}
