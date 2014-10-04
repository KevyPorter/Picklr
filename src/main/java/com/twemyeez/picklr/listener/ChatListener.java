package com.twemyeez.picklr.listener;

import java.util.ArrayList;
import java.util.List;

import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.commands.BulkFriend;
import com.twemyeez.picklr.friends.OnlineListManager;
import com.twemyeez.picklr.location.ServerLocationUtils;
import com.twemyeez.picklr.utils.CommonUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatListener {

	/*
	 * This enum holds the various status's that can be used for chat
	 */
	public enum ChatStatus {
		READY, WHEREAMI, FRIEND_GETTING_PAGE, FRIEND_LISTING, AFK, BACKGROUND_FRIEND, TOKEN_REQUEST, FRIEND_REQUEST_PROCESSING
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

		// Check the message isn't null and that they're not on another server
		if (event.message == null || !CommonUtils.isHypixel()) {
			return;
		}

		/*
		 * We now check the active status(s) and then fire their relevant event
		 * handlers
		 */

		if (currentStatus.contains(ChatStatus.WHEREAMI)) {
			// Call the method for dealing with whereami messages in case this
			// is one
			ServerLocationUtils.relatedChatEventHandler(event);
		}

		if (OnlineListManager.isInProgress()) {
			// Call the method for dealing with friend lists
			OnlineListManager.relatedChatEventHandler(event);
		}

		if (currentStatus.contains(ChatStatus.TOKEN_REQUEST)) {
			// Call the method for dealing with token messages in case this is
			// one
			SessionAuth.relatedChatEventHandler(event);
		}

		if (currentStatus.contains(ChatStatus.FRIEND_REQUEST_PROCESSING)) {
			// Fire the method for friend request bulk processing if needed
			BulkFriend.relatedChatEventHandler(event);
		}
	}
}
