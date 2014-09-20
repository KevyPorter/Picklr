package com.twemyeez.picklr.hud;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import com.twemyeez.picklr.friends.Friend;
import com.twemyeez.picklr.friends.OnlineListManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class FriendOnlineHud extends Gui{
	public ArrayList<String> favouriteFriends = new ArrayList<String>();

	  private Minecraft mc;
	  

	  public FriendOnlineHud(Minecraft mc) throws IOException
	  {
	    super();
	    // We need this to invoke the render engine.
	    this.mc = mc;
	    
	   
	  }
	  
	
	    public void render(Minecraft minecraft, double d, String username) {
	    	
	    		minecraft.fontRenderer.drawString(username, 10, (int) Math.round(d), 0xffffffff);
	    	
	    }
	    

	  @SubscribeEvent
	  public void onRenderExperienceBar(RenderGameOverlayEvent event)
	  {
	    if(event.isCancelable() || event.type != ElementType.EXPERIENCE)
	    {      
	      return;
	    }
	    
	    //First let's check we can draw them
	    Boolean ready = true;
	    for(Friend friend: OnlineListManager.friends)
	    {
	    	if(!friend.isTextureLoaded())
	    	{
	    		ready = false;
	    	}
	    }
	    
	    //calculate start point, bearing in mind screen seems to be 250 tall
	    double startDouble = (10 - OnlineListManager.friends.size())*12.5;
	    
	    int start = (int) Math.round(startDouble);
	    
	    if(ready)
	    {
	    	int i = 0;
	    	for(Friend friend: OnlineListManager.friends)
	    	{
	    		if(i<= 10)
	    		{
	    			friend.drawImage(0,i*25+start, 25,25);
	    			i++;
	    			
	    			if(i==1)
	    			{
	    				render( mc, i*25+start+12.5, friend.getName());
	    			}
	    		}
	    	}
	    }
	  }   
	  

		

}
