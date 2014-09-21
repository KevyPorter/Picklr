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
import java.util.Timer;
import java.util.TimerTask;

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
import net.minecraft.util.EnumChatFormatting;
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
	    	String extraDashes="-------------";
	    	String prependToUsername = "";
	    	for(int i = 0; i<(16-username.length())/2; i++)
	    	{
	    		prependToUsername = prependToUsername + " ";
	    	}
	    	
	    		minecraft.fontRenderer.drawString(EnumChatFormatting.GOLD+"-----"+extraDashes+EnumChatFormatting.RESET, 0, (int) Math.floor(d-17), 0xffffffff);
	    		minecraft.fontRenderer.drawStringWithShadow(EnumChatFormatting.GOLD+prependToUsername+username+EnumChatFormatting.RESET, 27, (int) Math.floor(d-(minecraft.fontRenderer.FONT_HEIGHT/2)), 0xffffffff);
	    		minecraft.fontRenderer.drawString(EnumChatFormatting.GOLD+"-----"+extraDashes+EnumChatFormatting.RESET, 0, (int) Math.floor(d+1+(minecraft.fontRenderer.FONT_HEIGHT)), 0xffffffff);
	    }
	    
	    
	    public static Boolean startedList = false;
	    public static int currentI = 0;
	    
	    public static void resetStatus()
	    {
	    	startedList = false;
	    	currentI = 0;
	    	cancelTimerStarted = false;
	    	
	    	OnlineListManager.friends.clear();
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
	    if(OnlineListManager.friends.size() == 0)
	    {
	    	ready=false;
	    }
	    
	    for(Friend friend: OnlineListManager.friends)
	    {
	    	if(!friend.isTextureLoaded())
	    	{
	    		ready = false;
	    	}
	    }
	    
	   
	    
	    //calculate start point, bearing in mind screen seems to be 250 tall
	    double startDouble = (10 - 6)*12.5;
	    
	    int start = (int) Math.round(startDouble);
	    
	    if(ready)
	    {
	    	if(!startedList)
	    	{
	    		FriendOnlineHud.startTimer();
	    	}
	    	startedList = true;
	    	
	    	int i = 0;
	    	for(Friend friend: OnlineListManager.friends)
	    	{
	    		if(i<= 10)
	    		{
	    			if(i==currentI)
	    			{
	    				friend.drawImage(0,1*25+start, 26,26);
	    				render(mc, 1*25+start+13, friend.getFormattedUsername());
	    			}
	    			else if(i == currentI+1){
	    				friend.drawImage(0,1*25+start+36, 10,10);
	    				//it's the one above
	    			}
	    			else if(i == currentI-1){
	    				friend.drawImage(0,1*25+start-20, 10,10);
	    				//it's the one above
	    			}
	    			
	    			i++;
	    		}
	    	}
	    	
	    	if(!cancelTimerStarted)
	    	{
	    	Timer timer = new Timer();
			timer.schedule(new TimerTask(){

				@Override
				public void run() {
					if(currentI == OnlineListManager.friends.size()-1)
			    	{
			    		FriendOnlineHud.resetStatus();
			    	}
				}
				
			}, 4*1000);
			cancelTimerStarted = true;
	    	}
	    	
	    }
	  }

	  
	  public static Boolean cancelTimerStarted = false;

	public static void startTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				if(FriendOnlineHud.startedList)
				{
					FriendOnlineHud.currentI = FriendOnlineHud.currentI+1;
				}
				else
				{
					this.cancel();
				}
			}
			
		}, 4*1000, 4*1000);
	}   
	  

		

}
