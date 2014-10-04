package com.twemyeez.picklr.radio;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class RadioGui extends Gui{
	String statusIndicator = EnumChatFormatting.DARK_AQUA+ "♫"+EnumChatFormatting.RESET;
	private Minecraft mc;

	public RadioGui(Minecraft mc)
	{
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
		
		//Otherwise, proceed to render icon
		ScaledResolution screenResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int height = screenResolution.getScaledHeight();
		height = height - mc.fontRenderer.FONT_HEIGHT;
	
		mc.fontRenderer.drawStringWithShadow(statusIndicator, 1, height, 0xFFFFFF);
	}
	
	
}
