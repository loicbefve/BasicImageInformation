package kdtree;
import java.util.List;

public class KdTree {
	
	private class KdNode {
			
			private KdNode filsDroit = null;
			private KdNode filsGauche = null;
			@SuppressWarnings("unused")
			private int direction;
			@SuppressWarnings("unused")
			private Point point;
			// TODO créer classe point
			
			public KdNode( KdNode filsGauche , KdNode filsDroit , Point point , int direction ){
				this.filsDroit = filsDroit;
				this.filsGauche = filsGauche;
				this.direction = direction;
				this.point = point;
			}
			
			@SuppressWarnings("unused")
			public boolean isTerminal(){
				return (filsDroit==null && filsGauche==null);
			}
			
//			public KdNode getFilsDroit() {
//				return this.filsDroit;
//			}
//			
//			public KdNode getFilsGauche() {
//				return this.filsGauche;
//			}
			
//			public int getDistance() {
//				return this.distance;
//			}
			
//			public Point getPoint() {
//				return this.point;
//			}
			
			// TODO fonction de distances
			
		}
	
	@SuppressWarnings("unused")
	private KdNode racine;

//TODO: Terminer l'implémentation de la fonction de tri pour passer par la médiane
//	//Tri de la liste par tri/fusion
//	private Point[] tri( Point listePoints[] , int direction ) {
//		int size = listePoints.length;
//		if( size <= 1) {
//			return listePoints;
//		}
//		else {
//			Point listeGauche[];
//			Point listeDroit[];
//			for( int i=0 ; i<size ; i++) {
//				if(i<= size/2) {
//					listeGauche[i] = listePoints[i];
//				}
//				else {
//					listeDroit[i-size/2] = listePoints[i];
//				}
//			}
//			return fusion( tri(listeGauche) , tri(listeDroit) , direction);
//		}
//	}
//	private Point[] fusion(Point listeA[], Point listeB[] , int direction) {
//		
//		if( listeA.length == 0 ) {
//			return listeB;
//		}
//		if( listeB.length == 0 ) {
//			return listeA;
//		}
//		if( listeA[1].coord[direction] <= listeB[1].coord[direction] ) {
//			return 
//		}
//	}
	
	private KdNode createKdTree (List<Point> listePoints , int k , int profondeur) {
		if( listePoints.isEmpty() ) {
			return null;
		}
		
		int direction = profondeur%k;
		
		int size = listePoints.size();
		Point point = listePoints.get(size/2);
		KdNode filsGauche = createKdTree(listePoints.subList(0, size/2) , k , profondeur+1);
		KdNode filsDroit = createKdTree(listePoints.subList(size/2+1 , size) , k , profondeur+1);
		KdNode res = new KdNode( filsGauche, filsDroit, point, direction);
		return res;
	}
	
	
	public KdTree( List<Point> listePoints , int k , int profondeur) {
		this.racine = createKdTree( listePoints , k , profondeur );
	}
	
	public void recherche ( KdNode noeud ) {
		
	}
	public void addNode( Point point ) {
	}
	
	// TODO : Plus tard
//	public void removeNode ( Point point ) {
//		
//	}
	
	public KdNode getNearestNeighbor( Point point ) {
		return null;
	}
	
}
