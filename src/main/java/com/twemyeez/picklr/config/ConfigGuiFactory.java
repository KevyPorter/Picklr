package com.twemyeez.picklr.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigGuiFactory implements IModGuiFactory {

	/*
	 * This is the GUI factory for the modification, it has little function
	 */

	// Basic initialisation
	@Override
	public void initialize(Minecraft instance) {
	}

	// Return the main config GUI screen
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return MainGuiConfiguration.class;
	}

	// Return the GUI categories
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	// Return the option handler
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement e) {
		return null;
	}

}
