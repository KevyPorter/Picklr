package com.twemyeez.picklr.afk;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import com.twemyeez.picklr.auth.AfkAPI;
import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.config.ConfigurationHandler.ConfigAttribute;
import com.twemyeez.picklr.location.ServerLocationUtils;
import com.twemyeez.picklr.utils.CommonUtils;

public class AFKHandler {
	static Boolean afkStatus = false;
	final static String prefix = EnumChatFormatting.GRAY + "["
			+ EnumChatFormatting.YELLOW + "AFK" + EnumChatFormatting.GRAY
			+ "] ";

	static List<String> messagesSaved = new ArrayList<String>();
	static List<String> cachedUsernames = new ArrayList<String>();

	/*
	 * This handles changing the AFK status
	 */
	public static void setAfk(Boolean value) {
		// Do API write
		AfkAPI.changeAfkStatus(value);

		// Get a nice readable "on" or "off" from the value
		String status;
		if (value) {
			status = "on";
		} else {
			status = "off";
		}
		// Tell the user it has been toggled
		CommonUtils.sendFormattedChat(true, prefix
				+ "AFK mode has been toggled " + status,
				EnumChatFormatting.BLUE, true);

		// Set the status
		afkStatus = value;

		if (value == false) {
			// Tell them the messages
			CommonUtils.sendFormattedChat(true, prefix
					+ "You recieved the following: ", EnumChatFormatting.BLUE,
					false);

			// Count the way through
			int i = 1;

			// Go through all messages
			for (String message : messagesSaved) {
				// Say message + number
				CommonUtils.sendFormattedChat(false, EnumChatFormatting.BLUE
						+ "Message " + EnumChatFormatting.BLUE + i + ": "
						+ EnumChatFormatting.WHITE + message,
						EnumChatFormatting.WHITE, false);
				// Increment counter
				i++;
			}

			// If there were no messages, say so
			if (i == 1) {
				CommonUtils.sendFormattedChat(true, "No messages recieved",
						EnumChatFormatting.YELLOW, false);
			}
			// Clear saved messages
			messagesSaved.clear();

			// Clear cached usernames
			cachedUsernames.clear();
		}
	}

	/*
	 * This returns whether or not the user is AFK
	 */
	public static Boolean getAfK() {
		return afkStatus;
	}

	public static void handleChat(ClientChatReceivedEvent event) {
		// See if they are afk
		if (getAfK()) {

			// Get the message
			String message = event.message.getUnformattedText();

			// if it starts with "From" it's a /tell
			if (message.startsWith("From")) {
				// Split it into words
				String[] messageSplit = message.split(" ");

				// Try to parse the username
				String username = "";
				// Determine if they are a rank or not
				if (messageSplit[1].indexOf("[") != -1) {
					// If there is a [ in the second word then they are a rank
					if (message.replace("From ", "").startsWith("[JR HELPER]")) {
						// Get the third word
						username = messageSplit[3].replace(":", "");
					} else {
						// Otherwise get the second word
						username = messageSplit[2].replace(":", "");
					}
				} else {
					// Default rank
					username = messageSplit[1].replace(":", "");
				}

				// Add the message to the saved ones
				messagesSaved.add(message);

				// Check if autoresponse is enabled and you haven't already
				// answered them
				if (((Boolean) ConfigurationHandler
						.getConfigurationAttribute(ConfigAttribute.AFK_RESPONSE_ENABLED))
						&& !cachedUsernames.contains(username)) {

					// Add their username to the cache
					cachedUsernames.add(username);

					// Tell them the response
					Minecraft.getMinecraft().thePlayer
							.sendChatMessage("/tell "
									+ username
									+ " "
									+ ConfigurationHandler
											.getConfigurationAttribute(ConfigAttribute.AFK_RESPONSE));

					// Copy username into a final variable
					final String usernameToRemove = username;

					// Schedule it to be removed
					new Timer().schedule(new TimerTask() {

						@Override
						public void run() {
							cachedUsernames.remove(usernameToRemove);
						}

					}, 30 * 1000);
				}

			}

		}
	}
}
