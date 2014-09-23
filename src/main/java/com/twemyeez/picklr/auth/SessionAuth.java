package com.twemyeez.picklr.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import net.minecraft.client.Minecraft;

public class SessionAuth {
	/*
	 * This class is a work in progress and is not currently used anywhere. The aim of this class is going to be to handle
	 * authentication with the Picklr API when it is released, because the PickledChat API was somewhat flawed in this respect.
	 */
	
	/*
	 * This method returns the Minecraft session token
	 */
	public static String getToken()
	{
		return Minecraft.getMinecraft().getSession().getToken();
	}
	
	/*
	 * This is a debug method to check that we can validate the string returned from the getToken() method. It returns the token if the
	 * request was not successful, but it will return "Session token verified" if it was successful.
	 */
	public static String checkTokenValidity(String token)
	{
		/* https://authserver.mojang.com/validate
		 * 
		 * with JSON payload 	{
		 *					"accessToken": "token",
		 *  			}
		 */
	
		/*
		 * We use a try...catch structure to easily handle the various ways in which this can fail
		 */
	    try {
	    	//Define the URL to which we will request
	    	URL url = new URL("https://authserver.mojang.com/validate");

	    	//Define the data payload
	        String payload="{\"accessToken\":\""+token+"\"}";
	        
	        //Open a HTTP connection to the URL
	    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	    	//Configure the connection
	    	connection.setDoOutput(true);
	    	connection.setDoInput(true);
	        
	        //Set the request properties to allow the data to be passed
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	         
	        //Now create the output writer
	        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	         
	        //Write the payload
	        writer.write(payload);
	         
	        //Close the writer to clean up resources
	        writer.close();
	      
	        //Now read the response to a string
	        String output = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
	        
	        //A null output implies that the request was sucessful
	        if(output == null)
	        {
	        	return "Session token verified";
	        }
	        else
	        {
	        	//Otherwise, just return the token to allow easier debugging
	        	return output;
	        }
	      
	  
	    }
	    catch (IOException e)
	    {
	    	//Print the stack trace for any exceptions and return a short explanatory string
	    	e.printStackTrace();
			return "An exception occured";
	    }

	    
	}
}
