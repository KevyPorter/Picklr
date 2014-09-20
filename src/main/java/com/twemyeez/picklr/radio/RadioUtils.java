package com.twemyeez.picklr.radio;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.EnumChatFormatting;

import com.twemyeez.picklr.utils.CommonUtils;

public class RadioUtils {
	
	public static Clip clip;
	public static FloatControl gainControl;
	public static Thread thread;
	public static Boolean inProgress = false;
	
	/*
	 * This takes a decoded buffered audio input stream and format, and plays it
	 */
	public static void rawplay(AudioFormat targetFormat, AudioInputStream decodedInput) throws IOException,                                                                                                LineUnavailableException
	{
	  byte[] data = new byte[4096];
	  
	  //Create the source data line
	  SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, targetFormat));
	  //Open the line with the specified format
	  sourceDataLine.open(targetFormat);

	  //If the source data line has been successfully established
	  if (sourceDataLine != null)
	  {
		  // Start
		  sourceDataLine.start();
		  
		  //Set the data remaining indicator to 0
		  int bytesRead = 0;
	    
		  //This is used to control the volume/master gain
		  gainControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
		  
		  //Set the gain to the saved default
		  gainControl.setValue(-1.0F);
		  
		  //while there is new data still remaining to read
		  while (bytesRead != -1)
	      {
			  //read the input data stream
			  bytesRead = decodedInput.read(data, 0, data.length);
			  
			  //assuming there is new data, write it out to the data line
			  if (bytesRead != -1)
			  {
				  sourceDataLine.write(data, 0, bytesRead);
			  }
	      }
	    
		  //Stop playing
		  sourceDataLine.drain();
		  sourceDataLine.stop();
	   
	    
		  //Close up resources to avoid leaks
		  sourceDataLine.close();
		  decodedInput.close();
	  } 
	}
	

	/*
	 * This starts the radio thread
	 */
	public static void startRadio(){
		//Do a sanity check by checking that the radio is not already playing.
		if(!inProgress)
		{
			//Tell the user that Radio is starting
			CommonUtils.sendFormattedChat(true, "HypixelRadio.net toggled on", EnumChatFormatting.GREEN, true);
			
			//Start a new Radio thread to do data retrieval and playing in the background
			thread = (new Thread(new HypixelRadioRunnable()));
			thread.start();
			//Alter the inProgress thread to indicate Radio is now playing
			inProgress = true;
		}
	}
	
	/*
	 * This is used to fade out radio and then stop the thread running
	 */
	public static void stopRadio()
	{
		//Before stopping radio, we need to make sure it's actually in progress
		if(inProgress)
		{
			//Tell the user that Radio is stopping
			CommonUtils.sendFormattedChat(true, "HypixelRadio.net toggled off", EnumChatFormatting.GOLD, true);
			
			//In another thread, we'll perform volume adjustments
			new Thread(new Runnable()
			{

				@Override
				public void run() {
					//the difference from -70F to -80F (the minimum volume) is virtually indistinguishable, hence we use -70
					while(gainControl.getValue() > -70F)
					{
						//lower the volume by a small increment
						gainControl.setValue(gainControl.getValue()-0.5f);
						
						//sleep the thread for a short while
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//the volume has now faded out, so we'll stop the thread
					thread.stop();
					//the thread has stopped, so let's reset the Boolean inProgress flag to false to indicate the radio can be started again
					inProgress = false;
				}
			
			}).start();
		}
	}
	
	/*
	 * This handles the simple decision between whether the radio is on or not, and acts accordingly, toggling the state
	 */
	public static void toggleRadio()
	{
		//If the radio is in progress, stop the radio
		if(inProgress)
		{
			stopRadio();
		}
		else
		{
			//The radio is not in progress - therefore start playing it.
			startRadio();
		}
	}

}
