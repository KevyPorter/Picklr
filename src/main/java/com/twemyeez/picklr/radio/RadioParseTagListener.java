package com.twemyeez.picklr.radio;

import net.minecraft.util.EnumChatFormatting;

import com.twemyeez.picklr.Picklr;
import com.twemyeez.picklr.forums.ForumProcessor;
import com.twemyeez.picklr.utils.CommonUtils;

import javazoom.spi.mpeg.sampled.file.tag.TagParseEvent;
import javazoom.spi.mpeg.sampled.file.tag.TagParseListener;

public class RadioParseTagListener implements TagParseListener {

	/*
	 * Listen for song changes
	 */
	@Override
	public void tagParsed(TagParseEvent event) {
		// Print a debug message
		System.out.println("Tag parsed for radio " + event.getTag().getName());

		//Check it is the song change
		if (!event.getTag().getName().equals("StreamTitle")) {
			return;
		}
		
		// Define the message prefix
		String prefix = EnumChatFormatting.GRAY + "["
				+ EnumChatFormatting.DARK_RED + "Radio"
				+ EnumChatFormatting.GRAY + "] ";

		// Send the user an alert saying the song has changed
		CommonUtils.sendFormattedChat(true, prefix + "Now playing '"
				+ event.getTag().getValue() + "'", EnumChatFormatting.GOLD,
				true);

		// Save the current song
		RadioUtils.currentSong = (String) event.getTag().getValue();
	}

}
