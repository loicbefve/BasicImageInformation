import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class Histolistener implements ActionListener {

	private BufferedImage image;
		
	public Histolistener(BufferedImage _image){
		
		this.image=_image;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.createHisto();
	}
	
	public void createHisto(){
		
		
	}
}
