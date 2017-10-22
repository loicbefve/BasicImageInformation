package kdtree;

import java.util.ArrayList;
import java.util.List;


public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Point p1 = new Point(1,2,3);
		Point p2 = new Point(12,3,2);
		Point p3 = new Point(9,7,30);
		Point p4 = new Point(1,3,2);
		List<Point> li = new ArrayList<Point>();
		li.add(p1);
		li.add(p2);
		li.add(p3);
		li.add(p4);
		@SuppressWarnings("unused")
		KdTree tree = new KdTree( li , 2 , 0);
	}

}
