package kdtree;

public class KdTree {
	
	private class KdNode {
			
			private KdNode filsDroit;
			private KdNode filsGauche;
			private int dimension;
			private Point point;
			// TODO créer classe point
			
			public KdNode(){
				
			}
			
			public boolean isTerminal(){
				
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
	
	private KdNode racine;
	// TODO implement racine
	
	
	public KdTree() {
	}
	
	public void addNode( Point point ) {
	}
	
	// TODO : Plus tard
//	public void removeNode ( Point point ) {
//		
//	}
	
	public KdNode getNearestNeighbor( Point point ) {
	}
	
}
