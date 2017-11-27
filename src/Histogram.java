import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.*; 
import org.jfree.chart.plot.*; 
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * <p> This class contains a static method required to create an histogram of R-G-B pixels within an image</p>
 */
public class Histogram{
	
	private static int max(int a,int b, int c){
		if(a>=b && a>=c){
			
			return a;
		}
		else if(b>=c && b>=a){
			
			return b;
		}
		
		return c;
	}
	/**
	 * method that create an histogram of im in a new Jframe
	 * @param im is a BufferedImage 
	 * 
	 */
	public static void createHisto(BufferedImage im){
		
		
		int h=im.getHeight();
		int w=im.getWidth();
		int R=0;int G=0;int B=0;
		int nR=0;int nG=0;int nB=0;
		
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				
			    Color pixelcolor= new Color(im.getRGB(i, j));
			      
			    R=pixelcolor.getRed();
			    G=pixelcolor.getGreen();
			    B=pixelcolor.getBlue();
				
				if(max(R,G,B)==R){
					nR++;
				}
				else if(max(R,G,B)==B){
					nB++;
				}
				else{
					nG++;
				}
			}
		}
		//creating a new Frame
		JFrame histogram_window=new JFrame(); 
		histogram_window.setSize(400,300);
		//creating Panel
		JPanel pan=new JPanel();
		histogram_window.getContentPane().add(pan);
		pan.setLayout(new java.awt.BorderLayout());
		
		
		DefaultCategoryDataset data=new DefaultCategoryDataset();
		data.addValue(nR,"red","color");
		data.addValue(nB,"blue","color");
		data.addValue(nG,"green","color");
		
		JFreeChart barChart = ChartFactory.createBarChart("Histogram","Number","Color", data, PlotOrientation.VERTICAL, true, true, false);
				
		ChartPanel CP = new ChartPanel(barChart);
		pan.add(CP,BorderLayout.CENTER);
		pan.validate();
		
		histogram_window.setVisible(true);
	}
}
