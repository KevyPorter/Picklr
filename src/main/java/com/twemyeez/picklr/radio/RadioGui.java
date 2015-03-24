package com.twemyeez.picklr.radio;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RadioGui extends Gui {
	String statusIndicator = EnumChatFormatting.DARK_AQUA + new String("Playing radio")
			+ EnumChatFormatting.RESET;
	private Minecraft mc;

	public RadioGui(Minecraft mc) {
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
		if (!RadioUtils.inProgress) {
			return;
		}

		// Otherwise, proceed to render icon
		ScaledResolution screenResolution = new ScaledResolution(mc,
				mc.displayWidth, mc.displayHeight);
		int height = screenResolution.getScaledHeight();
		height = height - mc.fontRendererObj.FONT_HEIGHT;

		mc.fontRendererObj.drawString(statusIndicator, 1, height,
				0xFFFFFF);
	}

}
