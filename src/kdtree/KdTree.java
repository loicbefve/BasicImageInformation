package kdtree;

public class KdTree {
	
	private class Int{
		private int valeur;
		public Int(int v){
			valeur=v;
		}
	}
	private class KdNode {
			
			private KdNode filsDroit;
			private KdNode filsGauche;
			private int dimension;
			private Point point;
			// TODO créer classe point
			
			public KdNode(Point point){
				
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
	public int distsq(KdNode n1,KdNode n2){
		
		int res=0;
		for(int i=0;i<n1.point.dim;i++){
			
			res+=(n1.point.coords[i]-n2.point.coords[i])*(n1.point.coords[i]-n2.point.coords[i]);
		}
		return res;
	}
	
	public Point getNearestNeighbor( Point point ) {
		
		
		KdNode startNode=new KdNode(point);
		Int distance=new Int(distsq(racine,startNode));
		
		return getNearestNeighbor(racine,startNode,distance).point;
		
	} 
		
	private KdNode getNearestNeighbor(KdNode pere,KdNode node, Int distance){
		
		int D=distsq(pere,node);
		if(distance.valeur>D){
			distance.valeur=D;
		}
		
		if(pere.isTerminal()){
			return pere;
		}
		int d=pere.point.coords[pere.dimension]-node.point.coords[pere.dimension];
		if(d>0){
			
			if(distsq(node,pere.filsDroit)<=d*d){//si le fils est plus proche du point que la limite de l'hyperplan
				
				return getNearestNeighbor(pere.filsDroit,node,distance);
			}
			else{
				
				KdNode a=getNearestNeighbor(pere.filsDroit,node,distance);
				KdNode b=getNearestNeighbor(pere.filsGauche,node,distance);
				
				if(distsq(a,node)<distsq(b,node)){
					return a;
				}
				else return b;
			}
		}
		else{
			if(distsq(node,pere.filsDroit)<=d*d){//si le fils est plus proche du point que la limite de l'hyperplan
				
				return getNearestNeighbor(pere.filsGauche,node,distance);
			}
			else{
				
				KdNode a=getNearestNeighbor(pere.filsDroit,node,distance);
				KdNode b=getNearestNeighbor(pere.filsGauche,node,distance);
				
				if(distsq(a,node)<distsq(b,node)){
					return a;
				}
				else return b;
			}
		}
		
	}
		
	
	
}
