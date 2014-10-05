package com.twemyeez.picklr.commands;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.forums.ForumProcessor;
import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;
import com.twemyeez.picklr.radio.RadioUtils;
import com.twemyeez.picklr.utils.CommonUtils;
import com.twemyeez.picklr.utils.KeyBindings;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class DebugCommand implements ICommand {

	/*
	 * This class handles a /picklr command which initially will serve as a
	 * command to dump various data, including whether we were able to verify
	 * the session token, and the friends detected.
	 * 
	 * Once the modification is debugged, the intention is to rework this
	 * command to offer a setup tutorial.
	 */

	// This list handles the command aliases
	private static List aliases;

	// The constructor for this method adds the command aliases to the list
	public DebugCommand() {
		this.aliases = new ArrayList();
		this.aliases.add("picklrinfo");
		this.aliases.add("picklr");
	}

	// Return the command name
	@Override
	public String getCommandName() {
		return "picklr";
	}

	// Currently takes no arguments, so the usage is simple
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "picklr";
	}

	// Return the aliases if requested
	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	// Deal with the command processing
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		new Thread(introduction).start();
	}

	// We are unlikely to have to sort this class, so we'll leave the
	// auto-generated method stub
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	// There is no limitation to who can use this, therefore we will always
	// return true
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	// This takes no arguments, so there is no requirement for tab completion
	// options.
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return null;
	}

	// This command takes no arguments, so there will be no username index.
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	public static Runnable introduction = new Runnable() {

		@Override
		public void run() {
			try {
				// Do an initial delay
				Thread.sleep(2000);
				// Tell them the token
				CommonUtils.sendFormattedChat(true,
						"Thank-you for using Picklr", EnumChatFormatting.BLUE,
						true);
				// Sleeps are used to spread the message over time, to make it
				// less overwhelming
				Thread.sleep(2000);
				// Tell them the token
				CommonUtils
						.sendFormattedChat(
								true,
								"Press "
										+ EnumChatFormatting.BLUE
										+ Keyboard
												.getKeyName(KeyBindings.friendList
														.getKeyCode())
										+ " to list your "
										+ EnumChatFormatting.BLUE
										+ "online "
										+ EnumChatFormatting.BLUE
										+ "friends. If you want to see their "
										+ EnumChatFormatting.BLUE
										+ "exact "
										+ EnumChatFormatting.BLUE
										+ "lobby, and whether they're AFK, do /picklrfind (username).",
								EnumChatFormatting.AQUA, false);
				Thread.sleep(6800);
				CommonUtils
						.sendFormattedChat(
								true,
								"You can play "
										+ EnumChatFormatting.BLUE
										+ "HypixelRadio by typing "
										+ EnumChatFormatting.BLUE
										+ "/radio and if you have it enabled, double press "
										+ EnumChatFormatting.BLUE
										+ Keyboard.getKeyName(KeyBindings.lobby
												.getKeyCode())
										+ " to lobby. Press "
										+ EnumChatFormatting.BLUE
										+ Keyboard
												.getKeyName(KeyBindings.toggleLocationHud
														.getKeyCode()) + " to "
										+ EnumChatFormatting.BLUE
										+ "display your "
										+ EnumChatFormatting.BLUE + "current "
										+ EnumChatFormatting.BLUE
										+ "server in the corner of the screen.",
								EnumChatFormatting.AQUA, false);
				Thread.sleep(7800);
				CommonUtils
						.sendFormattedChat(
								true,
								"If you want to "
										+ EnumChatFormatting.BLUE
										+ "accept or "
										+ EnumChatFormatting.BLUE
										+ "deny all "
										+ EnumChatFormatting.BLUE
										+ "friend "
										+ EnumChatFormatting.BLUE
										+ "requests, you can do /bulkfriend accept or /bulkfriend deny. Be careful, because this will not ask you to confirm your action.",
								EnumChatFormatting.AQUA, false);
				Thread.sleep(7300);
				CommonUtils
						.sendFormattedChat(
								true,
								"Press "
										+ EnumChatFormatting.BLUE
										+ Keyboard.getKeyName(KeyBindings.afk
												.getKeyCode())
										+ " to go "
										+ EnumChatFormatting.BLUE
										+ "AFK. When you return and press "
										+ EnumChatFormatting.BLUE
										+ Keyboard.getKeyName(KeyBindings.afk
												.getKeyCode())
										+ " again, any /tell's which you received will be shown to you.",
								EnumChatFormatting.AQUA, false);
				Thread.sleep(7300);
				CommonUtils.sendFormattedChat(
						true,
						"To automatically accept the first "
								+ EnumChatFormatting.BLUE
								+ "party "
								+ EnumChatFormatting.BLUE
								+ "invite you get, press "
								+ EnumChatFormatting.BLUE
								+ Keyboard.getKeyName(KeyBindings.partyMode
										.getKeyCode())
								+ EnumChatFormatting.AQUA
								+ ". Type /hud to get an online friend hud",
						EnumChatFormatting.AQUA, false);
				Thread.sleep(7500);
				CommonUtils
						.sendFormattedChat(
								true,
								"If you want to get "
										+ EnumChatFormatting.BLUE
										+ "forum "
										+ EnumChatFormatting.BLUE
										+ "notifications in game, add your forum username and password to the config.",
								EnumChatFormatting.AQUA, false);
				Thread.sleep(6800);
				CommonUtils.sendFormattedChat(true,
						"To see this again, type /picklr",
						EnumChatFormatting.AQUA, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};
}
