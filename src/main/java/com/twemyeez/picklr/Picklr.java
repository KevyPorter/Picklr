package com.twemyeez.picklr;

import com.twemyeez.picklr.hud.FriendOnlineHud;
import com.twemyeez.picklr.utils.CommonUtils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Picklr.MODID, version = Picklr.VERSION)
public class Picklr
{
    public static final String MODID = "Picklr";
    public static final String VERSION = "Lambda";
    public static Picklr mod;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	CommonUtils.registerHandlers();
    	this.mod = this;
    }
}

