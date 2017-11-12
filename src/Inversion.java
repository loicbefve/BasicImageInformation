import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 
 * This class contains a function which make a color inversion of an image
 *
 */
public class Inversion {

	/**
	 * 
	 * @param imageInput a BufferedImage in which each Pixel is transformed, it is not modified
	 * @return a BufferedImage in which each pixel is inverted
	 */
	public static BufferedImage inversion(BufferedImage imageInput){
	
		int width = imageInput.getWidth();
		int height = imageInput.getHeight();
		BufferedImage imageOutput = new BufferedImage(width,height,1);
			
		for (int i = 0; i < width; i++) {
		    for (int j = 0; j < height; j++) {
			         
		      	//Je récupère la couleur de chaque pixel
			    Color pixelcolor= new Color(imageInput.getRGB(i, j));
			           
			    //Je récupère chaque composante
			    int r=pixelcolor.getRed();
			    int g=pixelcolor.getGreen();
			    int b=pixelcolor.getBlue();
			             
			    //J'inverse les couleurs
			    r=Math.abs(r-255);
			    g=Math.abs(g-255);
			    b=Math.abs(b-255);
			            
			    //Je repasse en int
			    Color rgbNew = new Color(r,g,b);
			    int rgb = rgbNew.getRGB();
			             
			    //Je modifie l'output
			    imageOutput.setRGB(i, j, rgb);
			}
		}
		return imageOutput;
	}
}
