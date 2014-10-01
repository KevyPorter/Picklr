package com.twemyeez.picklr.auth;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CommonAPI {
	/*
	 * This class handles common API read and write methods
	 */
	
	//This is used to write to the API, and it does not return a success status for simplicity
	public static void carryOutAsyncApiWrite(final String URL){
		/*
		 * Create the thread
		 */
		Thread thread = new Thread(new Runnable(){
			public void run(){
				try
				{
					//Create the URL
					  URL target = new URL(URL);
					  //Open the input stream and read it
				      BufferedReader input = new BufferedReader(new InputStreamReader(target.openStream()));

				      //For debugging purposes, write the stream to console
				      String lineInput;
				      while ((lineInput = input.readLine()) != null)
				      {
				          System.out.println(lineInput);
				      }
				      
				      //Chose the input stream
				      input.close();
				      
				      //Print the URL too for debugging
					System.out.println(URL);
				}
				catch (Exception e)
				{
					//If there was an exception, print the exception stack trace
					e.printStackTrace();
				}
			}
		});
		//Run the thread
		thread.start();
		
	}
}
