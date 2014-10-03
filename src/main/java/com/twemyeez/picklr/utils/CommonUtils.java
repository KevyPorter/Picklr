package com.twemyeez.picklr.utils;

import java.io.IOException;

import com.twemyeez.picklr.Picklr;
import com.twemyeez.picklr.commands.DebugCommand;
import com.twemyeez.picklr.commands.HudToggle;
import com.twemyeez.picklr.commands.RadioToggle;
import com.twemyeez.picklr.commands.RadioVolume;
import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.hud.FriendOnlineHud;
import com.twemyeez.picklr.listener.ChatListener;
import com.twemyeez.picklr.location.ServerLocationUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;

public class CommonUtils {
	
	/*
	 * This class handles miscellaneous methods which are either used often or did not fit elsewhere.
	 */
	
	//This method will register the event handlers for the modification
	public static void registerHandlers()
	{
		//register this class for events
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		
		//Register the key bindings class
		FMLCommonHandler.instance().bus().register(new KeyBindings());
		
		//Register the chat event handler
		MinecraftForge.EVENT_BUS.register(new ChatListener());
		
		//Register GUI overlay
		MinecraftForge.EVENT_BUS.register(new FriendOnlineHud(Minecraft.getMinecraft()));
		
		//register the /debug command which does session checking
		ClientCommandHandler.instance.registerCommand(new DebugCommand());
		
		//register the /volume command used to adjust radio volume
		ClientCommandHandler.instance.registerCommand(new RadioVolume());
		
		//register the /radio command which toggles radio on and off
		ClientCommandHandler.instance.registerCommand(new RadioToggle());
		
		//register the /hud command which toggles friend HUD on and off
		ClientCommandHandler.instance.registerCommand(new HudToggle());
		
		//Register the handler for configuration changes
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
	}
	
	/*
	 * This is used in various locations to ensure the user is connected to Hypixel, and not another server, so we don't interfere
	 * with any other server commands or similar.
	 */
	public static Boolean isHypixel()
	{
		try{
			//First, we'll check if they're singleplayer
			if(!FMLClientHandler.instance().getClient().isSingleplayer())
			{
				//This implies they're multiplayer - therefore we will check if their server IP contains Hypixel.net
				if(FMLClientHandler.instance().getClient().func_147104_D().serverIP.indexOf("hypixel.net") != -1)
				{
					return true;
				}
			}
			return false;
		}
		catch(Exception e)
		{
			/* If there has been an exception, return false. Sometimes the obfuscated functions return null, and without
			 * documentation of them, the easiest way to deal with this is a try...catch.
			 */
			return false;
		}
	}
	
	/*
	 * This can send a formatted chat, with the optional parameters defining traits
	 * 		- doPrefix defines whether the chat message will have a prefix of the MODID
	 * 		- doAlertBars determines whether the message will have, before and after, dashed bars
	 * 		  across chat for emphasis
	 */
	public static void sendFormattedChat(Boolean doPrefix, String message, EnumChatFormatting color, Boolean doAlertBars)
	{
		//Forge has a tendency to lose colour between lines. Setting each space to include the colour code too solves this.
		message = color + message.replace(" ", " "+color);
		
		//Prepend the mod ID prefix if it's being used.
		if(doPrefix)
		{
			message = EnumChatFormatting.BLUE + "["+Picklr.MODID+"] "+message;
		}
		
		//this is the alert bar string. It should just fill chat horizontally
		String alertBar = EnumChatFormatting.AQUA+"----------------------------------------------------";
		
		//if they've enabled alert bars, then show one before the message
		if(doAlertBars)
		{
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(alertBar));
		}
		
		//Send the message
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
		
		//if they've enabled alert bars, then show another one after the message
		if(doAlertBars)
		{
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(alertBar));
		}
	}
	
	
	
}
