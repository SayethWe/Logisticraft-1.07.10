package com.sinesection.utils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class LogisticraftUtils {
	
	private static final String DEFAULT_TITLE = "Uninitialized Logger";
	
	private static Logger logger;
	
	public static void createLogger(String title) {
		logger = LogManager.getFormatterLogger(title);
	}
	
	public static Logger getLogger() {
		if(logger == null) logger = LogManager.getFormatterLogger(DEFAULT_TITLE);
		return logger;
	}
	
	public static BufferedImage[][] splitImage(int rows, int cols, String imageLocation) throws FileNotFoundException, IOException {
		BufferedImage[][] result = new BufferedImage[rows][cols];
		BufferedImage image = ImageIO.read(new FileInputStream(new File(imageLocation)));
		
		int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
	    int chunkHeight = image.getHeight() / rows;
	    
	    for(int x = 0; x < rows; x++) {
	    		for(int y = 0; y< cols; y++) {
	    			result[x][y] = new BufferedImage(chunkWidth,chunkHeight,image.getType());
	    			
	    			Graphics2D gr = result[x][y].createGraphics();
	                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * (y+1), chunkHeight * (x+1), null);
	                gr.dispose();
	    		}
	    }
	     
	    return result;
	}
}
