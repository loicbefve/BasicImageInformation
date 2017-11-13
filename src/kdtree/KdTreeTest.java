package kdtree;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class KdTreeTest {

	@Test
	public void testGetNearest() {
		
		List<Point> pixels=new ArrayList<Point>();

		Point p1=new Point(15,35,0,10);
		Point p2=new Point(15,34,0,15);
		Point p3=new Point(80,35,75,0);
		Point p4=new Point(80,35,74,25);
		Point p5=new Point(18,45,50,30);
		pixels.add(p1);pixels.add(p2);pixels.add(p3);pixels.add(p4);pixels.add(p5);
		KdTree tree=new KdTree(pixels,3,10);
		
		assertTrue(tree.getNearestNeighbor(p1).equals(p1));
		
		Point p6=new Point(18,40,50);
		
		assertTrue(tree.getNearestNeighbor(p6).equals(p5));
		
		Point p7=new Point(80,35,74);
		
		assertTrue(tree.getNearestNeighbor(p7).equals(p4));
		
		Point p8=new Point(80,35,74,0);
		
		assertTrue(tree.getNearestNeighbor(p8).equals(p3));
		
		
	}
	@Test
	public void testAdd(){
		
		List<Point> pixels=new ArrayList<Point>();

		Point p1=new Point(15,35,0,10);
		pixels.add(p1);
		KdTree tree=new KdTree(pixels,3,10);
		
		assertFalse(tree.addNode(p1,3));
		
		Point p2=new Point(15,34,0,15);

		assertTrue(tree.addNode(p2,3));
		
		
	}
	

}
