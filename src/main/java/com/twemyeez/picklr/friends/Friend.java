package com.twemyeez.picklr.friends;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class Friend {
	/*
	 * This object represents a friend, and contains data on their skin, location, and username
	 */
	
	//The username of the friend
	private String username;
	
	//Username of the friend with any formatting from the friends list, such as rank colour
	private String formattedUsername;
	
	//The server location of the friend
	private String location;
	
	//The file where their skin is stored
	private File skin;
	  
	//The image where their skin is stored
	private BufferedImage image;
	//The dynamic texture for their skin
	private DynamicTexture skinTexture;
	//The resource location for the skin
	private ResourceLocation skinResourceLocation;
	//The texture manager
	private TextureManager skinTextureManager;
		
	//This boolean indicates whether the skin has finished downloading
	private Boolean textureDownloaded = false;
	//This boolean indicates whether the texture is loaded, or we have to load it
	private Boolean textureLoaded = false;

	/*
	 * Friend constructor
	 */
	public Friend(final String username, String formattedUsername, String location)
	{
		//Set the various data parameters
		
		//Set username
		this.username = username;
		//Save the formatted username
		this.formattedUsername = formattedUsername;
		//Save the location of the user
		this.location = location;
		
		//Get the texture manager for rendering
		skinTextureManager = Minecraft.getMinecraft().getTextureManager();
		
		//Start a thread to download the skin
		new Thread(new Runnable(){

			@Override
			public void run() {
				try
				{
					//Create a temporary file for the username
					skin = File.createTempFile(username, ".png");
					//Download the head preview
					FileUtils.copyURLToFile(new URL("https://www.twemyeez.com/Picklr/skin.php?u="+username+"&s=80"), skin);
					//Indicate the texture is now downloaded
					textureDownloaded = true;
				}
				catch (Exception e)
				{
					//If this fails, print a stack trace for debugging
					e.printStackTrace();
				}
			}
			
		}).start();
		 
	}
	
	//Get the formatted username associated with the object
	public String getFormattedUsername()
	{
		return formattedUsername;
	}
	
	//Get the username associated
	public String getName()
	{
		return username;
	}

	//Get the location/status
	public String getStatus()
	{
		return location;
	}
	
	//Returns true if the texture is loaded, and false if now
	public Boolean isTextureLoaded()
	{
		return textureDownloaded;
	}

	//Draw the head of the user
	public void drawHead(int headX, int headY, int headWidth, int headHeight)
	{
		//If the texture isn't yet loaded, load it
		if(!textureLoaded)
		{
			try
			{
				//Read the skin file as an image
				image = ImageIO.read(skin);
				
				//Assign it to the dynamic texture
				skinTexture = new DynamicTexture(image);
				
				//Set the resource location
				skinResourceLocation = skinTextureManager.getDynamicTextureLocation("skinTexture", skinTexture);
				
				//Indicate we've now loaded the skin, so we don't reload it in future
				textureLoaded = true;
			}
			catch (IOException e)
			{
				//If an error occurred, we can print the stack trace for debugging
				e.printStackTrace();
			}
		}
		
		//Update the dynamic texture
		skinTexture.updateDynamicTexture();
		
		//Get the tessellator
		Tessellator tessellator = Tessellator.instance;
		
		//Bind the skin texture
		skinTextureManager.bindTexture(skinResourceLocation);
		
		//Enable OpenGL blend functions
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//Start drawing the quadrilateral
		tessellator.startDrawingQuads();
		
		//Add the vertices
		tessellator.addVertexWithUV(headX, headY + headHeight, 0, 0.0, 1.0);
		tessellator.addVertexWithUV(headX + headWidth, headY + headHeight, 0, 1.0, 1.0);
		tessellator.addVertexWithUV(headX + headWidth, headY, 0, 1.0, 0.0);
		tessellator.addVertexWithUV(headX, headY, 0, 0.0, 0.0);
		
		//Draw the image
		tessellator.draw();
	}

}
