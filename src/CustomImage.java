import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
 * </ul>
 * 
 *	Each pixel is an index to a given color in the color list.
 *</p>
 *	
 */

public class CustomImage {
	/**
	 * 
	 * Public function which writes the data of a BufferedImage following the cst protocole
	 * 
	 * @param image : BufferedImage that contains the cst image to write in filename
	 * @param filename 
	 */
	public static void write(BufferedImage image,String filename){
		
		File fichier=new File(filename);
		
		try(FileWriter fw = new FileWriter(fichier)){
			
			//on reccupere les donnees necessaires
			IndexColorModel cm=(IndexColorModel)image.getColorModel();
			int msize=cm.getMapSize();
			int psize=cm.getPixelSize();
			int[] palette=new int[msize];
			cm.getRGBs(palette);
			
			//on ecrit l'entete : nombre magique, dimensions, nombre de bits par pixels, palette des couleurs
			fw.write("P10\n");
			fw.write(image.getWidth()+" "+image.getHeight()+"\n");
			fw.write(Integer.toString(psize)+"\n");
			
			for(int i=0;i<msize-1;i++){
				fw.write(Integer.toHexString(palette[i]&0x00FFFFFF)+" ");	
			}
			fw.write(Integer.toHexString(palette[msize-1]&0x00FFFFFF));
			fw.write("\n");
			
			//on ecrit les pixels
			for(int i=0;i<image.getHeight();i++){
				for(int j=0;j<image.getWidth();j++){
					
					fw.write(Integer.toHexString(Quantification.getIndex(palette, image.getRGB(j,i))));
				}
				
			}
			
			fw.close();
			System.out.println("Writing done");
		
		}
		catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Public function that read a file with a given name filename and return a compatible BufferedImage
	 * @param filename
	 * @return a BufferedImage that represent the image stored in the file named filename
	 */
	public static BufferedImage read(File filename){
		
		String nm="",pal="",dims="",pixel_size="";
		
		try{
			
			InputStream flux=new FileInputStream(filename); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			
			nm=buff.readLine();
			ImageFormatException e=new ImageFormatException("Mauvais format d'image! Id="+nm+", Expected Id= P10" );
			if(!nm.equals("P10")){
				buff.close(); 
				lecture.close();
				flux.close();
				throw e;
			}

			dims=buff.readLine();
			String[] wh=dims.split(" ");
			int w=Integer.parseInt(wh[0]);
			int h=Integer.parseInt(wh[1]);
			
			pixel_size=buff.readLine();
			int ps=Integer.parseInt(pixel_size);
			
			pal=buff.readLine();
			String[] palette=pal.split(" ");
		
			byte[] pixels = new byte[w*h];
			int a=' ',z=0;
			while((a=buff.read())!=-1){//si a=-1 on arrive a la fin du fichier
				
				pixels[z++]=chartoByte((char)a);
			}
	
			buff.close(); 
			lecture.close();
			flux.close();
			
		byte r[]=new byte[palette.length];
		byte v[]=new byte[palette.length];
		byte b[]=new byte[palette.length];
		
		int c;
		for(int i=0;i<palette.length;i++){
		//on prepare les donnees du color model
			c=Integer.parseInt(palette[i],16);
			r[i]=(byte) ((c& 0xFF0000)>>16);
			v[i]=(byte) ((c& 0x00FF00)>>8);
			b[i]=(byte) (c& 0x0000FF);
		}
		
		DataBufferByte dataBuffer = new DataBufferByte(pixels, w*h);
		WritableRaster raster=Raster.createPackedRaster(dataBuffer,w,h,ps,null);
		IndexColorModel cm=new IndexColorModel(ps,palette.length,r,v,b);
		BufferedImage image2=new BufferedImage(cm,raster,true,null);
		
		System.out.println("Reading done");
		
		return image2;
		
		}		
		catch (Exception e){
			System.out.println(e);
			return null;	
		}
	}
	/**
	 *  Private fontion that transforms a char into an hexadecimal value, poorly writed 
	 * @param c
	 * @return the associated byte
	 */
	private static byte chartoByte(char c) {
	//TODO si quelqu'un trouve une meilleure solution je suis preneur 
		switch(c){
		case 'f':
			return 15;
		case 'e':
			return 14;
		case 'd':
			return 13;
		case 'c':
			return 12;
		case 'b':
			return 11;
		case 'a':
			return 10;
		default:
				
			return (byte)(c-'0');
		}
		
		
	}
		
	
}
