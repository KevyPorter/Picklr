package com.twemyeez.picklr.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.twemyeez.picklr.listener.ChatListener;
import com.twemyeez.picklr.listener.ChatListener.ChatStatus;
import com.twemyeez.picklr.utils.CommonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class BulkFriend implements ICommand {
	static String command = "";
	static List<String> friendRequests = new ArrayList<String>();

	/*
	 * This parses chat events to find friends
	 */
	public static void relatedChatEventHandler(ClientChatReceivedEvent event) {
		// Get the unformatted message
		String message = event.message.getUnformattedText();

		// See if it's the friend request list
		if (message.startsWith("Pending friend requests:")) {
			// If it is, remove the start
			message = message.replace("Pending friend requests:", "");

			// Hide the message
			event.setCanceled(true);

			// Print it to console as a debugging method
			System.out.println("Hidden: " + message);

			// Go through each user
			for (String user : message.trim().split(", ")) {
				// Remove the comma
				user = user.replace(",", "");
				// If there is a user, then add them to the list
				if (user != "") {
					friendRequests.add(user);
				}
			}
		} else if (message
				.equals("Accept a friend request using /friend accept player")) {
			// Check if it's the message that comes before friend requests

			// Hide the message
			event.setCanceled(true);
			// Print it to console as a debugging method
			System.out.println("Hidden: " + message);
		}
	}

	/*
	 * This handles the main command processing
	 */
	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] args) {

		// Define the message prefix
		final String prefix = EnumChatFormatting.GRAY + "["
				+ EnumChatFormatting.DARK_GREEN + "Friends"
				+ EnumChatFormatting.GRAY + "] ";

		// Check if the command is already in process
		if (ChatListener.currentStatus
				.contains(ChatStatus.FRIEND_REQUEST_PROCESSING)) {
			return;
		}

		// Check that there are enough arguments
		if (args.length != 1) {
			// Tell the user they have misused the command
			CommonUtils.sendFormattedChat(true, prefix
					+ "Please do /bulkfriend accept or deny",
					EnumChatFormatting.BLUE, true);
			return;
		}

		// Check whether they want to accept or deny
		if (args[0].equals("accept")) {
			// Set the command to be run as accept
			command = "/f accept ";
		} else if (args[0].equals("deny")) {
			// Set the command to be run as deny
			command = "/f deny ";
		} else {
			// Tell the user they have misused the command
			CommonUtils.sendFormattedChat(true, prefix
					+ "Please do /bulkfriend accept or deny",
					EnumChatFormatting.BLUE, true);
			return;
		}

		// Now we know we're going to start processing, so add the ChatStatus
		ChatListener.currentStatus.add(ChatStatus.FRIEND_REQUEST_PROCESSING);

		// Signal to server to send friend requests
		Minecraft.getMinecraft().thePlayer.sendChatMessage("/f requests");

		// Register the timer task
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// Count false positives (null values added to friend requests)
				int falsePositive = 0;

				// Go through all the friends
				for (String user : friendRequests) {
					// Check there is a username
					if (!user.trim().equals("")) {
						// Run the command for that username
						Minecraft.getMinecraft().thePlayer
								.sendChatMessage(command + user);
					} else {
						// Otherwise increment false positive count
						falsePositive++;
					}
				}

				// Processing has now finished, so remove the status
				ChatListener.currentStatus
						.remove(ChatStatus.FRIEND_REQUEST_PROCESSING);

				// Tell the user there has been success
				CommonUtils.sendFormattedChat(
						true,
						prefix
								+ EnumChatFormatting.BLUE
								+ "Processed all "
								+ EnumChatFormatting.YELLOW
								+ String.valueOf(friendRequests.size()
										- falsePositive)
								+ EnumChatFormatting.BLUE + " requests!",
						EnumChatFormatting.BLUE, true);

				// Now clear the friend requests
				friendRequests.clear();
			}

		}, 1500);
	}

	// We are unlikely to have to sort this class, so we'll leave the
	// auto-generated method stub
	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	// Return the command name
	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "bulkfriend";
	}

	// Return the usage - it takes only 1 argument which is either accept or
	// deny
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "bulkfriend accept|deny";
	}

	// There are no alisases, so simply return null for this
	@Override
	public List getCommandAliases() {
		return null;
	}

	// There is no limitation to who can use this, therefore we will always
	// return true
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	// Return the tab completion options, either deny or accept
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		List<String> tabOptions = new ArrayList<String>();
		tabOptions.add("deny");
		tabOptions.add("accept");
		return tabOptions;
	}

	// There are no username arguments so always return false for this
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
