package com.twemyeez.picklr.forums;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.minecraft.util.EnumChatFormatting;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.twemyeez.picklr.Picklr;

public class ForumLogin {
	/*
	 * This class is used for all of the various attributes of the forum login process
	 * A successful login and alert/message request would go
	 * 		- Constructor
	 *  	- getMessageRequestString()
	 *  	- getUserMessage(Parsed Message Response)
	 */
	
	//This stores the token
	private String token = "error";
	
	//This is the cookie store used to preserve the logged in state
	private CookieStore loginCookieStore;
	//The HTTP context used to assign the cookie store the the HTTP cleint
	private HttpContext loginContext;
	
	/*
	 * This constructor takes two arguments, the forum username and the forum password
	 */
	public ForumLogin(String username, String password)
	{
		//Create the cookie store and context for the requests
		loginCookieStore = new BasicCookieStore();
		loginContext = new BasicHttpContext();
		loginContext.setAttribute(ClientContext.COOKIE_STORE, loginCookieStore);
		
		//Create a Http client
		HttpClient loginClient = new DefaultHttpClient();
		
		//Define the URL for the Post request for login
		HttpPost loginRequest = new HttpPost("http://hypixel.net/login/login");

		try {
			//Create a list of the parameters
			List<NameValuePair> getParameters = new ArrayList<NameValuePair>();
			getParameters.add(new BasicNameValuePair("login", username));
			getParameters.add(new BasicNameValuePair("password", password));
		      
			//Set them to be applied to the login request
			loginRequest.setEntity(new UrlEncodedFormEntity(getParameters));
			
		    //Now carry out the request, and get a buffered reader from the response  
		    BufferedReader bufferedReaderFromResponse = new BufferedReader(new InputStreamReader(loginClient.execute(loginRequest, loginContext).getEntity().getContent()));
		    
		    //This string holds the overall output
		    String output = "";
		    
		    //This string will be used for a temporary line buffer
		    String line;
		      
		    //Read the response and copy it to the "output" string
		    while((line = bufferedReaderFromResponse.readLine()) != null)
		    {
		    	output = output.concat(line);
		    }
		      
		    //Now we begin parsing the response for the xfToken
		    int location = output.indexOf("name=\"_xfToken\"");
			    
		    //Get a shorter substring of the token
		    output = output.substring(location, location+100);
		    output = output.replace("name=\"_xfToken\" value=\"", "");
		    output = output.substring(0, output.lastIndexOf("\""));
			    
		    //Print a debugging message
		    System.out.println("["+Picklr.MODID+"] Forum token is "+output);
			  
		    //Save the token
			token = output;
		}
		catch (Exception e)
		{
			//Catch all exceptions in 1 block
			e.printStackTrace();
		    	
			//Return error which will prevent further gathering of the message number
			token = "error";
		}
	}
	
	/*
	 * This checks for a valid token. Any issues will result in the token being set to "error" so if it's not error, we can
	 * assume that it is safe to use.
	 */
	public Boolean hasValidToken()
	{
		if(token.equals("error"))
		{
			return false;
		}
		return true;
	}

	/*
	 * This gets the raw JSON response from the server, and will throw a LoginException if there is no valid token
	 */
	public String getMessageRequestString() throws Exception
	{
		//Check the token is valid first
		if(this.hasValidToken())
		{
			//First of all, we shall create a http client
			HttpClient requestClient = new DefaultHttpClient();
			
			//We will now create the get request to the forum URL
			HttpGet alertsRequest = new HttpGet("http://hypixel.net/conversations/popup?&_xfRequestUri=/&_xfNoRedirect=1&_xfResponseType=json"); //removed &_xfToken="+token+" because token was wrong and unneeded.
			
			//We now execute the request, making sure to include the context with the cookies
			HttpResponse jsonResponse = requestClient.execute(alertsRequest, loginContext);

			// Get the response
			BufferedReader alertsResponse = new BufferedReader (new InputStreamReader(jsonResponse.getEntity().getContent()));;

			//Now we'll use a temporary line buffer and append the response into one string
			String lineBuffer = "";
			String fullResponse = "";
			
			while ((lineBuffer = alertsResponse.readLine()) != null) {
				fullResponse = fullResponse + lineBuffer;
			}
			
			//Return the response
			return fullResponse;
		
		}
		else
		{
			//If there is no valid token, throw an exception
			throw new LoginException(Picklr.MODID+ " forum login failed.");
		}
	}
	
	/*
	 * This returns the user friendly text response, or "" if there are no new messages/alerts
	 */
	public String getUserMessage(String rawInput) throws ParseException
	{
		//Firstly, we will parse the response
		JSONObject parsedResponse = (JSONObject) new JSONParser().parse(rawInput);
		
		//This holds the number of unread conversations and alerts
		String alerts = "No new forum alerts or messages";
		
		//Parse the JSON to see unread conversations
		if (Integer.valueOf((String) parsedResponse.get("_visitor_conversationsUnread")) != 0)
		{
			//if there are any unread conversations, set the string
			alerts = (String) parsedResponse.get("_visitor_conversationsUnread")+ " unread conversations";
		}
		
		//Parse the JSON to find alerts
		if (Integer.valueOf((String) parsedResponse.get("_visitor_alertsUnread")) != 0)
		{
			String unreadAlerts = (String) parsedResponse.get("_visitor_alertsUnread")+ " unread alerts";
			//Check if alerts is "empty"
			if(alerts.equals("No new forum alerts or messages"))
			{
				alerts = unreadAlerts;
			}
			else
			{
				//If it is not empty, we need to add an and in the middle
				alerts = alerts + " and " + unreadAlerts;
			}
		}
		
		//Return the alerts
		return alerts;
	}
	
}
