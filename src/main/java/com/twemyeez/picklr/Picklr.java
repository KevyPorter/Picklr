package com.twemyeez.picklr;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.utils.CommonUtils;
import com.twemyeez.picklr.utils.UpdateChecker;



@Mod(modid = Picklr.MODID, version = Picklr.VERSION, guiFactory = "com.twemyeez.picklr.config.ConfigGuiFactory")
public class Picklr {
	public static final String MODID = "Picklr";
	public static final String VERSION = "Lambda";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		/*
		 * Carry out the various tasks required for the config to run
		 */

		ConfigurationHandler.initialise(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		/*
		 * Carry out the various tasks required for the modification to run
		 */

		// Print debug
		System.out
				.println(Minecraft.getMinecraft().mcDataDir.getAbsoluteFile());

		// Install libraries if required
		InstallManager.unzip();

		// Register all event handlers
		CommonUtils.registerHandlers();

		// Check for updates
		UpdateChecker.requestLatestVersion();

	}
}
