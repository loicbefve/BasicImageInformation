import java.util.ArrayList;
import java.util.List;
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
    
	
	
	/* petit test des fonctions getNearest et Kdtree()
	List<Point> li = new ArrayList<Point>();
	for(int i=0;i<900;i++){
		li.add(new Point((int)(Math.random()*100),(int)(Math.random()*100),(int)(Math.random()*100)));
	}
	KdTree tree = new KdTree( li , 3 );
	System.out.println(tree);
	Point p=new Point((int)(Math.random()*100),(int)(Math.random()*100),(int)(Math.random()*100));
	System.out.println(p);
	System.out.println(tree.getNearestNeighbor(p));
	*/

    }
}
