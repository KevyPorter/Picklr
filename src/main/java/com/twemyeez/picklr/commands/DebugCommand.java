package com.twemyeez.picklr.commands;

import java.util.ArrayList;
import java.util.List;

import com.twemyeez.picklr.auth.SessionAuth;
import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;
import com.twemyeez.picklr.radio.RadioUtils;
import com.twemyeez.picklr.utils.CommonUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class DebugCommand implements ICommand{

	/*
	 * This class handles a /picklr command which initially will serve as a command to dump various data, including whether we were
	 * able to verify the session token, and the friends detected.
	 * 
	 * Once the modification is debugged, the intention is to rework this command to offer a setup tutorial.
	 */
	
	  //This list handles the command aliases
	  private static List aliases;
	  
	  //The constructor for this method adds the command aliases to the list
	  public DebugCommand()
	  {
	    this.aliases = new ArrayList();
	    this.aliases.add("picklrinfo");
	    this.aliases.add("picklr");
	  }

	  //Return the command name
	  @Override
	  public String getCommandName()
	  {
	    return "picklr";
	  }

	  //Currently takes no arguments, so the usage is simple
	  @Override
	  public String getCommandUsage(ICommandSender icommandsender)
	  {
	    return "picklr";
	  }

	  //Return the aliases if requested
	  @Override
	  public List getCommandAliases()
	  {
	    return this.aliases;
	  }

	  //Deal with the command processing
	  @Override
	  public void processCommand(ICommandSender icommandsender, String[] astring)
	  {
		  //Verify their session
		  CommonUtils.sendFormattedChat(true, SessionAuth.checkTokenValidity(SessionAuth.getToken()), EnumChatFormatting.RED, true);
		  
		  //Now work on displaying saved friends
		  CommonUtils.sendFormattedChat(true, "Detected the following friends:", EnumChatFormatting.RED, true);
		  //Loop through friends
		  for(Friend friend: OnlineListManager.friends)
		  {
			  //Display the friend and the status
			  CommonUtils.sendFormattedChat(true, friend.getName()+" is in a "+friend.getStatus(), EnumChatFormatting.BLUE, false);
		  }
	  }

	//We are unlikely to have to sort this class, so we'll leave the auto-generated method stub
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	//There is no limitation to who can use this, therefore we will always return true
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	//This takes no arguments, so there is no requirement for tab completion options.
	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,	String[] p_71516_2_) {
		return null;
	}

	//This command takes no arguments, so there will be no username index.
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}
}
