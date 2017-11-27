import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class ImageViewer extends JFrame
{
	
	private static final long serialVersionUID = -2477005586868977725L;

	private static final int B_WIDTH = 200;
	private static final int B_HEIGHT = 25;
	
	private DisplayedImage inputImage  = new DisplayedImage(); 
	private DisplayedImage outputImage = new DisplayedImage();
	
	private JButton buttonHisto     = new JButton("Histogramme");
	private JButton buttonQuant     = new JButton(" Quantifie ");
	private JButton buttonInversion = new JButton(" Inversion ");

	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu   = new JMenu("File");

	private JMenuItem itemClose = new JMenuItem("Close");
	private JMenuItem itemSave  = new JMenuItem("Save");
	private JMenuItem itemSaveAs= new JMenuItem("SaveAs");
	private JMenuItem itemLoad  = new JMenuItem("Load");
	
	private JPanel output = new JPanel();
	private JPanel input  = new JPanel();
	
	
	
	public ImageViewer () {
		this.setTitle("Image Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 400);

		//Image d'entrée
		input.setLayout(new BoxLayout(input, BoxLayout.PAGE_AXIS));
		input.add(inputImage);
		
		//Image de sortie
		output.setLayout(new BoxLayout(output, BoxLayout.PAGE_AXIS));
		output.add(outputImage); 
		
		JPanel buttons=new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		
		//BoutonInversion
		buttonInversion.setMaximumSize(new Dimension(B_WIDTH,B_HEIGHT));
		buttons.add(buttonInversion);
		//BoutonHisto
		buttonHisto.setMaximumSize(new Dimension(B_WIDTH,B_HEIGHT));
		buttons.add(buttonHisto);
		//Bouton Quantifie
		buttonQuant.setMaximumSize(new Dimension(B_WIDTH,B_HEIGHT));
		buttons.add(buttonQuant);
		
		// Defines action associated to buttons
		buttonHisto.addActionListener(new Histolistener());
		buttonInversion.addActionListener(new InversionListener());
		buttonQuant.addActionListener(new Quantilistener());
		
		//Fenêtre globale
		JPanel global = new JPanel();
		global.setLayout(new BoxLayout(global, BoxLayout.LINE_AXIS));
		global.add(input);
		global.add(buttons);
		global.add(output);
		this.getContentPane().add(global);

		//Menu -> Exit
		itemClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}        
		});
		this.fileMenu.add(itemClose);  
		
		//Menu -> Save
		itemSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				MenuActions.saveImage(outputImage.getImage(),"save.png");
			}
		});
		this.fileMenu.add(itemSave);
		
		//Menu -> Save as;
		itemSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   
				int userSelection = fileChooser.showSaveDialog(ImageViewer.this);
				
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    try{
				    	fileToSave.createNewFile();
					    MenuActions.saveImage(outputImage.getImage(),fileToSave.getAbsolutePath());
					    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				    }
				    catch(Exception e){
				    	e.printStackTrace();
				    }   
				}		
			}
		});
		this.fileMenu.add(itemSaveAs);
		
		//Menu -> Load
		itemLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter =
			    new FileNameExtensionFilter("JPG, PNG & GIF Images", "jpg", "gif", "png","cst");//cst pour custom
			    chooser.setFileFilter(filter);
			    try{
			    	int returnVal = chooser.showOpenDialog(ImageViewer.this);
			    	File fichier;
			    	if(returnVal == JFileChooser.APPROVE_OPTION) {
			    		fichier=chooser.getSelectedFile();
			    		BufferedImage image=MenuActions.loadImage(fichier);
			    		outputImage.setImage(image);
						inputImage.setImage(image);
						output.repaint();
						input.repaint();
			    	}
			    }
			    catch(Exception e){
			    	e.printStackTrace();
			    }
			}
		});
		this.fileMenu.add(itemLoad);
		
		//Config menu
		this.menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		this.setVisible(true);	
		
	}	
	
	class InversionListener implements ActionListener{
		/**Listener of the "inversion" button*/
		public void actionPerformed(ActionEvent arg0)
		{
			outputImage.setImage(Inversion.inversion(outputImage.getImage()));
			output.repaint();
		}
	}
	
	class Histolistener implements ActionListener {
		/**Listener of the "histogram" button*/
		public void actionPerformed(ActionEvent arg0) {
			
			Histogram.createHisto(outputImage.getImage());
		}
	}
	
	class Quantilistener implements ActionListener {
		/**Listener of the "quantification" button*/
		public void actionPerformed(ActionEvent arg0) {
			
			outputImage.setImage(Quantification.compresse(outputImage.getImage(),true));
			output.repaint();
		}
	}
}
