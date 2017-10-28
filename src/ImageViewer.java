import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImageViewer extends JFrame
{
	
	private static final long serialVersionUID = -2477005586868977725L;
	
	private DisplayedImage inputImage = new DisplayedImage(); 
	private DisplayedImage outputImage = new DisplayedImage();
	
	private JButton buttonHisto = new JButton("Histogramme");
	private JButton buttonQuant = new JButton("Quantifie");
	private JButton buttonInversion = new JButton("Inversion");

	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");

	private JMenuItem itemClose = new JMenuItem("Close");
	private JMenuItem itemSave = new JMenuItem("Save");
	private JMenuItem itemLoad = new JMenuItem("Load");
	
	private JPanel output = new JPanel();
	private JPanel input = new JPanel();
	
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
		
		//BoutonInversion
		JPanel inversion = new JPanel();
		inversion.setLayout(new BoxLayout(inversion, BoxLayout.PAGE_AXIS));
		inversion.add(buttonInversion);
		
		//BoutonHisto
		JPanel histo = new JPanel();
		histo.setLayout(new BoxLayout(histo, BoxLayout.PAGE_AXIS));
		histo.add(buttonHisto);
		
		//Bouton Quantifie
		JPanel quant = new JPanel();
		quant.setLayout(new BoxLayout(quant, BoxLayout.PAGE_AXIS));
		quant.add(buttonQuant);
		
		// Defines action associated to buttons
		buttonHisto.addActionListener(new Histolistener());
		buttonInversion.addActionListener(new InversionListener());
		buttonQuant.addActionListener(new Quantilistener());
		
		//Fenêtre globale
		JPanel global = new JPanel();
		global.setLayout(new BoxLayout(global, BoxLayout.LINE_AXIS));
		global.add(input);
		global.add(histo);
		global.add(inversion);
		global.add(quant);
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
				saveImage("save.png");
			}
		});
		this.fileMenu.add(itemSave);
		
		//Menu -> Load
		itemLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImage();
			}
		});
		this.fileMenu.add(itemLoad);
		
		//Config menu
		this.menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		this.setVisible(true);	
	}		

	public Boolean saveImage(String name)
	{
		Boolean b = false;
		
		
		try
		{
			InputStream flux=new FileInputStream(name); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String a=buff.readLine();
			
			if(a.equals("P10")){//si c'est une image custom
				CustomImage.write(outputImage.getImage(),name);
			}
			else{
				b = ImageIO.write(outputImage.getImage(), "png", new File(name));
			}
			
			buff.close();
			lecture.close();
			flux.close();
		}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
		
		return b;
	}
	
	public void loadImage() {
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "JPG, PNG & GIF Images", "jpg", "gif", "png","cst");//cst pour custom
	    chooser.setFileFilter(filter);
	    
	    try
	    {
	    	int returnVal = chooser.showOpenDialog(ImageViewer.this);
	    	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    			
	    		try
	    		{
	    			File fichier=chooser.getSelectedFile();
	    			
	    			InputStream flux=new FileInputStream(fichier); 
	    			InputStreamReader lecture=new InputStreamReader(flux);
	    			BufferedReader buff=new BufferedReader(lecture);
	    			String a=buff.readLine();
	    			
	    			if(a.equals("P10")){
	    				
	    				inputImage.setImage(CustomImage.read(fichier));
	    				outputImage.setImage(CustomImage.read(fichier));
	    			}
	    			else{
	    				inputImage.setImage(ImageIO.read(fichier));
	    				outputImage.setImage(ImageIO.read(fichier));
	    			}
	    			
	    			input.repaint();
	    			output.repaint();
	    			
	    			buff.close();
	    			lecture.close();
	    			flux.close();
	  
	    		}
	    		catch (IOException e) {
	        		e.printStackTrace();
	        	}    
	    	}
	    }
	    catch (HeadlessException e)
	    {
	    	e.printStackTrace();
	    }
	}
	


	/**
	 * Listener des differents boutons
	 */

	class InversionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0)
		{
			outputImage.setImage(Inversion.inversion(outputImage.getImage()));
			output.repaint();
		}
	}
	
	class Histolistener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			
			Histogramme.createHisto(outputImage.getImage());
		}
	}
	
	class Quantilistener implements ActionListener {	
		public void actionPerformed(ActionEvent arg0) {
			
			outputImage.setImage(Quantification.compresse(outputImage.getImage()));
			output.repaint();
		}
	}
}
