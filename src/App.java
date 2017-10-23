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
	li.add(p1);
	li.add(p2);
	li.add(p3);
	li.add(p4);
	KdTree tree = new KdTree( li , 2 );
	System.out.println(tree);
//	boolean rech = tree.recherche( p1 );
    }
}
