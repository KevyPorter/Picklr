package com.twemyeez.picklr.commands;

import java.util.ArrayList;
import java.util.List;

import com.twemyeez.picklr.Picklr;
import com.twemyeez.picklr.radio.RadioUtils;
import com.twemyeez.picklr.utils.CommonUtils;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

public class RadioVolume implements ICommand {

	/*
	 * This command handles /volume which takes a percentage, and adjusts the
	 * volume of the Hypixel Radio playing
	 */

	// Return the command name
	@Override
	public String getCommandName() {
		return "volume";
	}

	// Return example usage
	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "volume [0-100]";
	}

	// Construct and return a list of the aliases for this command
	@Override
	public List getCommandAliases() {
		// Construct list
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("volume");
		aliases.add("vol");
		aliases.add("radiovolume");

		// Return the list
		return aliases;
	}

	/*
	 * This method handles the commands processing
	 */

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		// first, we must check if they've got the correct number of arguments
		if (args.length != 1) {
			CommonUtils.sendFormattedChat(true,
					"Volume adjustment is done via /volume [percentage]",
					EnumChatFormatting.RED, true);
		} else {
			// this means they have 1 argument
			try {
				// attempt to convert it to an integer
				Integer volumePercent = Integer.valueOf(args[0]);
				// check if radio is playing
				if (!RadioUtils.inProgress) {
					// the radio isn't in progress, so let's warn them.
					CommonUtils
							.sendFormattedChat(
									true,
									"Volume adjustment requires Hypixel Radio to be playing!",
									EnumChatFormatting.RED, true);
				} else {
					// from -80 to 0, so it's 4/5ths.
					float newVolume = -1 * (100 - volumePercent) * 0.4F;

					// if it's smaller than -80 or greater than 0, tell the user
					// they're wrong
					if (newVolume < -80F || newVolume > 0F) {
						CommonUtils
								.sendFormattedChat(
										true,
										"Your volume percentage must be between 0 and 100",
										EnumChatFormatting.GOLD, true);
					} else {
						// Set the new value
						RadioUtils.gainControl.setValue(-1
								* (100 - volumePercent) * 0.8F);
						// tell the player the volume has been set
						CommonUtils.sendFormattedChat(true, "Volume set to "
								+ volumePercent + "%", EnumChatFormatting.GOLD,
								true);
					}
				}

			} catch (Exception e) {
				// If there has been an exception, it's probably in the integer
				// parsing, so we'll return a syntax hint message
				e.printStackTrace();
				CommonUtils
						.sendFormattedChat(
								true,
								"Radio volume adjustment is done via /volume [percentage]",
								EnumChatFormatting.RED, true);
			}
		}
	}

	// There are no required limitations on who can use the command, so we'll
	// simply return true
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	// This takes a percentage, so tab complete really won't be that useful, so
	// simply return null
	public List addTabCompletionOptions(ICommandSender sender,
			String[] existing) {
		return null;
	}

	// A username is never one of the arguments
	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

	// We are unlikely to ever have to compare this, so we'll just return 0
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
