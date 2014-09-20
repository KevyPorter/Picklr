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
	
	public static String getToken()
	{
		return Minecraft.getMinecraft().getSession().getToken();
	}
	
	public static String checkTokenValidity(String token)
	{
		/* https://authserver.mojang.com/validate
		 * with format 	{
		 *					"accessToken": "valid accessToken",
		 *  			}
		 */
	
	    try {
	    	URL url = new URL("https://authserver.mojang.com/validate");

	        String payload="{\"accessToken\":\""+token+"\"}";
	        
	    	 HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	    	 connection.setDoInput(true);
	         connection.setDoOutput(true);
	         
	         connection.setRequestMethod("POST");
	         connection.setRequestProperty("Accept", "application/json");
	         
	         connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	         
	         OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	         
	         writer.write(payload);
	         
	         writer.close();
	      
	     
	         BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	         String output =  br.readLine();
	         if(output == null)
	         {
	        	 return "Session token verified";
	         }
	         else
	         {
	        	 return output;
	         }
	      
	  
	    } catch (IOException e) {
	      e.printStackTrace();
	      
	    }
		return token;
	    
	}
}
