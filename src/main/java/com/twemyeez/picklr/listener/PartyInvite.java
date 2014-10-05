package com.twemyeez.picklr.listener;

import java.util.Timer;
import java.util.TimerTask;

import com.twemyeez.picklr.listener.ChatListener.ChatStatus;
import com.twemyeez.picklr.utils.CommonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class PartyInvite {

	final static String prefix = EnumChatFormatting.GRAY + "["
			+ EnumChatFormatting.DARK_RED + "Party" + EnumChatFormatting.GRAY
			+ "] ";

	public static void checkForPartyInvite(ClientChatReceivedEvent event) {
		String message = event.message.getUnformattedText();

		if (message.indexOf(" has invited you to join their party") != -1
				&& message.indexOf("to join! You have") != -1) {
			/*
			 * Syntax of message is
			 * "twemyeez has invited you to join their party! /party accept twemyeez to join! You have 60 seconds to accept."
			 */

			// Process string to get username
			int start = message.indexOf("/party accept");
			message = message.substring(start);
			message = message.split(" ")[2];

			// Accept invite
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/p accept "
					+ message);

			// Remove chat status
			ChatListener.currentStatus.remove(ChatStatus.PARTY_INVITE);

			// Tell user
			CommonUtils.sendFormattedChat(true, prefix
					+ "Accepted invite from " + message,
					EnumChatFormatting.BLUE, true);
		}
	}

	public static void togglePartyMode() {
		// See if we are disabling or enabling
		if (ChatListener.currentStatus.contains(ChatStatus.PARTY_INVITE)) {
			// Remove chat status
			ChatListener.currentStatus.remove(ChatStatus.PARTY_INVITE);
			// Tell user
			CommonUtils.sendFormattedChat(true, prefix
					+ "Turned party invite mode " + EnumChatFormatting.AQUA
					+ "off", EnumChatFormatting.BLUE, true);
		} else {
			// Remove chat status
			ChatListener.currentStatus.add(ChatStatus.PARTY_INVITE);
			// Tell user
			CommonUtils.sendFormattedChat(true, prefix
					+ "Turned party invite mode " + EnumChatFormatting.AQUA
					+ "on. It will expire after 60 seconds",
					EnumChatFormatting.BLUE, true);

			// Schedule the expiration of party mode
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					// See if party mode is still active
					if (ChatListener.currentStatus
							.contains(ChatStatus.PARTY_INVITE)) {
						// If so, disable it
						ChatListener.currentStatus
								.remove(ChatStatus.PARTY_INVITE);

						// Tell the user
						CommonUtils.sendFormattedChat(true, prefix
								+ "Party invite mode expired.",
								EnumChatFormatting.BLUE, true);
					}
				}

			}, 60 * 1000);
		}
	}
}
