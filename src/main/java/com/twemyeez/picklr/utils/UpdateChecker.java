package com.twemyeez.picklr.utils;

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
	private static String cachedNewRelease = Picklr.VERSION+" da";
	
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
}
