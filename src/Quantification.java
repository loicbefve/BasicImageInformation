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
 * <p>the static value L2N_COLORS is log2 of the maximum number of colors we want in the color list, it should not be greater than 8 </p>
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
	
		for(int i=0;i<palette.length;i++){
			if(palette[i]==valeur){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Public static function that compress a given image. It indexes each pixel in an array of colors of size 2^L2N_COLORS
	 * @param image
	 * @param beautiful 
	 * @return a compressed BufferedImage
	 */
	public static BufferedImage compresse(BufferedImage image, boolean beautiful){
		
		List<Point> listOfPoints =new ArrayList<Point>();//list of the image's pixels
		int R=0;int G=0;int B=0;
		Color pixelColor; 
		
		//creating a list containing every pixel of the image
		for(int i=0;i<image.getWidth();i++){
			
			for(int j=0;j<image.getHeight();j++){
				
				pixelColor=new Color(image.getRGB(i, j));
				R=pixelColor.getRed();
				G=pixelColor.getGreen();
				B=pixelColor.getBlue();
				listOfPoints.add(new Point(R,G,B,0));
			}
		}
		//creating a KdTree from the pixels
		KdTree tree=new KdTree(listOfPoints,3,L2N_COLORS+1);
		
		//filling the palette, each point has an index
		List<Point> palette=tree.getColors(L2N_COLORS);
		int numberOfColors=palette.size();
		for(int i=0;i<numberOfColors;i++){
			palette.get(i).setOneCoord(3,i);
		}
		
		if(beautiful){
			System.out.println(palette);
			//lloyd algorithm to have better colors
			palette=lloyd(palette,listOfPoints,5);
			System.out.println(palette);
		}
		
		//creating a Kdtree from palette without taking the index dimension into account
		KdTree tree_palette=new KdTree(palette,3,L2N_COLORS+1);
		Point p=new Point(0,0,0);
		Point g;
		
		int ig=0;
		byte index[]=new byte[image.getWidth()*image.getHeight()];
		//new list of pixels, each point is replaced by the index of his closest in the palette
		for(int i=0;i<image.getHeight();i++){
			for(int j=0;j<image.getWidth();j++){
				
				pixelColor=new Color(image.getRGB(j, i));
				
				R=pixelColor.getRed();
				G=pixelColor.getGreen();
				B=pixelColor.getBlue();
				
				p.setCoord(R,G,B);
				g=tree_palette.getNearestNeighbor(p);//g is the closest to p in the palette
				ig=g.getCoord(3);//getting index of g in the palette
				index[i*image.getWidth()+j]=(byte)ig;//putting the index of g in index list
			}
		}
		//Creating a new compressed BufferedImage
		byte r[]=new byte[numberOfColors];
		byte v[]=new byte[numberOfColors];
		byte b[]=new byte[numberOfColors];
		
		for(int ind=0;ind<numberOfColors;ind++){
			for(int j=0;j<numberOfColors;j++){				
				if(palette.get(j).getCoord(3)==ind){
					
					r[ind]=(byte) palette.get(j).getCoord(0);
					v[ind]=(byte) palette.get(j).getCoord(1);
					b[ind]=(byte) palette.get(j).getCoord(2);
					break;
				}
				
			}
		}
		DataBufferByte dataBuffer = new DataBufferByte(index, image.getWidth()*image.getHeight());
		WritableRaster raster=Raster.createPackedRaster(dataBuffer,image.getWidth(),image.getHeight(),8,null);
		IndexColorModel cm=new IndexColorModel(8,numberOfColors,r,v,b);
		BufferedImage compressedImage=new BufferedImage(cm,raster,true,null);
		
		System.out.println("Compression successfull");
		
		return compressedImage;
		
		
	}
	
	/**
	 * Implementation of the lloyd algorithm,  points of palette are going to converge
	 * towards a local optimal solution that represent the image with the smallest error. 
	 * @param palette an initial list of Point, isn't modified
	 * @param listOfPoints list of Point of the image,
	 * @param iterMax the number of iteration
	 * @return a smoothed palette
	 */
	private static List<Point> lloyd(List<Point> palette, List<Point> listOfPoints, int iterMax) {
		List<Point> optimalColors=new ArrayList<Point>();
		
		int j=0;
		//copy of palette
		for(Point p : palette){
			optimalColors.add(new Point(p));
			optimalColors.get(j).setOneCoord(3,j++);
		}
		
		for(int i=0;i<iterMax;i++){
			//cluster is always reinitialized
			List<List<Point>> clusters=new ArrayList<List<Point>>();
			for(int k=0;k<optimalColors.size();k++){
				clusters.add(new ArrayList<Point>());
			}
		
			centersToClusters(clusters,optimalColors,listOfPoints);//clusters is modified here
			clustersToCenters(clusters,optimalColors,listOfPoints);//optimalColors is modified here
		}
		
		return optimalColors;
		
	}
	/**
	 * Function that changes the Points of optimalColor to be closer to the solution
	 * @param clusters isn't modified
	 * @param optimalColors is modified
	 * @param listOfPoints isn't modified
	 */
	private static void clustersToCenters(List<List<Point>> clusters,
			List<Point> optimalColors, List<Point> listOfPoints) {
		
		for(int i=0;i<clusters.size();i++){
			//for each cluster, computation of the center of gravity 
			Point cog=barycentre(optimalColors.get(i),clusters.get(i),3);
			//optimalColor[i] is replaced by the center of gravity
			for(int j=0;j<cog.getCoords().length;j++){
				optimalColors.get(i).setOneCoord(j,cog.getCoord(j));
			}
		}	
	}
	
	/**
	 * Function that computes the center of gravity of a list
	 * @param pi an initial point
	 * @param list is the data for which we need to  compute the center of gravity
	 * @param dim the number of dimension we should consider 
	 * @return the center of gravity of list 
	 */
	private static Point barycentre(Point pi,List<Point> list,int dim) {
		
		int coords[]=new int[dim];
		//computation of the means for each dimension
		for(int i=0;i<dim;i++){
			coords[i]=0;
			for(Point q : list){
				coords[i]+=q.getCoord(i);
			}
			coords[i]/=list.size();
		}
		return new Point(coords);
	}
	
	/**
	 * Function that makes "clusters[i]" containing same points as the voronoi cell around the
	 * point centers[i]  
	 * @param clusters is modified
	 * @param optimalColors is modified
	 * @param listOfPoints isn't modified, list of points of the image
	 */
	private static void centersToClusters(List<List<Point>> clusters,
			List<Point> centers, List<Point> listOfPoints) {
		//creation of a tree containing the centers for a faster search
		KdTree centersTree=new KdTree(centers,3,1000);
		for(Point p : listOfPoints){
			//cluster[i] is filled with the Points of the voronoi cell of centers[i]
			clusters.get(centersTree.getNearestNeighbor(p).getCoord(3)).add(p);
		}
		
	}
	
}
	
