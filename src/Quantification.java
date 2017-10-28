import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.WritableRaster;
import kdtree.*;

public class Quantification {
	
	public static int getIndex(int[] palette, int valeur){
	//fonction qui permet de retrouve l'index d'une couleur dans la palette, -1 si pas dans la palette
		for(int i=0;i<palette.length;i++){
			if(palette[i]==valeur){
				return i;
			}
		}
		return -1;
	}
	
	public static BufferedImage compresse(BufferedImage image){
		
		List<Point> listePoints =new ArrayList<Point>();//liste des pixels de l'image
		int R=0;int V=0;int B=0;
		
		for(int i=0;i<image.getWidth();i=i+10){
			//TODO refaire le tri, le +10 sert à éviter les problèmes de récursion dans le tri
			for(int j=0;j<image.getHeight();j=j+10){
				//on recupere la couleur
				R=(image.getRGB(i,j) & 0xFF0000)>>16;
				V=(image.getRGB(i,j) & 0x00FF00)>>8;
				B=(image.getRGB(i,j) & 0x0000FF);
				listePoints.add(new Point(R,V,B));
			}
		}
		
		KdTree tree_palette=new KdTree(listePoints,3);
		tree_palette.troncature(tree_palette.racine,4);//on aura 
		int palette[]=tree_palette.remplissage(4);//on recupere les couleurs sous forme 0xRRVVBB
		
		Point p=new Point(0,0,0);
		Point g;
		
		int ig=0;
		byte index[]=new byte[image.getWidth()*image.getHeight()];
		for(int i=0;i<image.getHeight();i++){
			for(int j=0;j<image.getWidth();j++){
				
				R=(image.getRGB(j,i) & 0xFF0000)>>16;
				V=(image.getRGB(j,i) & 0x00FF00)>>8;
				B=(image.getRGB(j,i) & 0x0000FF);
				p.setCoord(R,V,B);
				g=tree_palette.getNearestNeighbor(p);//g est le point de la palette correspondant à p
				ig=(g.getCoord(0)<<16 | g.getCoord(1)<<8 | g.getCoord(2));//on transforme g en une couleur 0xRRVVBB
				index[i*image.getWidth()+j]=(byte)Quantification.getIndex(palette,ig);//on met l'indice de g dans la palette
			}
		}
		//Creation de la nouvelle BufferedImage compressee
		byte r[]=new byte[palette.length];
		byte v[]=new byte[palette.length];
		byte b[]=new byte[palette.length];
		
		for(int i=0;i<palette.length;i++){
			
			r[i]=(byte) ((palette[i]& 0xFF0000)>>16);
			v[i]=(byte) ((palette[i]& 0x00FF00)>>8);
			b[i]=(byte) (palette[i]& 0x0000FF);
		}
		DataBufferByte dataBuffer = new DataBufferByte(index, image.getWidth()*image.getHeight());
		WritableRaster raster=Raster.createPackedRaster(dataBuffer,image.getWidth(),image.getHeight(),8,null);
		IndexColorModel cm=new IndexColorModel(8,palette.length,r,v,b);
		BufferedImage image_compressee=new BufferedImage(cm,raster,true,null);
		
		System.out.println("Compression reussie");
		
		return image_compressee;
		
		
	}
	
}
	
