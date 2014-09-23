package com.twemyeez.picklr;

import com.twemyeez.picklr.hud.FriendOnlineHud;
import com.twemyeez.picklr.utils.CommonUtils;
import com.twemyeez.picklr.utils.UpdateChecker;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Picklr.MODID, version = Picklr.VERSION)
public class Picklr
{
    public static final String MODID = "Picklr";
    public static final String VERSION = "Lambda";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	/*
    	 * Carry out the various tasks required for the modification to run
    	 */
    	
    	//Register all event handlers
    	CommonUtils.registerHandlers();
    	
    	//Check for updates
    	UpdateChecker.requestLatestVersion();
    }
}

