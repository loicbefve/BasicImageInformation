import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.*; 
import org.jfree.chart.plot.*; 
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Color;


public class Histogramme{
	
	public static int max(int a,int b, int c){
		if(a>=b && a>=c){
			
			return a;
		}
		else if(b>=c && b>=a){
			
			return b;
		}
		
		return c;
	}
	
	public static void createHisto(BufferedImage im){
		
		
		int h=im.getHeight();
		int w=im.getWidth();
		int R=0;int V=0;int B=0;
		int nR=0;int nV=0;int nB=0;
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				
			    Color pixelcolor= new Color(im.getRGB(i, j));
			      
			    R=pixelcolor.getRed();
			    V=pixelcolor.getGreen();
			    B=pixelcolor.getBlue();
				
				if(max(R,V,B)==R){
					nR++;
				}
				else if(max(R,V,B)==B){
					nB++;
				}
				else{
					nV++;
				}
			}
		}
		//on cree une nouvelle fenetre
		JFrame fenetre_histogramme=new JFrame(); 
		fenetre_histogramme.setSize(400,300);
		//on cree notre zone d affichage
		JPanel pan=new JPanel();
		fenetre_histogramme.getContentPane().add(pan);
		pan.setLayout(new java.awt.BorderLayout());
		
		
		DefaultCategoryDataset data=new DefaultCategoryDataset();
		data.addValue(nR,"rouge","color");
		data.addValue(nB,"bleu","color");
		data.addValue(nV,"vert","color");
		
		JFreeChart barChart = ChartFactory.createBarChart("Histogramme","Number","Couleur", data, PlotOrientation.VERTICAL, true, true, false);
				
		ChartPanel CP = new ChartPanel(barChart);
		pan.add(CP,BorderLayout.CENTER);
		pan.validate();
		
		fenetre_histogramme.setVisible(true);
	}
}
