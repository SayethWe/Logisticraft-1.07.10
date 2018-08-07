package com.sinesection.utils;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import com.sinesection.logisticraft.Logisticraft;

public class LogisticraftUtils {

	public static BufferedImage[] splitImage(int rows, int cols, String imageLocation) throws FileNotFoundException, IOException {
		BufferedImage[] result = new BufferedImage[rows * cols];
		BufferedImage image = ImageIO.read(Logisticraft.class.getResourceAsStream(imageLocation));

		if (image == null)
			return null;

		int chunkWidth = image.getWidth() / cols; // determines the chunk width
													// and height
		int chunkHeight = image.getHeight() / rows;
		Log.info("Splitting image, chunkWidth=" + chunkWidth + ", chunkHeight=" + chunkHeight + ", rows=" + rows + ", cols=" + cols);
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				result[x + y * rows] = image.getSubimage(x * chunkWidth, y * chunkHeight, chunkWidth, chunkHeight);
			}
		}

		return result;
	}
	
	public static NBTTagCompound removeLocationFromNBT(NBTTagCompound nbt) {
		nbt.removeTag("x");
		nbt.removeTag("y");
		nbt.removeTag("z");
		return nbt;
	}
	
	public static NBTTagCompound addLocationToNBT(NBTTagCompound nbt, int x, int y, int z) {
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		return nbt;
	}
}
