import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import kdtree.*;

public class Quantification {
	
	public static String encode(BufferedImage image,int[] palette){
		String image_encodee="";
		for(int i=0;i<palette.length;i++){
			image_encodee.concat(Integer.toHexString(palette[i])+" ");
		}
		
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				
				image_encodee.concat(Integer.toHexString(0x00FFFFFF&image.getRGB(i,j))+" ");
			}
		}
		
		return image_encodee;
		
	}
	
	public static BufferedImage quantifie(BufferedImage image){
		
		List<Point> listePoints =new ArrayList<Point>();
		int R=0;int V=0;int B=0;
		for(int i=0;i<image.getWidth();i=i+10){//le +10 sert à éviter les problèmes de récursion dans le tri
			for(int j=0;j<image.getHeight();j=j+10){
				//on recupere la couleur sous une forme chelou
				R=(image.getRGB(i,j) & 0xFF0000)>>16;
				V=(image.getRGB(i,j) & 0x00FF00)>>8;
				B=(image.getRGB(i,j) & 0x0000FF);
				listePoints.add(new Point(R,V,B));
			}
		}
		System.out.println(listePoints.size());
		KdTree tree_palette=new KdTree(listePoints,3);
		tree_palette.troncature(tree_palette.racine,4);
		int palette[]=tree_palette.remplissage(4);
		//palette contient les 16 couleurs au format 0xRRGGBB
		for(int i=0;i<16;i++){
		System.out.printf("0x%06X palette \n",palette[i]);
		}
		Point p=new Point(0,0,0);
		Point g;
		String image2="";
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				
				R=(image.getRGB(i,j) & 0xFF0000)>>16;
				V=(image.getRGB(i,j) & 0x00FF00)>>8;
				B=(image.getRGB(i,j) & 0x0000FF);
				p.setCoord(R,V,B);
				g=tree_palette.getNearestNeighbor(p);
				
				
				Color rgbNew = new Color(g.getCoord(0),g.getCoord(1),g.getCoord(2));
			    int rgb = rgbNew.getRGB();
			    image.setRGB(i, j, rgb);
			    
			}
		}
		image2+=encode(image,palette);
		System.out.printf(image2);
		return image;
	}
}
