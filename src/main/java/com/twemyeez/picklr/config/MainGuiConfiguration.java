package com.twemyeez.picklr.config;

import com.twemyeez.picklr.Picklr;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class MainGuiConfiguration extends GuiConfig {
	
	/*
	 * This creates a new config GUI for the main Picklr config
	 */
    public MainGuiConfiguration(GuiScreen parent) {
    	//Call constructor the create the GUI
        super(parent,
                new ConfigElement(ConfigurationHandler.getMainConfig().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                Picklr.MODID, //Register the config for the correct mod ID
                false, //define no world restart needed
                false, //define no MC restart required
                GuiConfig.getAbridgedConfigPath(ConfigurationHandler.getMainConfig().toString()));
    }
    
}