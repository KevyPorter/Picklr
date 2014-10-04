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

public class HypixelRadioRunnable implements Runnable {

	@Override
	public void run() {

		// declare streams
		IcyInputStream icyIs = null;
		InputStream inputRaw = null;
		InputStream bufferedIn = null;

		try {
			// this is the stream URL.
			URL url = new URL("http://knight.wavestreamer.com:1237/listen.mp3");

			// We now open a socket to that address and port
			Socket socketWithPort = new Socket(url.getHost(), url.getPort());
			OutputStream outputFromSocket = socketWithPort.getOutputStream();

			// Send a GET request for the data
			String req = "GET / HTTP/1.0\r\nIcy-MetaData: 1\r\nConnection: keep-alive\r\n\r\n";
			outputFromSocket.write(req.getBytes());
			inputRaw = socketWithPort.getInputStream();

			// Sleep to fill data buffer
			Thread.sleep(500);

			// open a buffered input stream
			bufferedIn = new BufferedInputStream(inputRaw, 81920);

			// Apply ICY metadata cleansing to the stream
			icyIs = new IcyInputStream(bufferedIn);

			// Add the tag parser for allowing song name fetching
			icyIs.addTagParseListener(new RadioParseTagListener());

		} catch (Exception e) {
			// Print a stack trace for debugging
			e.printStackTrace();
			// if the HTTP request has failed, it'd be best to stop, hence we'll
			// return.
			return;
		}

		// Now, initialise the audio input stream
		AudioInputStream inputAudio = null;

		// Try to get the audio input stream from the ICY input stream
		try {
			inputAudio = AudioSystem.getAudioInputStream(icyIs);
		} catch (Exception e) {
			// Print a stack trace for debugging
			e.printStackTrace();
			// if this has failed, it'd be best to stop, hence we'll return.
			return;
		}

		// Now deal with the audio decoding
		AudioInputStream decodedAudioInputStream = null;

		// Define the base audio format
		AudioFormat inputFormat = inputAudio.getFormat();

		// And define a decode format
		AudioFormat decodedAudioFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(),
				16, inputFormat.getChannels(), inputFormat.getChannels() * 2,
				inputFormat.getSampleRate(), false);

		// Decode the base audio
		decodedAudioInputStream = AudioSystem.getAudioInputStream(
				decodedAudioFormat, inputAudio);

		// Attempt to play the decoded audio input stream
		try {
			RadioUtils.rawplay(decodedAudioFormat, decodedAudioInputStream);
		} catch (Exception e) {
			// If there was an exception in playing, print the stack trace
			e.printStackTrace();
		}

		// Now attempt to close the base audio input
		try {
			inputAudio.close();
		} catch (Exception e) {
			// If an exception occured, print a stack trace
			e.printStackTrace();
		}

	}
}
