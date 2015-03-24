package com.twemyeez.picklr.commands;

import java.util.ArrayList;
import java.util.List;

import com.twemyeez.picklr.radio.RadioUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class RadioToggle implements ICommand {

	/*
	 * This command is used to toggle on and off the Radio function of the mod
	 */

	// Return the command name
	@Override
	public String getCommandName() {
		return "radio";
	}

	// Return the usage - it takes no arguments at all
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "radio";
	}

	// This command has no aliases, so we will return null
	@Override
	public List getCommandAliases() {
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("hpr");
		aliases.add("rad");
		aliases.add("radio");
		return aliases;
	}

	// Command processing
	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		// Toggle the radio playing
		RadioUtils.toggleRadio();
	}

	// There is no limitation to who can use this, therefore we will always
	// return true
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	// This takes no arguments, so there is no requirement for tab completion
	// options.
	public List addTabCompletionOptions(ICommandSender sender,
			String[] existing) {
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

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,
			BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

}
