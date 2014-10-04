package com.twemyeez.picklr.commands;

import java.util.List;

import com.twemyeez.picklr.hud.FriendOnlineHud;
import com.twemyeez.picklr.radio.RadioUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class HudToggle implements ICommand {
	/*
	 * This command is used to toggle on and off the Friend HUD function of the
	 * mod
	 */

	// Return the command name
	@Override
	public String getCommandName() {
		return "hud";
	}

	// Return the usage - it takes no arguments at all
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "hud";
	}

	// This command has no aliases, so we will return null
	@Override
	public List getCommandAliases() {
		return null;
	}

	// Command processing
	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		// Toggle the HUD
		FriendOnlineHud.toggleHud();
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

	// We are unlikely to have to sort this class, so we'll leave the
	// auto-generated method stub
	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

}
