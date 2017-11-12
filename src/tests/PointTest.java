package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import kdtree.Point;

public class PointTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testPoint() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoord() {
		Point TestPoint = new Point( 0 ,  1000000 , -1000000 , 1 , 2 , 3 , 4 , 5 , 6 , 7 , 8 , 9 , 10 , 11 , 12 , 13 );
		assertTrue("Zéro" , TestPoint.getCoord(0) == 0);
		assertTrue("1000000" , TestPoint.getCoord(1) == 1000000);
		assertTrue("-1000000" , TestPoint.getCoord(2) == -1000000);
		assertTrue("Grande dimension" , TestPoint.getCoord(15) == 13);
		
		Point TestPointOne = new Point(1);
		assertTrue("Point à une seul coordonée" , TestPointOne.getCoord(0) == 1);
	}
	
	

	@Test
	public void testGetCoords() {
		Point TestPoint = new Point( 0 ,  1000000 , -1000000 );
		int[] res = {0 , 1000000 , - 1000000};
		assertTrue("Point = [0 , 1000000 , -1000000]" , TestPoint.getCoords() == res);
	}

	@Test
	public void testSetCoord() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetOneCoord() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDistsq() {
		fail("Not yet implemented");
	}

}
