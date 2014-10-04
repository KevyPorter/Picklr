package com.twemyeez.picklr.forums;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.config.ConfigurationHandler.ConfigAttribute;
import com.twemyeez.picklr.utils.CommonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ForumProcessor {

	/*
	 * This method is used to decide whether to do the forum checks based on
	 * whether they have the settings properly configured
	 */
	public static Boolean checkForumsEnabled() {
		// Get the username and password from the configuration
		String username = (String) ConfigurationHandler
				.getConfigurationAttribute(ConfigAttribute.FORUM_USERNAME);
		String password = (String) ConfigurationHandler
				.getConfigurationAttribute(ConfigAttribute.FORUM_PASSWORD);

		// Firstly we'll check that no credentials are null
		if (username == null && password == null) {
			System.out.println("Username or password are null.");
			return false;
		}

		// Also we need to check they haven't left the example username in the
		// config
		if (username.equals("example_username")) {
			return false;
		}

		// Otherwise, return true
		return true;
	}

	/*
	 * This method returns an alert for the forums, or an error message,
	 * depending upon if the request has succeeded. This message is designed to
	 * be presented directly to the user via chat.
	 */
	public static String getMessageNumberFromToken() {

		// Define the message prefix
		String forumMessagePrefix = EnumChatFormatting.GRAY + "["
				+ EnumChatFormatting.DARK_PURPLE + "Forum"
				+ EnumChatFormatting.GRAY + "] ";

		// Get the username and password from the configuration
		String username = (String) ConfigurationHandler
				.getConfigurationAttribute(ConfigAttribute.FORUM_USERNAME);
		String password = (String) ConfigurationHandler
				.getConfigurationAttribute(ConfigAttribute.FORUM_PASSWORD);

		// Let's log them in, via setting cookies and getting their XenForo
		// token.
		ForumLogin loginObject = new ForumLogin(username, password);

		// Check if the login has been successful
		if (loginObject.hasValidToken()) {
			// Using a try...catch block for everything is the most readable
			// solution here
			try {
				// Get the text response
				String textCollector = loginObject.getMessageRequestString();

				// Check for HTML at the start - this should not be there unless
				// Cloudflare DDoS protection mode is on
				if (textCollector.startsWith("<")) {
					// If it starts with < it's not going to be valid JSON so
					// we'll return now
					return forumMessagePrefix
							+ "The forums are currrently down, sorry.";
				} else if (textCollector
						.indexOf("_visitor_conversationsUnread") == -1
						|| textCollector.indexOf("_visitor_alertsUnread") == -1) {
					// If it doesn't contain the required attributes, this
					// indicates that the user hasn't been logged in well
					return forumMessagePrefix
							+ "Login error - please check your forum credentials.";
				} else {
					// Otherwise, we can assume a valid response and we'll
					// return the parsed response
					return forumMessagePrefix
							+ loginObject.getUserMessage((textCollector));
				}

			} catch (Exception e) {
				// There shouldn't be an exceptions, but if they are, alert the
				// user and print a stack trace
				e.printStackTrace();
				return forumMessagePrefix
						+ "An exception occured in forum checking. Please report this.";
			}

		} else {
			// No valid token indicates that there was a login error or the
			// forums are down.
			return forumMessagePrefix
					+ "Sorry, there was an error with your login, please try again later.";
		}
	}

	public static void initialise() {
		// This is the timer task used for the forums
		TimerTask forumChecker = new TimerTask() {

			@Override
			public void run() {
				// Check the forums are enabled and that we can send them a
				// message
				if (ForumProcessor.checkForumsEnabled()
						&& Minecraft.getMinecraft().thePlayer != null) {
					// If they're enabled, send the alert
					CommonUtils.sendFormattedChat(true,
							ForumProcessor.getMessageNumberFromToken(),
							EnumChatFormatting.GOLD, true);
				}
			}

		};

		// Create a timer
		Timer forumTimer = new Timer();

		// Schedule the timer to run every 5 minutes
		forumTimer.schedule(forumChecker, 60 * 1000, 5 * 60 * 1000);

	}
}
