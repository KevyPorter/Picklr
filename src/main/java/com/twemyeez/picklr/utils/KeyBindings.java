package com.twemyeez.picklr.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.input.Keyboard;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
import com.twemyeez.picklr.Picklr;
import com.twemyeez.picklr.afk.AFKHandler;
import com.twemyeez.picklr.config.ConfigurationHandler;
import com.twemyeez.picklr.config.ConfigurationHandler.ConfigAttribute;
import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;
import com.twemyeez.picklr.location.ServerLocationUtils;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
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

	public static KeyBinding lobby = new KeyBinding("Lobby", Keyboard.KEY_L,
			"Picklr");

	public static KeyBinding afk = new KeyBinding("AFK Key", Keyboard.KEY_K,
			"Picklr");

	public static List<KeyBinding> pressedWithinTimeframe = new ArrayList<KeyBinding>();

	public KeyBindings() {
		// Register the friend list key binding
		ClientRegistry.registerKeyBinding(friendList);

		// Register the server HUD key binding
		ClientRegistry.registerKeyBinding(toggleLocationHud);

		// Register the right click add friend key
		ClientRegistry.registerKeyBinding(changeRightClickActionToAdd);

		// Register key binding for lobby
		ClientRegistry.registerKeyBinding(lobby);

		// Register key binding for afk
		ClientRegistry.registerKeyBinding(afk);
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
		
		if(afk.isPressed()){
			//Handle the AFK event
			AFKHandler.setAfk(!AFKHandler.getAfK());
		}

		if (lobby.isPressed()) {
			// If it was the lobby key

			// Check it is Hypixel
			if (!CommonUtils.isHypixel()) {
				return;
			}

			// Check if the lobby button is enabled
			if (!((Boolean) ConfigurationHandler
					.getConfigurationAttribute(ConfigAttribute.LOBBY_ENABLED))) {
				return;
			}

			// Otherwise, continue
			if (pressedWithinTimeframe.contains(lobby)) {
				// It has been pressed once within the timeframe so carry out
				// the action
				Minecraft.getMinecraft().thePlayer
						.sendChatMessage("/lobby "
								+ ConfigurationHandler
										.getConfigurationAttribute(ConfigAttribute.DEFAULT_LOBBY));

				// Alert the user
				CommonUtils
						.sendFormattedChat(
								true,
								EnumChatFormatting.GRAY
										+ "["
										+ EnumChatFormatting.LIGHT_PURPLE
										+ "Lobby"
										+ EnumChatFormatting.GRAY
										+ "] Lobbied to "
										+ ConfigurationHandler
												.getConfigurationAttribute(ConfigAttribute.DEFAULT_LOBBY),
								EnumChatFormatting.GOLD, true);

				// Remove the keybinding from the timeout
				Iterator<KeyBinding> iterator = pressedWithinTimeframe
						.iterator();
				// While there is more left to iterate, continue
				while (iterator.hasNext()) {
					// Check if it is the lobby keybinding
					if (iterator.next().equals(lobby)) {
						// If so, remove
						iterator.remove();
					}
				}
			} else {
				// Add the keybinding to the list
				pressedWithinTimeframe.add(lobby);
				// Then, schedule a new timer to remove it after 1 second
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						// Remove the keybinding from the timeout
						Iterator<KeyBinding> iterator = pressedWithinTimeframe
								.iterator();
						// While there is more left to iterate, continue
						while (iterator.hasNext()) {
							// Check if it is the lobby keybinding
							if (iterator.next().equals(lobby)) {
								// If so, remove
								iterator.remove();
							}
						}
					}

				}, 1000);
			}
		}
	}
}
