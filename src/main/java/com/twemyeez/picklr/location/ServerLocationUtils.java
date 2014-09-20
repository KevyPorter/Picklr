package com.twemyeez.picklr.location;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import com.twemyeez.picklr.utils.CommonUtils;

public class ServerLocationUtils {
	
	/*
	 * This method is used to send /whereami messages
	 */
	
	public static String currentServerName;
	
	public static void sendServer()
	{
		//sanity checks - make sure you're on Hypixel server
		if(CommonUtils.isHypixel())
		{
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/whereami");
		}
	}
	
	public static void setServer(String name)
	{
		/*
		 * Save the server locally for use if required
		 */
		currentServerName = name;
		
		/*
		 * TODO: Upload this via the API
		 */
		
	}
	
	public static void relatedChatEventHandler(ClientChatReceivedEvent event)
	{
		//We know that the message isn't null
		String message = event.message.getUnformattedText();
		
		/*
		 * Let's check if the message is a whereami response via checking if it starts with the correct string
		 */
		if(message.startsWith("You are currently on server"))
		{
			//Cancel the event so the user doesn't see the response
			event.setCanceled(true);
			//Save the server detected
			setServer(message.split(" ")[5]);
		}
	}
}
