package com.twemyeez.picklr.location;

import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.config.ConfigurationHandler.ConfigAttribute;
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

		// Check which side
		if (ConfigurationHandler.getConfigurationAttribute(
				ConfigAttribute.LOBBY_DISPLAY_SIDE).equals("right")) {
			// For the right, get a scaled resolution
			ScaledResolution scaledResolution = new ScaledResolution(this.mc,
					this.mc.displayWidth, this.mc.displayHeight);
			// Get width
			int scaledWidth = scaledResolution.getScaledWidth();
			// Calculate required start X
			int locationX = scaledWidth
					- mc.fontRenderer
							.getStringWidth(ServerLocationUtils.currentServerName);

			// Draw strng
			mc.fontRenderer.drawStringWithShadow(
					ServerLocationUtils.currentServerName, locationX, 1,
					0xffffffff);
		} else {
			// Otherwise, proceed to render on right
			mc.fontRenderer.drawStringWithShadow(
					ServerLocationUtils.currentServerName, 1, 1, 0xFFFFFF);
		}

	}
}
