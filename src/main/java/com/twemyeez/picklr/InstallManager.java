package com.twemyeez.picklr;

import java.awt.*;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.*;

import com.twemyeez.picklr.auth.CommonAPI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.*;

public class InstallManager {

	/*
	 * This downloads and unzips libraries if required
	 */
	public static void unzip() {

		// Firstly get the working directory
		String workingDirectory = Minecraft.getMinecraft().mcDataDir
				.getAbsolutePath();

		// If it ends with a . then remove it
		if (workingDirectory.endsWith(".")) {
			workingDirectory = workingDirectory.substring(0,
					workingDirectory.length() - 1);
		}

		// If it doesn't end with a / then add it
		if (!workingDirectory.endsWith("/") && !workingDirectory.endsWith("\\")) {
			workingDirectory = workingDirectory + "/";
		}

		// Use a test file to see if libraries installed
		File file = new File(workingDirectory + "mods/mp3spi1.9.5.jar");

		// If the libraries are installed, return
		if (file.exists()) {
			System.out.println("Checking " + file.getAbsolutePath());
			System.out.println("Target file exists, so not downloading API");
			return;
		}

		// Now try to download the libraries
		try {

			String location = CommonAPI
					.carryOutAsyncApiRead("https://picklr.me/api/v1/library_location.txt");

			// Define the URL
			URL url = new URL(location);

			// Get the ZipInputStream
			ZipInputStream zipInput = new ZipInputStream(
					new BufferedInputStream((url).openStream()));

			// Use a temporary ZipEntry as a buffer
			ZipEntry zipFile;

			// While there are more file entries
			while ((zipFile = zipInput.getNextEntry()) != null) {
				// Check if it is one of the file names that we want to copy
				Boolean required = false;
				if (zipFile.getName().indexOf("mp3spi1.9.5.jar") != -1) {
					required = true;
				}
				if (zipFile.getName().indexOf("jl1.0.1.jar") != -1) {
					required = true;
				}
				if (zipFile.getName().indexOf("tritonus_share.jar") != -1) {
					required = true;
				}
				if (zipFile.getName().indexOf("LICENSE.txt") != -1) {
					required = true;
				}

				// If it is, then we shall now copy it
				if (!zipFile.getName().replace("MpegAudioSPI1.9.5/", "")
						.equals("")
						&& required) {

					// Get the file location
					String tempFile = new File(zipFile.getName()).getName();

					tempFile = tempFile.replace("LICENSE.txt",
							"MpegAudioLicence.txt");

					// Initialise the target file
					File targetFile = (new File(workingDirectory + "mods/"
							+ tempFile.replace("MpegAudioSPI1.9.5/", "")));

					// Print a debug/alert message
					System.out.println("Picklr is extracting to "
							+ workingDirectory + "mods/"
							+ tempFile.replace("MpegAudioSPI1.9.5/", ""));

					// Make parent directories if required
					targetFile.getParentFile().mkdirs();

					// If the file does not exist, create it
					if (!targetFile.exists()) {
						targetFile.createNewFile();
					}

					// Create a buffered output stream to the destination
					BufferedOutputStream destinationOutput = new BufferedOutputStream(
							new FileOutputStream(targetFile, false), 2048);

					// Store the data read
					int bytesRead;

					// Data buffer
					byte dataBuffer[] = new byte[2048];

					// While there is still data to write
					while ((bytesRead = zipInput.read(dataBuffer, 0, 2048)) != -1) {
						// Write it to the output stream
						destinationOutput.write(dataBuffer, 0, bytesRead);
					}

					// Flush the output
					destinationOutput.flush();

					// Close the output stream
					destinationOutput.close();
				}

			}
			// Close the zip input
			zipInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
