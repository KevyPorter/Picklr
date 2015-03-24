package com.twemyeez.picklr.utils;

import java.io.IOException;

import com.twemyeez.picklr.Picklr;
import com.twemyeez.picklr.commands.BulkFriend;
import com.twemyeez.picklr.commands.RadioToggle;
import com.twemyeez.picklr.commands.RadioVolume;
import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.forums.ForumProcessor;
import com.twemyeez.picklr.listener.ChatListener;
import com.twemyeez.picklr.location.LocationGui;
import com.twemyeez.picklr.location.ServerLocationUtils;
import com.twemyeez.picklr.radio.RadioGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;

public class CommonUtils {

	/*
	 * This class handles miscellaneous methods which are either used often or
	 * did not fit elsewhere.
	 */

	// This method will register the event handlers for the modification
	public static void registerHandlers() {
		// register this class for events
		MinecraftForge.EVENT_BUS.register(new CommonEvents());

		// Register the key bindings class
		FMLCommonHandler.instance().bus().register(new KeyBindings());

		// Register the chat event handler
		MinecraftForge.EVENT_BUS.register(new ChatListener());

		// Register Radio Gui overlay
		MinecraftForge.EVENT_BUS
				.register(new RadioGui(Minecraft.getMinecraft()));

		// Register Radio Gui overlay
		MinecraftForge.EVENT_BUS.register(new LocationGui(Minecraft
				.getMinecraft()));

		// register the /volume command used to adjust radio volume
		ClientCommandHandler.instance.registerCommand(new RadioVolume());

		// register the /radio command which toggles radio on and off
		ClientCommandHandler.instance.registerCommand(new RadioToggle());

		// register the /bulkfriend command which processes or denies all friend
		// requests
		ClientCommandHandler.instance.registerCommand(new BulkFriend());

		// Register the handler for configuration changes
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());

		// Call the method that will schedule the forum timer task
		ForumProcessor.initialise();
	}

	/*
	 * This is used in various locations to ensure the user is connected to
	 * Hypixel, and not another server, so we don't interfere with any other
	 * server commands or similar.
	 */
	public static Boolean isHypixel() {
		try {
			// First, we'll check if they're singleplayer
			if (!FMLClientHandler.instance().getClient().isSingleplayer()) {
				// This implies they're multiplayer - therefore we will check if
				// their server IP contains Hypixel.net
				if (FMLClientHandler.instance().getClient().func_147104_D().serverIP
						.indexOf("hypixel.net") != -1) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			/*
			 * If there has been an exception, return false. Sometimes the
			 * obfuscated functions return null, and without documentation of
			 * them, the easiest way to deal with this is a try...catch.
			 */
			return false;
		}
	}

	/*
	 * This can send a formatted chat, with the optional parameters defining
	 * traits - doPrefix defines whether the chat message will have a prefix of
	 * the MODID - doAlertBars determines whether the message will have, before
	 * and after, dashed bars across chat for emphasis
	 */
	public static void sendFormattedChat(Boolean doPrefix, String message,
			EnumChatFormatting color, Boolean doAlertBars) {
		try {
			// Forge has a tendency to lose colour between lines. Setting each
			// space
			// to include the colour code too solves this.
			message = color + message.replace(" ", " " + color);

			// Prepend the mod ID prefix if it's being used.
			if (doPrefix) {
				message = EnumChatFormatting.GRAY + "["
						+ EnumChatFormatting.BLUE + Picklr.MODID
						+ EnumChatFormatting.GRAY + "] " + message;
			}

			// this is the alert bar string. It should just fill chat
			// horizontally
			String alertBar = EnumChatFormatting.AQUA
					+ "-----------------------------------------------------";

			// if they've enabled alert bars, then show one before the message
			if (doAlertBars) {
				Minecraft.getMinecraft().thePlayer
						.addChatMessage(new ChatComponentText(alertBar));
			}

			// Send the message
			Minecraft.getMinecraft().thePlayer
					.addChatMessage(new ChatComponentText(message));

			// if they've enabled alert bars, then show another one after the
			// message
			if (doAlertBars) {
				Minecraft.getMinecraft().thePlayer
						.addChatMessage(new ChatComponentText(alertBar));
			}
		} catch (Exception e) {
			// This often occurs if a player leaves but the mod still tries to
			// send a message. Just print the stack trace
			e.printStackTrace();
		}
	}

	public static void sendFormattedChatWithPrefix(IChatComponent msg,
			Boolean doAlertBars) {
		// Create the main message prefix
		IChatComponent mainMessage = new ChatComponentText(
				EnumChatFormatting.GRAY + "[" + EnumChatFormatting.BLUE
						+ Picklr.MODID + EnumChatFormatting.GRAY + "] ");

		// this is the alert bar string. It should just fill chat horizontally
		String alertBar = EnumChatFormatting.AQUA
				+ "-----------------------------------------------------";

		// if they've enabled alert bars, then show one before the message
		if (doAlertBars) {
			Minecraft.getMinecraft().thePlayer
					.addChatMessage(new ChatComponentText(alertBar));
		}

		// Send the message
		Minecraft.getMinecraft().thePlayer.addChatMessage(mainMessage
				.appendSibling(msg));

		// if they've enabled alert bars, then show another one after the
		// message
		if (doAlertBars) {
			Minecraft.getMinecraft().thePlayer
					.addChatMessage(new ChatComponentText(alertBar));
		}
	}

}
