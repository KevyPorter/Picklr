package com.twemyeez.picklr.radio;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.spi.mpeg.sampled.file.tag.IcyInputStream;

public class HypixelRadioRunnable  implements Runnable{

	@Override
	public void run() {
		
		//declare streams
		IcyInputStream icyIs = null;
		InputStream inputRaw = null;
		InputStream bufferedIn = null;
		
		
		try
		{
			//this is the stream URL.
			URL url = new URL("http://knight.wavestreamer.com:1237/listen.mp3");
		
			//We now open a socket to that address and port
			Socket socketWithPort = new Socket(url.getHost(), url.getPort());
			OutputStream outputFromSocket = socketWithPort.getOutputStream();
			
			//Send a GET request for the data
			String req="GET / HTTP/1.0\r\nIcy-MetaData: 1\r\nConnection: keep-alive\r\n\r\n";
			outputFromSocket.write(req.getBytes());
			inputRaw=socketWithPort.getInputStream();
			
			//Sleep to fill data buffer
			Thread.sleep(500);
			
			//open a buffered input stream
			bufferedIn = new BufferedInputStream(inputRaw, 81920);
			
			//Apply ICY metadata cleansing to the stream
			icyIs = new IcyInputStream(bufferedIn);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//if the HTTP request has failed, it'd be best to stop, hence we'll return.
			return;
		}
		
		System.out.println("Stage 1");
		
		
	    AudioInputStream inputAudio = null;
	    
		try {
			inputAudio = AudioSystem.getAudioInputStream(icyIs);
		} catch (Exception e)
		{
			
		}
	    AudioInputStream decodedAudioInput = null;
	    AudioFormat baseFormat = inputAudio.getFormat();
	    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
	                                                                                  baseFormat.getSampleRate(),
	                                                                                  16,
	                                                                                  baseFormat.getChannels(),
	                                                                                  baseFormat.getChannels() * 2,
	                                                                                  baseFormat.getSampleRate(),
	                                                                                  false);
	    decodedAudioInput = AudioSystem.getAudioInputStream(decodedFormat, inputAudio);
	    System.out.println("Stage 2");
	    // Play now. 
	    try {
			RadioUtils.rawplay(decodedFormat, decodedAudioInput);
	    } catch (Exception e) {
	    	System.out.println("ERR 3");
		}
	    System.out.println("Stage 3");
	    try {
	    	inputAudio.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
}
