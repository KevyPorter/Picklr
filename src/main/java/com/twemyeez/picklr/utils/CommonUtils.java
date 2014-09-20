package com.twemyeez.picklr.utils;

import com.twemyeez.picklr.Picklr;
import com.twemyeez.picklr.commands.DebugCommand;
import com.twemyeez.picklr.commands.RadioToggle;
import com.twemyeez.picklr.commands.RadioVolume;
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
	
	public static void registerHandlers()
	{
		//register this class for events
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		
		//Register the key bindings class
		MinecraftForge.EVENT_BUS.register(new KeyBindings());
		
		//Register the chat event handler
		MinecraftForge.EVENT_BUS.register(new ChatListener());
		
		//register the /debug command which does session checking
		ClientCommandHandler.instance.registerCommand(new DebugCommand());
		
		//register the /volume command used to adjust radio volume
		ClientCommandHandler.instance.registerCommand(new RadioVolume());
		
		//register the /radio command which toggles radio on and off
		ClientCommandHandler.instance.registerCommand(new RadioToggle());
	}
	
	/*
	 * This is used in various locations to ensure the user is connected to Hypixel, and not another server, so we don't interfere
	 * with any other server commands or similar.
	 */
	public static Boolean isHypixel()
	{
		try{
			if(!FMLClientHandler.instance().getClient().isSingleplayer())
			{
				if(FMLClientHandler.instance().getClient().func_147104_D().serverIP.indexOf("hypixel.net") != -1)
				{
					return true;
				}
			}
			return false;
		}
		catch(Exception e)
		{
			//if there has been an exception, return false
			return false;
		}
	}
	
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
