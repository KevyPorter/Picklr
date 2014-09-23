package com.twemyeez.picklr.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;

import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.location.ServerLocationUtils;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CommonEvents {
	
	/*
	 * Detecting server change is required for quite a few features. Thus, we decide to only have one listener for it - this one.
	 * It can be called multiple times per lobby change, hence we'll do a check so it's not run more than once per 1000ms.
	 */
	
	//This variable stores the last time a GUI was shown
	public static Long lastGuiShow = 0L;
	
	//This is the event listener
	 @SubscribeEvent
	 public void onGuiShow(GuiOpenEvent event) {
	      if(CommonUtils.isHypixel())
	      {
	    	  //The user is confirmed to be on the Hypixel network and the time of last show is more than 1000ms ago
	    	  if((event.gui instanceof GuiDownloadTerrain) && (lastGuiShow < (System.currentTimeMillis() - 1000)))
	    	  {
	    		  //This implies it is a server change
	    		  ServerLocationUtils.sendServer();
	    		  
	    		  //Do the check for updates
	    		  UpdateChecker.checkForUpdate();
	    		  
	    		  //Save the current time, to ensure we don't show the update dialogue too often.
	    		  lastGuiShow = System.currentTimeMillis();
	    	  }
	      }
	     
	 }
	 
	 
}
