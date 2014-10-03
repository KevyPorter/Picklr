package com.twemyeez.picklr.config;

import java.io.File;

import com.twemyeez.picklr.Picklr;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	/*
	 * This is the main configuration
	 */
	static Configuration config;
	
	/*
	 * This holds all possible configuration attributes, to avoid having strings which could be mispelled etc and cause bugs.
	 */
	public enum ConfigAttribute {
		DEFAULT_LOBBY, AFK_RESPONSE, LOBBY_ENABLED, FORUM_USERNAME, FORUM_PASSWORD, LOBBY_DISPLAY_SIDE, PARTY_WARP_ENABLED;
	};

	//This returns the main configuration file
	public static Configuration getMainConfig(){
		return config;
	}
	
	//This is used to return the value for a certain attribute, it's an object because it's either boolean or string
	public static Object getConfigurationAttribute(ConfigAttribute a){
		//Check the attribute and return the value
		switch(a){
			case DEFAULT_LOBBY:
				return config.get(config.CATEGORY_GENERAL, "Default Lobby", "main", "This controls the default lobby chosen by the lobby button.").getString();
			case AFK_RESPONSE:
				return config.get(config.CATEGORY_GENERAL, "Do auto responses when AFK", true, "Setting this to false disables automatic replies when AFK mode is activated.").getBoolean(true);
			case LOBBY_ENABLED: 
				return config.get(config.CATEGORY_GENERAL, "Lobby button enabled", false, "This controls whether the lobby button function is enabled.").getBoolean(false);
			case FORUM_USERNAME:
				return config.get(config.CATEGORY_GENERAL, "Forum name", "example_username", "Please enter your Hypixel.net username here").getString();
			case FORUM_PASSWORD:
				return config.get(config.CATEGORY_GENERAL, "Forum password", "", "Please enter your Hypixel.net password here").getString();
			case LOBBY_DISPLAY_SIDE:
				return config.get(config.CATEGORY_GENERAL, "Side for display of data", "left", "For data such as the lobby or time HUD, this defines which it is shown on. It can either be true or false.").getString();
			case PARTY_WARP_ENABLED:
				return config.get(config.CATEGORY_GENERAL, "Party warp button",false, "This controls whether the party warp function is enabled.").getBoolean(false);
			default:
				return null;
		}
	}

	//This handles initialising the mod configuration
	public static void initialise(FMLPreInitializationEvent event) {
		//Get the configuration
		config = new Configuration(event.getSuggestedConfigurationFile());
		//Load it
		config.load();
			
		//Loop through attributes to create them if required
		for(ConfigAttribute attribute: ConfigAttribute.values())
		{
			getConfigurationAttribute(attribute);
		}
		config.save();
	}
	
	//This is an event handler used to monitor the changes to the config
	@SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent args) {
		//Check that the event applies to this modification
        if(args.modID.equals(Picklr.MODID))
        	if(config.hasChanged())
    		{
        		//If the config has changed, save it
    			config.save();
    		}
    }
}
