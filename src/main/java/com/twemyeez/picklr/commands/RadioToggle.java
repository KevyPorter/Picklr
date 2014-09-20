package com.twemyeez.picklr.commands;

import java.util.List;

import com.twemyeez.picklr.radio.RadioUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class RadioToggle implements ICommand{

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "radio";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "radio";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		RadioUtils.toggleRadio();
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
