package com.twemyeez.picklr.auth;

import net.minecraft.client.Minecraft;

public class AfkAPI {
	/*
	 * This asynchronously writes to the API saying that the AFK status has
	 * changed
	 */
	public static void changeAfkStatus(Boolean status) {
		// Construct the API url
		String url = "https://picklr.me/api/v1/setafk.php?username="
				+ Minecraft.getMinecraft().thePlayer.getCommandSenderName()
				+ "&r_uuid=" + SessionAuth.getToken() + "&afk=" + status;

		// Write the new location to the API in a new thread to avoid lag
		CommonAPI.carryOutAsyncApiWrite(url);

		// Print a debug message
		System.out.println(url);
	}
}
