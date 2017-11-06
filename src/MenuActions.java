import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import sun.awt.image.ImageFormatException;

/**
 * 
 * <p>Class that contains static methods to use with the menu buttons
 * 
 * <ul>
 * <li>saveImage to save an image into a file</li>
 * <li>loadImage to load an image in a BufferedImage</li>
 * </ul>
 * <p>
 *
 */
public class MenuActions {
	/**
	 * Public function that save an Image in the given file,
	 * @param image the image to save
	 * @param name the filename in which it should be saved
	 * @return a boolean : true is the image has been saved, false otherwise
	 */
	public static Boolean saveImage(BufferedImage image,String name)
	{
		Boolean b = false;
			
			try{	
				String[] n=name.split("\\.");
				
				if(n.length==2 && n[1].equals("cst")){//si c'est une image custom avec un nom propre
					CustomImage.write(image,name);
					System.out.println("Format cst");
					b=true;
				}
				else{
					b = ImageIO.write(image, "png", new File(name));
				}
			}
	    	catch (Exception e) {
	    		e.printStackTrace();
	    	}
			
			return b;
		}
	
	/**
	 * Public function that create a BufferedImage from a chosen file, if it fails returns null
	 * @param fichier a file from which the image should be loaded
	 * @return a BufferedImage 
	 * @throws ImageFormatException,IOException 
	 */
	public static BufferedImage loadImage(File fichier) throws ImageFormatException,IOException {
		
	    BufferedImage im=null;
	    try
	    {
	    	String name=fichier.getName();
	    	String[] n=name.split("\\.");
	    	if(n.length==2 && n[1].equals("cst")){
	    				
	    		im=CustomImage.read(fichier);	
	    	}
	    	else{
	    				
	    		im=ImageIO.read(fichier);
	    	}
	    }
	    catch (Exception e) {
	    			
	        throw e;
	    }
	    
	    return im;
	}
}
