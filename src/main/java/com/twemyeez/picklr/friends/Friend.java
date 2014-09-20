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
	

	private String username;
	private String location;
	private File skin;
	  
	private File file;
		private BufferedImage image;
		private DynamicTexture previewTexture;
		private ResourceLocation resourceLocation;
		private TextureManager textureManager;
		
		private Boolean textureDownloaded = false;
		private Boolean textureLoaded = false;

	public Friend(final String username, String location)
	{
		this.username = username;
		this.location = location;
		textureManager = Minecraft.getMinecraft().getTextureManager();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					skin = File.createTempFile(username, ".png");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    try {
					FileUtils.copyURLToFile(new URL("https://www.twemyeez.com/Picklr/skin.php?u="+username+"&s=80"), skin);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    textureDownloaded = true;
				
			}
			
		}).start();
		 
	}
	
	public String getName()
	{
		return username;
	}

	public String getStatus()
	{
		return location;
	}
	
	public Boolean isTextureLoaded()
	{
		return textureDownloaded;
	}
	
	public boolean loadPreview()
	{
		try {
			image = ImageIO.read(skin);
			previewTexture = new DynamicTexture(image);
			resourceLocation = textureManager.getDynamicTextureLocation("preview", previewTexture);
			textureLoaded = true;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void drawImage(int xPos, int yPos, int width, int height)
	{
		if(!textureLoaded)
		{
			loadPreview();
		}
		previewTexture.updateDynamicTexture();
		Tessellator tessellator = Tessellator.instance;
		textureManager.bindTexture(resourceLocation);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tessellator.startDrawingQuads();
		
		tessellator.addVertexWithUV(xPos        , yPos + height, 0, 0.0, 1.0);
		tessellator.addVertexWithUV(xPos + width, yPos + height, 0, 1.0, 1.0);
		tessellator.addVertexWithUV(xPos + width, yPos         , 0, 1.0, 0.0);
		tessellator.addVertexWithUV(xPos        , yPos         , 0, 0.0, 0.0);
		
		
		tessellator.draw();
	}

}
