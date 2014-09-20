package com.twemyeez.picklr.commands;

import java.util.ArrayList;
import java.util.List;

import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.radio.RadioUtils;
import com.twemyeez.picklr.utils.CommonUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class DebugCommand implements ICommand{

	  private static List aliases;
	  
	  public DebugCommand()
	  {
	    this.aliases = new ArrayList();
	    this.aliases.add("picklrinfo");
	    this.aliases.add("picklr");
	  }

	  @Override
	  public String getCommandName()
	  {
	    return "picklr";
	  }

	  @Override
	  public String getCommandUsage(ICommandSender icommandsender)
	  {
	    return "picklr";
	  }

	  @Override
	  public List getCommandAliases()
	  {
	    return this.aliases;
	  }

	  @Override
	  public void processCommand(ICommandSender icommandsender, String[] astring)
	  {
		  CommonUtils.sendFormattedChat(true, SessionAuth.checkTokenValidity(SessionAuth.getToken()), EnumChatFormatting.RED, true);
	  }

	  
	  
	  /*
	   * Unrequired methods
	   */
	  
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,	String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}
}
