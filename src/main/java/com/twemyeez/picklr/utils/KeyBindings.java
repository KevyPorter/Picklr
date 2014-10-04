package com.twemyeez.picklr.utils;

import org.lwjgl.input.Keyboard;

import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.config.ConfigurationHandler.ConfigAttribute;
import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;
import com.twemyeez.picklr.location.ServerLocationUtils;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyBindings {
	public static KeyBinding friendList = new KeyBinding("Friends list",
			Keyboard.KEY_F, "Picklr");

	public static KeyBinding toggleLocationHud = new KeyBinding("Location HUD",
			Keyboard.KEY_V, "Picklr");

	public static KeyBinding changeRightClickActionToAdd = new KeyBinding(
			"Right click to add", Keyboard.KEY_O, "Picklr");

	public KeyBindings() {
		// Register the friend list key binding
		ClientRegistry.registerKeyBinding(friendList);

		// Register the server HUD key binding
		ClientRegistry.registerKeyBinding(toggleLocationHud);
		
		//Register the right click add friend key
		ClientRegistry.registerKeyBinding(changeRightClickActionToAdd);
	}

	@SubscribeEvent
	public void KeyInputEvent(KeyInputEvent event) {
		/*
		 * There's been a key input event, so let's check what's been pressed
		 */

		// For the online friend list, if the key is pressed, we run the friend
		// list function
		if (friendList.isPressed()) {
			// Check if they are on Hypixel
			if (!CommonUtils.isHypixel()) {
				CommonUtils.sendFormattedChat(true,
						"You need to be on Hypixel to use this.",
						EnumChatFormatting.RED, true);
			} else {
				OnlineListManager.runFriendList();
			}
		}

		if (toggleLocationHud.isPressed()) {
			// Check if they are on Hypixel
			if (!CommonUtils.isHypixel()) {
				CommonUtils.sendFormattedChat(true,
						"You need to be on Hypixel to use this.",
						EnumChatFormatting.RED, true);
			} else {
				// Toggle the HUD
				ServerLocationUtils.locationHudEnabled = !ServerLocationUtils.locationHudEnabled;

				String status;
				if (ServerLocationUtils.locationHudEnabled) {
					status = "on";
				} else {
					status = "off";
				}

				// Define the message prefix
				String prefix = EnumChatFormatting.GRAY + "["
						+ EnumChatFormatting.DARK_GREEN + "HUD"
						+ EnumChatFormatting.GRAY + "] ";

				// Tell the user it is toggled
				CommonUtils.sendFormattedChat(true, prefix
						+ "Toggled server HUD display " + status,
						EnumChatFormatting.GOLD, true);

				// Save the change to config
				ConfigurationHandler.setConfigurationAttribute(
						ConfigAttribute.HUD_ACTIVE,
						ServerLocationUtils.locationHudEnabled);
			}

		}
	}
}
