package com.twemyeez.picklr.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.util.EnumChatFormatting;

import com.twemyeez.picklr.Picklr;

public class UpdateChecker {
	/*
	 * This class handles checking for updates, and presenting them to the user.
	 */
	
	/*
	 * We don't want to run an update every single time the user does a command. Therefore, we cache the server result here
	 * and used this cached result instead. It's only reloaded when the modification initialises, so they'd need to restart Minecraft
	 * for a new update to show. However, this is unlikely to be an issue for most users, as very few people leave Minecraft open
	 * forever.
	 */
	private static String cachedNewRelease = Picklr.VERSION;
	
	/*
	 * Update messages can be annoying, so we only display them once every 5 minutes maximum. Therefore, this is used to store
	 * the last time (in milliseconds) that there was a message given to the user. Default is 0, because we should always show
	 * the first update
	 */
	private static Long lastMessage = 0L;
	
	/*
	 * This method will be called whenever the user runs a command/function in PickledChat
	 */
	public static void checkForUpdate()
	{
		/*
		 * We calculate the system time 5 minutes ago, to make sure we don't show the message too often
		 */
		Long thresholdTime = System.currentTimeMillis() - 5*60*1000;
		
		/*
		 * If their version isn't up to date and it's been over 5 minutes since the last message
		 */
		if(!Picklr.VERSION.equals(cachedNewRelease) && thresholdTime > lastMessage)
		{
			CommonUtils.sendFormattedChat(true, "It appears the mod is not up to date. Please check the thread for exciting new updates!", EnumChatFormatting.RED, true);
		}
	}

	/*
	 * This method will deal with the async web request to retrieve the latest Picklr Version
	 */
	public static void requestLatestVersion() {
		//Start a new thread to do the web request
		
		new Thread(new Runnable(){
			
			@Override
			public void run() {
				/*
				 * We run this entire block in a try, because if it fails, it is unlikely any following step will be sucessful
				 */
				
				try
				{
					//Define the update URL
					URL website = new URL("https://twemyeez.com/Picklr/version.txt");
					
					//Open a connection to the website
					URLConnection connection = website.openConnection();
					connection.setConnectTimeout(1500);
					connection.setReadTimeout(1500);
					      
					//This header eliminates some issues with user agent filtering
					connection.addRequestProperty("User-Agent", 
					                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

					//Read the input
					BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

					//Make a stringbuilder
					StringBuilder responseBuilder = new StringBuilder();
					
					//Define a string to temporarily hold the input lines
					String inputLine = null;

					//Build the output from each line of the input
					while ((inputLine = input.readLine()) != null) 
					{
						responseBuilder.append(inputLine);
					}

					//Close the buffered reader
					input.close();

					//Save the retrieved version
					UpdateChecker.cachedNewRelease = responseBuilder.toString();
				}
				catch(Exception e)
				{
					//The current version has a safe default, so we'll just print a stack trace
					e.printStackTrace();
				}
				
			}
		}).start();
	}
}
