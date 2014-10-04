package com.twemyeez.picklr.location;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import com.twemyeez.picklr.auth.CommonAPI;
import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.listener.ChatListener;
import com.twemyeez.picklr.listener.ChatListener.ChatStatus;
import com.twemyeez.picklr.utils.CommonUtils;

public class ServerLocationUtils {

	// This string stores the current location of the user on the Hypixel Server
	public static String currentServerName;

	/*
	 * This method is used to send /whereami messages
	 */

	public static void sendServer() {
		// sanity checks - make sure you're on Hypixel server
		if (CommonUtils.isHypixel()) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/whereami");
		}

		// Register the status to say we've requested the location
		ChatListener.currentStatus.add(ChatStatus.WHEREAMI);
	}

	// This method handles setting the server and the API update
	public static void setServer(String name) {
		System.out.println("Set the server name");
		/*
		 * Save the server locally for use if required
		 */
		currentServerName = name;

		// Construct the API url
		String url = "https://twemyeez.com/Picklr/api/setlocation.php?username="
				+ Minecraft.getMinecraft().thePlayer.getCommandSenderName()
				+ "&r_uuid=" + SessionAuth.getToken() + "&server=" + name;

		// Write the new location to the API in a new thread to avoid lag
		CommonAPI.carryOutAsyncApiWrite(url);
	}

	/*
	 * This event handler handles any events which may be relevant to the server
	 * location context
	 */
	public static void relatedChatEventHandler(ClientChatReceivedEvent event) {
		System.out.println("Passing chat event to server location parser");

		// We know that the message isn't null
		String message = event.message.getUnformattedText();

		/*
		 * Let's check if the message is a whereami response via checking if it
		 * starts with the correct string
		 */
		if (message.startsWith("You are currently on server")) {
			// Debug message output used here
			System.out.println("Processed user location");

			// Cancel the event so the user doesn't see the response
			event.setCanceled(true);
			// Save the server detected
			setServer(message.split(" ")[5]);

			// Now, we will remove this status, because it's served the purpose
			// it was meant to
			ChatListener.currentStatus.remove(ChatStatus.WHEREAMI);
		}
	}
}
