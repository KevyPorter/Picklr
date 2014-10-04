package com.twemyeez.picklr.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.twemyeez.picklr.listener.ChatListener;
import com.twemyeez.picklr.listener.ChatListener.ChatStatus;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class SessionAuth {
	/*
	 * This class is a work in progress and is not currently used anywhere. The
	 * aim of this class is going to be to handle authentication with the Picklr
	 * API when it is released, because the PickledChat API was somewhat flawed
	 * in this respect.
	 */

	/*
	 * This method returns the Picklr session token
	 */

	// Holds system time in millis of last request start
	public static Long lastTokenRequestInitialisation = 0L;
	// Holds system time in millis of last request completion
	public static Long lastTokenRequestCompletion = 0L;

	// Holds the most recent token
	public static String token = "";

	// This method is used to initialise a token request
	public static void startTokenRequest() {
		// Add the token request status
		ChatListener.currentStatus.add(ChatStatus.TOKEN_REQUEST);

		// Start a token request
		Minecraft.getMinecraft().thePlayer
				.sendChatMessage("/tell twemyeez Picklr");

		// Store the start time
		lastTokenRequestInitialisation = System.currentTimeMillis();
	}

	/*
	 * This method is used to check if we've got a token ready for processing or
	 * if we should wait
	 */
	public static Boolean checkTokenValid() {
		// If there is no new token, return false
		if (token.equals("")) {
			return false;
		}

		// If the last token is older than the last request, then return false
		// as there is a new one pending
		if (lastTokenRequestInitialisation > lastTokenRequestCompletion) {
			return false;
		}

		// Otherwise return true
		return true;
	}

	/*
	 * This handles chat parsing for when it is required
	 */
	public static void relatedChatEventHandler(ClientChatReceivedEvent event) {
		// We know that the message isn't null
		String message = event.message.getUnformattedText();
		// Check if the message is from the correct user
		if (message.startsWith("From twemyeez:")) {
			// Get the UUID
			token = message.split(" ")[2];

			// Cancel the message
			event.setCanceled(true);

			// Set the last message date
			lastTokenRequestCompletion = System.currentTimeMillis();

			// Print the message to console
			System.out.println(message);

			// Remove the status
			ChatListener.currentStatus.remove(ChatStatus.TOKEN_REQUEST);
		}

		// Check if the message is the request
		if (message.equals("To twemyeez: Picklr")) {
			// Cancel the message
			event.setCanceled(true);

			// Print the message to console
			System.out.println(message);
		}

	}

	/*
	 * This method safely gets the authentication token
	 */
	public static String getToken() {
		// Check if the token is valid, if not, request one
		if (!checkTokenValid()) {
			startTokenRequest();
		}

		// Return the token
		return token;
	}

}
