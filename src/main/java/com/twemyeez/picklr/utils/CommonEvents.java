package com.twemyeez.picklr.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import com.twemyeez.picklr.Picklr;
import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.commands.DebugCommand;
import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.config.ConfigurationHandler.ConfigAttribute;
import com.twemyeez.picklr.location.ServerLocationUtils;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CommonEvents {

	/*
	 * Detecting server change is required for quite a few features. Thus, we
	 * decide to only have one listener for it - this one. It can be called
	 * multiple times per lobby change, hence we'll do a check so it's not run
	 * more than once per 5000ms.
	 */

	// This variable stores the last time a GUI was shown
	public static Long lastGuiShow = 0L;

	// This is the event listener
	@SubscribeEvent
	public void onGuiShow(GuiOpenEvent event) {
		if (CommonUtils.isHypixel()) {
			// The user is confirmed to be on the Hypixel network and the time
			// of last show is more than 5000ms ago
			if ((event.gui instanceof GuiDownloadTerrain)
					&& (lastGuiShow < (System.currentTimeMillis() - 5000))) {
				// Save the current time, to ensure we don't show the update
				// dialogue too often.
				lastGuiShow = System.currentTimeMillis();

				// This implies it is a server change
				ServerLocationUtils.sendServer();

				// Do the check for updates
				UpdateChecker.checkForUpdate();
				
				//Check if it's the first join
				if((Boolean) ConfigurationHandler.getConfigurationAttribute(ConfigAttribute.FIRST_JOIN))
				{
					new Thread(DebugCommand.introduction).start();
					ConfigurationHandler.setConfigurationAttribute(ConfigAttribute.FIRST_JOIN, false);
				}
			}
		}
	}

	/*
	 * This event handles the ability to make right clicking a player suggest
	 * various actions
	 */

	@SubscribeEvent
	public void onPlayerInteract(EntityInteractEvent event) {
		// If it is Hypixel and they right clicked a player
		if (event.target instanceof EntityPlayer && CommonUtils.isHypixel()) {

			// Get the player and get their name
			EntityPlayer Player = (EntityPlayer) event.target;
			String name = Player.getCommandSenderName();

			// Check the player is in a lobby
			if (!ServerLocationUtils.currentServerName.contains("lobby")) {
				// If they are not, return without doing anything
				return;
			}

			if (Minecraft.getMinecraft().thePlayer.getHeldItem() == null) {
				// Check they are holding nothing in their hands
				if (KeyBindings.changeRightClickActionToAdd.getIsKeyPressed()) {
					// See if they're pressing the key to add users

					// If so, create the chat style for adding a friend
					ChatStyle chatStyle = new ChatStyle()
							.setChatClickEvent(new ClickEvent(
									ClickEvent.Action.RUN_COMMAND, "/f add "
											+ name));

					// Set the hover event
					chatStyle = chatStyle.setChatHoverEvent(new HoverEvent(
							HoverEvent.Action.SHOW_TEXT,
							new ChatComponentText(EnumChatFormatting.BLUE
									+ "Click to " + EnumChatFormatting.YELLOW
									+ "add")));

					// Send the text to the user
					event.entityPlayer.addChatMessage(new ChatComponentText(
							EnumChatFormatting.GRAY + "["
									+ EnumChatFormatting.BLUE + Picklr.MODID
									+ EnumChatFormatting.GRAY + "] "
									+ EnumChatFormatting.BLUE
									+ "Click here to add "
									+ EnumChatFormatting.YELLOW + name)
							.setChatStyle(chatStyle));

				} else {

					// Create the chat style to suggest /telling the user
					ChatStyle chatStyle = new ChatStyle()
							.setChatClickEvent(new ClickEvent(
									ClickEvent.Action.SUGGEST_COMMAND, "/tell "
											+ name));
					// Set the chat hover event
					chatStyle = chatStyle.setChatHoverEvent(new HoverEvent(
							HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
									EnumChatFormatting.BLUE + "Click to "
											+ EnumChatFormatting.YELLOW
											+ "message")));
					// Send the text
					event.entityPlayer.addChatMessage(new ChatComponentText(
							EnumChatFormatting.GRAY + "["
									+ EnumChatFormatting.BLUE + Picklr.MODID
									+ EnumChatFormatting.GRAY + "] "
									+ EnumChatFormatting.BLUE
									+ "Click here to send a message to "
									+ EnumChatFormatting.YELLOW + name)
							.setChatStyle(chatStyle));
				}
			}
		}
	}

}
