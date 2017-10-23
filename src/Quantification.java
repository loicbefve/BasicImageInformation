import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import kdtree.*;

public class Quantification {
	
	public static BufferedImage quantifie(BufferedImage image){
		
		List<Point> listePoints =new ArrayList<Point>();
		int R=0;int V=0;int B=0;
		for(int i=0;i<image.getWidth();i=i+10){
			for(int j=0;j<image.getHeight();j=j+10){
				//on recupere la couleur sous une forme chelou
				R=(image.getRGB(i,j) & 0xFF0000)>>16;
				V=(image.getRGB(i,j) & 0x00FF00)>>8;
				B=(image.getRGB(i,j) & 0x0000FF);
				listePoints.add(new Point(R,V,B));
			}
		}
		System.out.println(listePoints.size());
		KdTree palette=new KdTree(listePoints,3);
		palette.troncature(palette.racine,8);
		
		Point p=new Point(0,0,0);
		Point g;
		
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				
				R=(image.getRGB(i,j) & 0xFF0000)>>16;
				V=(image.getRGB(i,j) & 0x00FF00)>>8;
				B=(image.getRGB(i,j) & 0x0000FF);
				p.setCoord(R,V,B);
				g=palette.getNearestNeighbor(p);
				
				
				Color rgbNew = new Color(g.getCoord(0),g.getCoord(1),g.getCoord(2));
			    int rgb = rgbNew.getRGB();
			    image.setRGB(i, j, rgb);
			}
		}
		
		return image;
	}
}
