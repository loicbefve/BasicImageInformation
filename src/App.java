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
    
    
    Point p1 = new Point(1,2);
	Point p2 = new Point(3,30);
	Point p3 = new Point(1,7);
	Point p4 = new Point(6,3);
	
	List<Point> li = new ArrayList<Point>();
	for(int i=0;i<30;i++){
		li.add(new Point((int)(Math.random()*100),(int)(Math.random()*100)));
	}
	KdTree tree = new KdTree( li , 2 );
	System.out.println(tree);
//	boolean rech = tree.recherche( p1 );
    }
}
