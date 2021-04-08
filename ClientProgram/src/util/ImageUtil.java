package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import view.FriendListView;

public class ImageUtil {

	/**
	 * Õº∆¨ª“∂»¥¶¿Ì
	 * @param f
	 * @throws IOException
	 */
	public static void getGrayImage(File f) throws IOException {
		BufferedImage img = ImageIO.read(f);
		int w = img.getWidth();
		int h = img.getHeight();
		int[] data = img.getRGB(0, 0, w, h, null, 0, w);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int c = data[j + i * w];  //32bit  
				int A = (c >> 24) & 0xFF;
				int R = (c >> 16) & 0xFF;
				int G = (c >> 8) & 0xFF;
				int B = (c >> 0) & 0xFF;
                int gray=(int)(R*0.3)+(int)(G*0.59)+(int)(B*0.11);
				data[j + i* w] = (A << 24) | (gray << 16) | (gray << 8) | (gray << 0);
			}
		}    
		img.setRGB(0, 0, w, h, data, 0, w);
		ImageIO.write(img,"png",new File("..."));
	}
	
}
