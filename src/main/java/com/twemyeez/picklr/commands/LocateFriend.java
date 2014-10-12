package com.twemyeez.picklr.commands;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.twemyeez.picklr.auth.CommonAPI;
import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;
import com.twemyeez.picklr.utils.CommonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class LocateFriend implements ICommand {

	/*
	 * This class provides the /picklrfind command which is used to find a
	 * player and to return their AFK status
	 */

	// Define the message prefix
	final String prefix = EnumChatFormatting.GRAY + "["
			+ EnumChatFormatting.GREEN + "Locator" + EnumChatFormatting.GRAY
			+ "] ";

	/*
	 * This actually handles the API requests in a new thread
	 */
	public void startNewThreadForUsername(final String username) {
		// Create the thread
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Construct the API url
				String url = "http://picklr.me/api/v1/locate.php?username="
						+ Minecraft.getMinecraft().thePlayer
								.getCommandSenderName()
						+ "&r_uuid="
						+ SessionAuth.getToken() + "&target=" + username;

				// Debug by printing the URL
				System.out.println(url);

				// Write the new location to the API in a new thread to avoid
				// lag
				String location = CommonAPI.carryOutAsyncApiRead(url);

				// Construct the API url
				url = "http://picklr.me/api/v1/afk.php?username="
						+ Minecraft.getMinecraft().thePlayer
								.getCommandSenderName() + "&r_uuid="
						+ SessionAuth.getToken() + "&target=" + username;

				String afk = CommonAPI.carryOutAsyncApiRead(url);

				// Debug by printing the URL
				System.out.println(url);

				if (location.equals("") && afk.equals("")) {
					CommonUtils.sendFormattedChat(true, prefix + "Sorry, "
							+ username + " was not found.",
							EnumChatFormatting.BLUE, true);
				} else {
					String append = "";
					// They aren't ""
					if (afk.equals("true")) {
						append = " and is AFK";
					}

					CommonUtils.sendFormattedChat(true, prefix + "The player "
							+ username + " is on " + location + append,
							EnumChatFormatting.BLUE, true);
				}
			}

		});

		thread.start();
	}

	// We probably won't have to compare this, so we'll simply return 0
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	// Return the name of the command
	@Override
	public String getCommandName() {
		return "picklrfind";
	}

	// Return the command usage
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "picklrfind username";
	}

	// There are no aliases, so return null
	@Override
	public List getCommandAliases() {
		return null;
	}

	// Deal with the main command processing
	@Override
	public void processCommand(final ICommandSender sender, final String[] args) {
		// Check is hypixel
		if (!CommonUtils.isHypixel()) {
			// Tell the user the command is only for Hypixel
			CommonUtils.sendFormattedChat(true, prefix
					+ "This can only be run on the Hypixel server.",
					EnumChatFormatting.BLUE, true);
			return;
		}

		// Get the arguments length
		if (args.length == 1) {
			// Check if they have any friends
			if (OnlineListManager.friends.size() == 0) {

				// If not, send a background request
				OnlineListManager.sendBackgroundRequest();

				// Store this instance to run the command again
				final ICommand locator = this;

				// And run this command again in a short while
				new Timer().schedule(new TimerTask() {
					public void run() {
						locator.processCommand(sender, args);
					}
				}, 8 * 1000);

			} else {
				// Otherwise, loop through their friends
				for (Friend friend : OnlineListManager.friends) {
					if (friend.getName().equals(args[0])) {
						// If they have a friend of the name they asked for,
						// send the request
						startNewThreadForUsername(args[0]);
						return;
					}
				}

				// Check if the user is themselves
				if (Minecraft.getMinecraft().thePlayer.getCommandSenderName()
						.equals(args[0])) {
					startNewThreadForUsername(args[0]);
					return;
				}

				// Otherwise, it's not one of their friends
				CommonUtils.sendFormattedChat(true, prefix + "Please add "
						+ args[0] + " as a friend before you can do this.",
						EnumChatFormatting.BLUE, true);
			}
		} else {
			// Tell the user the correct method
			CommonUtils.sendFormattedChat(true, prefix
					+ "Please run /picklrfind username",
					EnumChatFormatting.BLUE, true);
		}
	}

	// This can be used by anyone, so return true
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	// There are no tab completion options being added
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return null;
	}

	// The only argument is an username, so return true
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return true;
	}
}
