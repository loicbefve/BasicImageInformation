import java.util.List;
import java.util.ArrayList;
import kdtree.*;
/**
 * Basic image processing application
 *
 */
public class App 
{
	//public static BufferedImage img; 
    public static void main( String[] args )
    {
		@SuppressWarnings("unused")
		ImageViewer mainWindow = new ImageViewer();
    
	
	
	//petit test des fonctions getNearest et Kdtree()
	List<Point> li = new ArrayList<Point>();
	for(int i=0;i<12;i++){
		//li.add(new Point((int)(Math.random()*100),(int)(Math.random()*100),(int)(Math.random()*100)));
		li.add(new Point(10,10,10));
		
	}
	//System.out.println(li.get(2)+" "+li.get(2)+" "+li.get(2).equals(li.get(1)));
	//KdTree tree = new KdTree( li , 3,1000 );
	/*
	System.out.println(li);
	Point p=new Point((int)(Math.random()*100),(int)(Math.random()*100),(int)(Math.random()*100));
	System.out.println(p);
	int[] d=new int[12];
	for(int i=0;i<12;i++){
		d[i]=(p.distsq(li.get(i)));
		System.out.printf("%d ",d[i]);
	}
	System.out.println();
	System.out.println(tree.getNearestNeighbor(p));
	*/

    }
}
