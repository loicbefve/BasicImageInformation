import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
 
public class DisplayedImage extends JPanel {
	
	private static final long serialVersionUID = -3544787848737362127L;
	private BufferedImage image;
    
    public DisplayedImage() {
    		try {
    			//Lecture de l'image et stockage dans un BufferedImage
    			image = ImageIO.read(new File("img.png"));
        	} catch (IOException e) {
        		e.printStackTrace();
        	}                
    }
    
    public void paintComponent(Graphics g){
    		//g.drawImage(image, 0, 0, this); // draw as much as possible
    		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this); // draw full image
    } 
    
    public BufferedImage getImage(){
    	
    	return this.image;
    }
    
    public void setImage( BufferedImage newImage) {
    	this.image = newImage;
    }
}
