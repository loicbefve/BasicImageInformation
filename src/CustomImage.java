import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class CustomImage {
	
	public static void write(BufferedImage image,String filename){
		
		;
		
		File fichier=new File(filename);
		try(FileWriter fw = new FileWriter(fichier)){
			
			fw.write("P10\n");
			
			fw.write(image.getWidth()+" "+image.getHeight()+"\n");
			IndexColorModel cm=(IndexColorModel)image.getColorModel();
			int msize=cm.getMapSize();
			
			int psize=cm.getPixelSize();
			fw.write(Integer.toString(psize)+"\n");
			int[] palette=new int[msize];
			cm.getRGBs(palette);
			
			for(int i=0;i<msize-1;i++){
				fw.write(Integer.toHexString(palette[i]&0x00FFFFFF)+" ");	
			}
			fw.write(Integer.toHexString(palette[msize-1]&0x00FFFFFF));
			fw.write("\n");
			
			for(int i=0;i<image.getHeight();i++){
				for(int j=0;j<image.getWidth();j++){
					
					fw.write(Integer.toHexString(Quantification.getIndex(palette, image.getRGB(j,i))));
				}
				
			}
			
			fw.close();
			//fichier.close();
			System.out.println("done");
		
		}
		catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	
	public static BufferedImage read(File filename){
		
		String nm="",pal="",dims="",pixel_size="";
		
		try{
			
			
			InputStream flux=new FileInputStream(filename); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			
			
			nm=buff.readLine();
			
			dims=buff.readLine();
			String[] wh=dims.split(" ");
			int w=Integer.parseInt(wh[0]);
			int h=Integer.parseInt(wh[1]);
			pixel_size=buff.readLine();
			int ps=Integer.parseInt(pixel_size);
			pal=buff.readLine();
			String[] palette=pal.split(" ");
			
			
			char[] pixels = new char[w*h];
			int a=' ',z=0;
			while((a=buff.read())!=-1){
				
				pixels[z++]=(char)a;
				
			}
			
			System.out.println(pixels);
			buff.close(); 
			lecture.close();
			flux.close();
			
		
		
		
		
	
		byte r[]=new byte[palette.length];
		byte v[]=new byte[palette.length];
		byte b[]=new byte[palette.length];
		int c;
		for(int i=0;i<palette.length;i++){
			c=Integer.parseInt(palette[i],16);
			
			r[i]=(byte) ((c& 0xFF0000)>>16);
			v[i]=(byte) ((c& 0x00FF00)>>8);
			b[i]=(byte) (c& 0x0000FF);
			
		}
		
		byte index[]=new byte[pixels.length];
		for(int i=0;i<pixels.length;i++){
			
			index[i]=chartoByte(pixels[i]);
			
			
		}
		DataBufferByte dataBuffer = new DataBufferByte(index, w*h);
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

	private static byte chartoByte(char c) {
		switch(c){
		case 'f':
			return 16;
		case 'e':
			return 15;
		case 'd':
			return 14;
		case 'c':
			return 13;
		case 'b':
			return 12;
		case 'a':
			return 11;
		default:
				
		return (byte)(c-'0');
		}
		
		
	}
		
	
}
