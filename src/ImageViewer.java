import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ImageViewer extends JFrame /*implements ActionListener*/
{
	private DisplayedImage inputImage = new DisplayedImage(); 
	private DisplayedImage ouputImage = new DisplayedImage();
	private JButton buttonAction = new JButton("Action");
	private JButton buttonHisto = new JButton("Histogramme");
	private JButton buttonInversion = new JButton("Inversion");

	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");

	private JMenuItem itemClose = new JMenuItem("Close");
	
	private JPanel output = new JPanel();

	public ImageViewer () {
		this.setTitle("Image Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 400);
		
		//Image d'entrée
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.PAGE_AXIS));
		input.add(inputImage);
		
		//Bouton1
		JPanel action = new JPanel();
		action.setLayout(new BoxLayout(action, BoxLayout.PAGE_AXIS));
		action.add(buttonAction);
		
		//BoutonInversion
		JPanel inversion = new JPanel();
		inversion.setLayout(new BoxLayout(inversion, BoxLayout.PAGE_AXIS));
		inversion.add(buttonInversion);
		
		// Defines action associated to buttons
		buttonAction.addActionListener(new ButtonListener());
		
		JPanel histo = new JPanel();
		histo.setLayout(new BoxLayout(histo, BoxLayout.PAGE_AXIS));
		histo.add(buttonHisto);
		// Defines action associated to buttons
		buttonHisto.addActionListener(new Histolistener(ouputImage));

		JPanel output = new JPanel();

		buttonInversion.addActionListener(new InversionListener());
		
		//Image de sortie
		output.setLayout(new BoxLayout(output, BoxLayout.PAGE_AXIS));
		output.add(ouputImage); 

		JPanel global = new JPanel();
		global.setLayout(new BoxLayout(global, BoxLayout.LINE_AXIS));
		global.add(input);
		global.add(action);
		global.add(histo);
		global.add(inversion);
		global.add(output);

		this.getContentPane().add(global);

		this.fileMenu.addSeparator();
		itemClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}        
		});
		this.fileMenu.add(itemClose);  

		this.menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);

		this.setVisible(true);
	}

	/**
	 * Class listening to a given button
	 */
	class ButtonListener implements ActionListener{
		
		
		public void actionPerformed(ActionEvent arg0) 
		{
			System.out.println("Action Performed");
		}
		
	}
	
	class InversionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0)
		{
			BufferedImage imageInput = inputImage.image;
			BufferedImage imageOutput = imageInput;
			
			int width = imageInput.getWidth();
			int height = imageInput.getHeight();
			
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
		            
		            ouputImage.image = imageOutput;
		        }
		    }
			output.repaint();
		}
	}
	

}