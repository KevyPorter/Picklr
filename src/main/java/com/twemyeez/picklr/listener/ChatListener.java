package com.twemyeez.picklr.listener;

import java.util.ArrayList;
import java.util.List;

import com.twemyeez.picklr.afk.AFKHandler;
import com.twemyeez.picklr.commands.BulkFriend;
import com.twemyeez.picklr.location.ServerLocationUtils;
import com.twemyeez.picklr.utils.CommonUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatListener {

	/*
	 * This enum holds the various status's that can be used for chat
	 */
	public enum ChatStatus {
		READY, WHEREAMI, FRIEND_GETTING_PAGE, FRIEND_LISTING, BACKGROUND_FRIEND, TOKEN_REQUEST, FRIEND_REQUEST_PROCESSING, PARTY_INVITE
	};

	/*
	 * There can be multiple status's active at once, so we'll store these in a
	 * list
	 */
	public static List<ChatStatus> currentStatus = new ArrayList<ChatStatus>();

	/*
	 * This is the event fired when chat is received, and it's what fires the
	 * relevant methods based upon the active chat status's.
	 */
	@SubscribeEvent
	public void ClientChatReceivedEvent(ClientChatReceivedEvent event) {
		// We cannot risk crashing, so use a try...catch
		try {
			// Check the message isn't null and that they're not on another
			// server
			if (event.message == null || !CommonUtils.isHypixel()) {
				return;
			}

			/*
			 * We now check the active status(s) and then fire their relevant
			 * event handlers
			 */

			if (currentStatus.contains(ChatStatus.WHEREAMI)) {
				// Call the method for dealing with whereami messages in case
				// this
				// is one
				ServerLocationUtils.relatedChatEventHandler(event);
			}

	
			if (currentStatus.contains(ChatStatus.FRIEND_REQUEST_PROCESSING)) {
				// Fire the method for friend request bulk processing if needed
				BulkFriend.relatedChatEventHandler(event);
			}

			if (currentStatus.contains(ChatStatus.PARTY_INVITE)) {
				// It might be a party invite so process it
				PartyInvite.checkForPartyInvite(event);
			}

			// See if they're AFK
			AFKHandler.handleChat(event);

			// Now we'll monitor to see if it's a join message
			handleJoinEvent(event);

		
		} catch (Exception e) {
			e.printStackTrace();
			CommonUtils.sendFormattedChat(true,
					"An error occured. Please report this.",
					EnumChatFormatting.DARK_RED, false);
		}
	}

	public static void handleJoinEvent(ClientChatReceivedEvent event) {
		// Get the message
		String message = event.message.getUnformattedText();

		if (message.contains(":")) {
			// It is chat, so return
			return;
		}

		// Otherwise, we'll check it's the right length and contains "joined."
		if (message.split(" ").length == 2
				&& message.split(" ")[1].equals("joined.")) {
			// We know it's a join message so cancel the event
			event.setCanceled(true);

			// Get the username
			String nameJoined = message.split(" ")[0];

		
			// Create the chat style with click event
			ChatStyle chatStyle = new ChatStyle()
					.setChatClickEvent(new ClickEvent(
							ClickEvent.Action.SUGGEST_COMMAND, "/tell "
									+ nameJoined + " hi"));

			// Add the hover message to the chat style
			chatStyle = chatStyle.setChatHoverEvent(new HoverEvent(
					HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
							EnumChatFormatting.BLUE + "Click to "
									+ EnumChatFormatting.YELLOW + "message")));

			// Send the player a message with prominent bars to show them
			CommonUtils.sendFormattedChatWithPrefix(new ChatComponentText(
					event.message.getFormattedText()).setChatStyle(chatStyle),
					true);

		}
	}
}
