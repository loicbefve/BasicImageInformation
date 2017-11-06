import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.WritableRaster;
import kdtree.*;

/**
 * <p> Class used to compress a given image.</p> 
 * <p>the static value L2N_COLORS is the log2 of the actual number of colors we want in the color list </p>
 */
public class Quantification {
	
	public static final int L2N_COLORS=4;
	/**
	 * Public static function that returns the index of a given value in an array
	 * @param int[] palette : array of integer
	 * @param int valeur 
	 * @return the index if it is in, -1 otherwise
	 */
	public static int getIndex(int[] palette, int valeur){
	//fonction qui permet de retrouve l'index d'une couleur dans la palette, -1 si pas dans la palette
		for(int i=0;i<palette.length;i++){
			if(palette[i]==valeur){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Public static function that compress a given image. It indexes each pixel in an array of colors with a given size
	 * @param image
	 * @return a compressed BufferedImage
	 */
	public static BufferedImage compresse(BufferedImage image){
		
		List<Point> listePoints =new ArrayList<Point>();//liste des pixels de l'image
		int R=0;int V=0;int B=0;
		Color pixelcolor; 
		for(int i=0;i<image.getWidth();i++){
			
			for(int j=0;j<image.getHeight();j++){
				//on recupere la couleur
				pixelcolor=new Color(image.getRGB(i, j));
				R=pixelcolor.getRed();
				V=pixelcolor.getGreen();
				B=pixelcolor.getBlue();
				listePoints.add(new Point(R,V,B));
			}
		}
		
		KdTree tree_palette=new KdTree(listePoints,3);
		tree_palette.troncature(tree_palette.racine,L2N_COLORS);
		int palette[]=tree_palette.remplissage(L2N_COLORS);//on recupere les couleurs sous forme 0xRRVVBB
		
		Point p=new Point(0,0,0);
		Point g;
		
		int ig=0;
		byte index[]=new byte[image.getWidth()*image.getHeight()];
		for(int i=0;i<image.getHeight();i++){
			for(int j=0;j<image.getWidth();j++){
				
				pixelcolor=new Color(image.getRGB(i, j));
				
				R=pixelcolor.getRed();
				V=pixelcolor.getGreen();
				B=pixelcolor.getBlue();
				
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
	
