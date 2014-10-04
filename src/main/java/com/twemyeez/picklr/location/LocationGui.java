package com.twemyeez.picklr.location;

import com.twemyeez.picklr.radio.RadioUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class LocationGui extends Gui {
	private Minecraft mc;

	public LocationGui(Minecraft mc) {
		super();

		// Set the value of the mc variable
		this.mc = mc;
	}

	@SubscribeEvent
	public void onRenderOfOverlay(RenderGameOverlayEvent event) {
		// Check the event is not cancellable
		if (event.isCancelable()) {
			return;
		}

		// Check it's not for experience
		if (event.type != ElementType.EXPERIENCE) {
			return;
		}

		// Check if hud enabled
		if (!ServerLocationUtils.locationHudEnabled) {
			return;
		}

		// Otherwise, proceed to render icon
		mc.fontRenderer.drawStringWithShadow(
				ServerLocationUtils.currentServerName, 1, 1, 0xFFFFFF);
	}
}
