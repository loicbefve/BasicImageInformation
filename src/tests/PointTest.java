package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import kdtree.Point;

public class PointTest {
	
	private static Point testPoint;
	private static int listeTestPoint[] = {0,1000000,-1000000,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,1};
	private static int sizeTestPoint = listeTestPoint.length;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		testPoint = new Point( listeTestPoint );
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
		assertTrue("Zéro" , testPoint.getCoord(0) == listeTestPoint[0]);
		assertTrue("1000000" , testPoint.getCoord(1) == listeTestPoint[1]);
		assertTrue("-1000000" , testPoint.getCoord(2) == listeTestPoint[2]);
		assertTrue("Grande dimension" , testPoint.getCoord(sizeTestPoint-1) == listeTestPoint[sizeTestPoint-1]);
	}
	
	@Test
	public void testGetCoords() {
		int resTrouvee[] = testPoint.getCoords();
		for( int i = 0 ; i<sizeTestPoint ; i++) {
			assertTrue("Egalité resTheo et resTrouvee" , resTrouvee[i] == listeTestPoint[i]);
		}
	}

	@Test
	public void testSetCoord() {
		int listeNewPoint[] = {100,1,-1000000,1,2,3,-4,5,6,7,-8,9,1,2,3,-4,5,6,5787,8,9,1,92,3,4,5698,6,7,68,9,100000};
		int sizeListeNewPoint = listeNewPoint.length;
		
		testPoint.setCoord(listeNewPoint);
		int resTrouvee[] = testPoint.getCoords();
		
		for( int i = 0 ; i<sizeListeNewPoint ; i++) {
			assertTrue("Egalité resTheo et resTrouvee" , resTrouvee[i] == listeNewPoint[i]);
		}
	}

	@Test
	public void testSetOneCoord() {
		int testValue;
		int testPos;
		
		//Test1
		testValue = -100000;
		testPos = 6;
		testPoint.setOneCoord(testPos, testValue);
		assertTrue("Grande valeur négative" , testPoint.getCoord(testPos) == testValue);
		
		//Test2:
		testValue = 100000;
		testPos = 6;
		testPoint.setOneCoord(testPos, testValue);
		assertTrue("Grande valeur positive" , testPoint.getCoord(testPos) == testValue);
		
		//Test3:
		testValue = 0;
		testPos = 6;
		testPoint.setOneCoord(testPos, testValue);
		assertTrue("Valeur Zéro" , testPoint.getCoord(testPos) == testValue);
		
		//Test4:
		testValue = 123;
		testPos = 0;
		testPoint.setOneCoord(testPos, testValue);
		assertTrue("Position zéro" , testPoint.getCoord(testPos) == testValue);
		
		//Test5:
		testValue = 123;
		testPos = sizeTestPoint-1;
		testPoint.setOneCoord(testPos, testValue);
		assertTrue("Dernière position" , testPoint.getCoord(testPos) == testValue);
		
		//Test6:
		for ( int i=0 ; i<100 ; i++) {
			testValue = Integer.MIN_VALUE + (int)(Math.random() * ((Integer.MAX_VALUE - Integer.MIN_VALUE) + 1));
			testPos = (int)(Math.random() * sizeTestPoint);
			testPoint.setOneCoord(testPos, testValue);
			assertTrue("100 valeurs et positions aléatoires" , testPoint.getCoord(testPos) == testValue);
		}
		
		
		
	}

	@Test
	public void testDistsq() {
		
	}

}
