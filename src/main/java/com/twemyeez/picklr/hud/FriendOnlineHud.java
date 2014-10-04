package com.twemyeez.picklr.hud;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;
import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;
import com.twemyeez.picklr.utils.CommonUtils;
import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class FriendOnlineHud extends Gui {
	/*
	 * This class handles rendering the friend online hud
	 */

	// We store a Minecraft object for rendering
	private Minecraft mc;

	// This boolean stores whether the friend hud is enabled
	public static Boolean hudEnabled = false;

	// This is the friend list buffer
	private static List<Friend> friendBuffer = new ArrayList<Friend>();

	// This is the increment timer
	private static Timer incrementTimer = new Timer();

	// In the constructor, we pass the Minecraft object
	public FriendOnlineHud(Minecraft mc) {
		super();

		// Set the value of the mc variable
		this.mc = mc;
	}

	// This handles safely toggling the HUD
	public static void toggleHud() {
		// Check if the hud is already enabled
		if (!hudEnabled) {
			// Set the HUD to enabled
			hudEnabled = true;
			// Send the first data request
			OnlineListManager.sendBackgroundRequest();
		} else {
			// It's already enabled, so warn the user
			CommonUtils.sendFormattedChat(true, "Friend HUD now disabled",
					EnumChatFormatting.RED, true);

			// Disable HUD
			hudEnabled = false;

			// Clear status
			resetStatus();
		}
	}

	// This is used to set the friend buffer to a copy of the other array
	public static void setFriendBuffer(List<Friend> targetList) {
		try {
			// Save the current friend
			Friend playerTemoraryBuffer = friendBuffer.get(currentI);

			// Clear the buffer
			friendBuffer.clear();
			// Copy
			friendBuffer = new ArrayList<Friend>(targetList);

			// Now, get the location of this friend in the new buffer
			int newIteratorLocation = friendBuffer
					.indexOf(playerTemoraryBuffer);

			// If it's found, set this as the new iterator value
			if (newIteratorLocation != -1) {
				currentI = newIteratorLocation;
			}
		} catch (Exception e) {
			// This probably means the list was empty

			// Clear the buffer
			friendBuffer.clear();
			// Copy
			friendBuffer = new ArrayList<Friend>(targetList);
		}
	}

	// This renders a username at a certain location
	public void render(Minecraft minecraft, double d, String username) {
		// Define the extra dashes to make the strings later more readable
		String extraDashes = "-------------";

		// Depending upon the username, we'll need to calculate and add
		// different amounts of spaces before it to centre it
		String prependToUsername = "";

		// Loop through and add the spaces
		for (int i = 0; i < (16 - username.length()) / 2; i++) {
			prependToUsername = prependToUsername + " ";
		}

		// Draw the top border
		minecraft.fontRenderer.drawString(EnumChatFormatting.GOLD + "-----"
				+ extraDashes + EnumChatFormatting.RESET, 0,
				(int) Math.floor(d - 17), 0xffffffff);

		// Draw the username and spaces
		minecraft.fontRenderer.drawStringWithShadow(EnumChatFormatting.GOLD
				+ prependToUsername + username + EnumChatFormatting.RESET, 27,
				(int) Math.floor(d - (minecraft.fontRenderer.FONT_HEIGHT / 2)),
				0xffffffff);

		// Draw a bottom border
		minecraft.fontRenderer.drawString(EnumChatFormatting.GOLD + "-----"
				+ extraDashes + EnumChatFormatting.RESET, 0,
				(int) Math.floor(d + 1 + (minecraft.fontRenderer.FONT_HEIGHT)),
				0xffffffff);
	}

	// Looping through the friends more than once can be an issue, so we store
	// this Boolean to determine whether we're already looping
	public static Boolean startedList = false;

	// This integer carries the value of the user who is currently emphasised,
	// with their username showing
	public static int currentI = 0;

	// Various aspects have to be reset, so this method deals with resetting
	// them all
	public static void resetStatus() {
		// Reset the list position
		startedList = false;

		// Reset the current user
		currentI = 0;

		// Clear the friends listed
		OnlineListManager.friends.clear();

		// Send a relist request if it's enabled
		if (FriendOnlineHud.hudEnabled) {
			OnlineListManager.sendBackgroundRequest();
		} else {
			// Otherwise, clear friends
			friendBuffer.clear();
		}
	}

	/*
	 * This listener handles for rendering the overlay and whether or not to
	 * draw the heads
	 */
	@SubscribeEvent
	public void onRenderOfOverlay(RenderGameOverlayEvent event) {
		// Check the event is not cancellable
		if (event.isCancelable()) {
			return;
		}

		// Check it's not for experience
		if (event.type != ElementType.EXPERIENCE) {
			return;
		}

		// Check if hud enabled
		if (!hudEnabled) {
			return;
		}

		// First let's check we can draw them by checking the images are loaded

		// Default is that they're ready to draw
		Boolean ready = true;

		// If there are no friends, we can't draw them
		if (friendBuffer.size() == 0) {
			ready = false;
		}

		// Now loop through friends
		Iterator<Friend> itr = friendBuffer.iterator();
		while (itr.hasNext()) {
			// Check if their texture has downloaded
			if (!itr.next().isTextureLoaded()) {
				// If their texture isn't loaded, then change the value of ready
				// to false
				ready = false;
			}
		}

		// Calculate the drawing start point
		double startDouble = 4 * 12.5;
		int start = (int) Math.round(startDouble);

		// Now check if we're ready to render
		if (ready) {
			// Check if we've already started the list
			if (!startedList) {
				// If we haven't started friend listing, start the timer to
				// increment the position
				FriendOnlineHud.startTimer();
			}

			// Indicate that we've now started the list
			startedList = true;

			// Set the increment to 0
			int i = 0;
			// Loop through all friends
			Iterator<Friend> friendIterator = friendBuffer.iterator();
			// Keep going while there are more
			while (friendIterator.hasNext()) {
				// Get the next iteration
				Friend friend = friendIterator.next();
				// Check the position of the friend
				if (i == currentI) {
					// If the value of the user's position is equal to the one
					// we need to emphasise then draw them
					friend.drawHead(0, 1 * 25 + start, 26, 26);
					// Also draw their username
					render(mc, 1 * 25 + start + 13,
							friend.getFormattedUsername());
				} else if (i == currentI + 1) {
					// If the value of the user's position is above to the one
					// we need to emphasise by 1 then draw them smaller and
					// above
					friend.drawHead(0, 1 * 25 + start + 36, 10, 10);
				} else if (i == currentI - 1) {
					// If the value of the user's position is 1 below the one we
					// need to emphasise draw them smaller below
					friend.drawHead(0, 1 * 25 + start - 20, 10, 10);
				}

				// Increment the position
				i++;
			}

		}
	}

	// This handles the starting of the increment timer
	public static void startTimer() {
		// Print a debug message
		System.out.println("Friend hud increment");

		// Register and schedule the timertask
		incrementTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// Check whether the friend list has started
				if (FriendOnlineHud.startedList) {
					if (!hudEnabled) {
						// Cancel this as it is now not needed
						this.cancel();
						return;
					}

					// If list has started, increment the current position
					FriendOnlineHud.currentI = FriendOnlineHud.currentI + 1;

					// If we reach the last player, reset status
					if (currentI >= friendBuffer.size()) {
						// Reset status
						FriendOnlineHud.resetStatus();
					}
				} else {
					// If friend list isn't in progress, cancel this timer as it
					// is no longer needed
					this.cancel();
				}
			}

		}, 10 * 1000, 10 * 1000);
	}

}
