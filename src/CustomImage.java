import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import sun.awt.image.ImageFormatException;

/**
 * <p>
 * This class contains static methods used to manage .cst images, cst images use the following encoding:
 * 
 * <ul>
 * <li>The first line is a magic number "P10"</li>
 * <li>The second line contains the dimensions : "WIDTH HEIGHT" </li>
 * <li>The third line contains the number of bits for each pixels</li>
 * <li>The fourth line contains the color list formated as : "0xRRGGBB 0xRRGGBB ..."</li>
 * <li>Each pixel is a byte that represents an index to a given color in the color list.</li>
 * <li>Note : A quantified image stored as a png is lighter than a quantified image stored as a cst</li>
 * </ul>
 *</p>
 *	
 */

public class CustomImage {
	/**
	 * Function which creates exceptions
	 * 
	 * @param message 
	 * @param classe 
	 * @param fonction 
	 * @param ligne 
	 * @return ImageFormatExpression with a StackTrace and a message
	 */
	private static ImageFormatException makeException(String message,String classe,String fonction,int ligne){
		
		ImageFormatException e=new ImageFormatException(message);
		StackTraceElement[] s= {new StackTraceElement(classe,fonction,null,ligne)};
		e.setStackTrace(s);
		
		return e;
	}
	/**
	 * 
	 * Public function which writes the data of a BufferedImage following the cst protocole
	 * 
	 * @param image : BufferedImage that contains the cst image to write in filename
	 * @param filename 
	 * @throws ImageFormatException,IOException 
	 */
	public static void write(BufferedImage image,String filename) throws ImageFormatException, IOException{
		
		
		
		try{
			File fichier=new File(filename);
			FileWriter fw = new FileWriter(fichier);
			//getting useful data
			IndexColorModel cm;
			try{
				cm=(IndexColorModel)image.getColorModel();
			}
			catch(Exception f){
				fw.close();
				String message="Wrong color model format";
				throw makeException(message,"CustomImage","write",62);
			}
			int msize=cm.getMapSize();
			int psize=cm.getPixelSize();
			int[] palette=new int[msize];
			cm.getRGBs(palette);
			
			//writing the header : magic number, dimensions, number of bits per pixels, palette of colors
			fw.write("P10\n");
			fw.write(image.getWidth()+" "+image.getHeight()+"\n");
			fw.write(Integer.toString(psize)+"\n");
			
			for(int i=0;i<msize-1;i++){
				fw.write(Integer.toHexString(palette[i]&0x00FFFFFF)+" ");	
			}
			fw.write(Integer.toHexString(palette[msize-1]&0x00FFFFFF));
			fw.write("\n");
			
			//writing pixels
			for(int i=0;i<image.getHeight();i++){
				for(int j=0;j<image.getWidth();j++){
					
					fw.write((byte)Quantification.getIndex(palette, image.getRGB(j,i)));
				}
				
			}
			
			fw.close();
			System.out.println("Writing done");
		
		}
		catch (IOException e) {
    		throw e;
    	}
	}

	/**
	 * Public function that read a cst file with a given name filename and return a compatible BufferedImage
	 * @param filename
	 * @return a BufferedImage that represent the image stored in the file named filename
	 * @throws FileNotFoundException,IOException, ImageFormatException
	 */
	public static BufferedImage read(File filename)throws FileNotFoundException,IOException, ImageFormatException{
		
		String nm="",pal="",dims="",pixel_size="";
		InputStream flux;
		
		//can throw FileNotFoundException
		flux=new FileInputStream(filename); 
		
		InputStreamReader reader=new InputStreamReader(flux);
		BufferedReader buff=new BufferedReader(reader);
		
		//can throw IOException
		nm=buff.readLine();
		
		if(nm==null || !nm.equals("P10")){
			//can throw IOException
			buff.close();reader.close();flux.close();
			String message="Wrong image format! found="+nm+", expected= P10";
			throw makeException(message,"CustomImage","read",122);
		}

		dims=buff.readLine();
		String[] wh=dims.split(" ");
		int w,h;
		try{
			assert wh.length==2 ;
			w=Integer.parseInt(wh[0]);
			h=Integer.parseInt(wh[1]);
		}
		catch(Exception e){
			buff.close();reader.close();flux.close();
			String message="Wrong width/height, found="+dims+", expected= int int";
			throw makeException(message,"CustomImage","read",134);
		}
		
		pixel_size=buff.readLine();
		
		int ps;
		try{
		ps=Integer.parseInt(pixel_size);
		}
		catch(NumberFormatException f){
			buff.close();reader.close();flux.close();
			String message="Wrong pixel size, found="+pixel_size+", expected= int";
			throw makeException(message,"CustomImage","read",149);
		}
		if(ps>8){
			buff.close(); reader.close();flux.close();
			String message="Pixel size too big! "+ps+">8bits, impossible reading";
			throw makeException(message,"CustomImage","read",149);
		}
		
		pal=buff.readLine();
		String[] palette=pal.split(" ");
		
		byte[] pixels = new byte[w*h];
		int a=0,z=0;
		while((a=buff.read())!=-1){//if a=-1,EOF
			
			try{pixels[z++]=(byte)a;}
			catch(ArrayIndexOutOfBoundsException f){
				buff.close(); reader.close();flux.close();
				String message="Too many pixels";
				throw  makeException(message,"CustomImage","read",169);
			}	
		}
	
		buff.close(); reader.close();flux.close();
			
		byte r[]=new byte[palette.length];
		byte v[]=new byte[palette.length];
		byte b[]=new byte[palette.length];
		
		int c=0;
		try{
			for(int i=0;i<palette.length;i++){
				//preparing color model data
				//can throw NumberFormatException
				c=Integer.parseInt(palette[i],16);
			
			
				r[i]=(byte) ((c& 0xFF0000)>>16);
				v[i]=(byte) ((c& 0x00FF00)>>8);
				b[i]=(byte) (c& 0x0000FF);
			}
		}
		catch(NumberFormatException f){
			String message="wrong format for a palette color expected=0xRRGGBB";
			throw  makeException(message,"CustomImage","read",187);
		}
		DataBufferByte dataBuffer = new DataBufferByte(pixels, w*h);
		WritableRaster raster=Raster.createPackedRaster(dataBuffer,w,h,ps,null);
		IndexColorModel cm=new IndexColorModel(ps,palette.length,r,v,b);
		BufferedImage image2=new BufferedImage(cm,raster,true,null);
		
		System.out.println("Reading done");
		
		return image2;
		
	}		
		
	
}