package kdtree;
import java.util.ArrayList;
import java.util.List;

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
			private int direction;
			private Point point;
		
			public KdNode( KdNode filsGauche , KdNode filsDroit , Point point , int direction ){
				this.filsDroit = filsDroit;
				this.filsGauche = filsGauche;
				this.direction = direction;
				this.point = point;
			}
			
			public KdNode(Point point){
        
				this.direction=-1; 
				this.filsDroit=null;
				this.filsGauche=null;
				this.point = point;
  
			}
			
			
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
	
	public KdNode racine;


	//Tri de la liste par tri/fusion
	private List<Point> tri( List<Point> listePoints , int direction ) {
		int size = listePoints.size();
		if( size <= 1) {
			return listePoints;
		}
		else {
			List<Point> listeGauche = listePoints.subList(0, size/2-1);
			List<Point> listeDroite = listePoints.subList(size/2, size-1);
			return fusion( tri(listeGauche , direction) , tri(listeDroite , direction) , direction);
		}
	}
	private List<Point> fusion(List<Point> listeA, List<Point> listeB , int direction) {
		
		if( listeA == null ) {
			return listeB;
		}
		if( listeB == null ) {
			return listeA;
		}
		if( listeA.get(0).coord[direction] <= listeB.get(0).coord[direction] ) {
			List<Point> ret = new ArrayList<Point>();
			ret.add(listeA.get(0));
			if(listeA.size() > 1) {
				ret.addAll(fusion(listeA.subList(1, listeA.size()-1) , listeB , direction));
			}
			else {
				ret.addAll(fusion( null , listeB , direction));
			}
			
			return ret;
		}
		else {
			List<Point> ret = new ArrayList<Point>();
			ret.add(listeB.get(0));
			if(listeB.size() > 1) {
				ret.addAll(fusion(listeA , listeB.subList(1, listeB.size()-1) , direction));
			}
			else {
				ret.addAll(fusion(listeA , null , direction));
			}
			return ret;
		}
	}
	
	private Point mediane( List<Point> listePointsTriee , int direction) {
		if(listePointsTriee.size() > 0) {
			if(listePointsTriee.size()%2 != 0) {
				return listePointsTriee.get(listePointsTriee.size()/2);
			}
			return listePointsTriee.get(listePointsTriee.size()/2-1);
		}
		else {
			return listePointsTriee.get(0);
		}
	}
	
	private KdNode createKdTree (List<Point> listePoints , int k , int profondeur) {
		if( listePoints.isEmpty() ) {
			return null;
		}
		
		int direction = profondeur%k;
		
		List<Point> listePointsTriee = tri(listePoints , direction);
		int size = listePointsTriee.size();
		
		Point point = mediane(listePointsTriee , direction);
		if(listePointsTriee.size() > 2) {
			KdNode filsGauche = createKdTree(listePointsTriee.subList(0, listePointsTriee.indexOf(point)-1) , k , profondeur+1);
			KdNode filsDroit = createKdTree(listePointsTriee.subList(listePointsTriee.indexOf(point)+1 , size-1) , k , profondeur+1);
			KdNode res = new KdNode( filsGauche, filsDroit, point, direction);
			return res;
		}
		else if (listePointsTriee.size() == 2){
			KdNode filsGauche = null;
			KdNode filsDroit = createKdTree(listePointsTriee.subList(listePointsTriee.indexOf(point)+1 , size-1) , k , profondeur+1);
			KdNode res = new KdNode( filsGauche, filsDroit, point, direction);
			return res;
		}
		else {
			KdNode filsDroit = null;
			KdNode filsGauche = null;
			KdNode res = new KdNode( filsGauche, filsDroit, point, direction);
			return res;
		}
	}
	
	
	public KdTree( List<Point> listePoints , int k , int profondeur) {
		this.racine = createKdTree( listePoints , k , profondeur );
	}
	
	private KdNode algoRecherche( KdNode noeudDepart, int coordCherch , int direction) {
		if( noeudDepart.point.coord[direction] == coordCherch ) {
			return noeudDepart;
		}
		else if(coordCherch > noeudDepart.point.coord[direction] && noeudDepart.filsDroit != null) {
			System.out.println("gyg1");
			algoRecherche(noeudDepart.filsDroit , coordCherch , direction);
		}
		else if(coordCherch < noeudDepart.point.coord[direction] && noeudDepart.filsGauche != null) {
			System.out.println("gy2");
			algoRecherche(noeudDepart.filsGauche , coordCherch , direction);
		}
		else if(noeudDepart.isTerminal() ) {
			System.out.println("gyg");
			return null;
		}
		System.out.println("Cas non géré");
		return null;
	}
	
	public boolean recherche(int coordCherch , int direction) {
		return (algoRecherche(this.racine , coordCherch, direction)!=null);
	}
//	public void addNode( Point point ) {
//	}
	
	// TODO : Plus tard
//	public void removeNode ( Point point ) {
//		
//	}
	public int distsq(KdNode n1,KdNode n2){
		
		int res=0;
		for(int i=0;i<n1.point.dim;i++){
			
			res+=(n1.point.coord[i]-n2.point.coord[i])*(n1.point.coord[i]-n2.point.coord[i]);
		}
		return res;
	}
	

	public Point getNearestNeighbor( Point point ) {
		
		
		KdNode startNode=new KdNode(point);
		//Int distance=new Int(distsq(racine,startNode));
		
		return getNearestNeighbor(racine,startNode).point;
		
	} 
		
	private KdNode getNearestNeighbor(KdNode pere,KdNode node/*, Int distance*/){
		
		/*int D=distsq(pere,node);//distance minimale juste comme ca pas necessaire si on s'arrete toujours au fond
		if(distance.valeur>D){
			distance.valeur=D;
		}
		*/
		if(pere.isTerminal()){
			return pere;
		}
		int d=pere.point.coord[pere.direction]-node.point.coord[pere.direction];//distance par rapport a l'hyperplan
		if(d>0){
			
			if(pere.filsDroit!=null && distsq(node,pere.filsDroit)<=d*d){
			//si le fils est plus proche du point que la limite de l'hyperplan
				
				return getNearestNeighbor(pere.filsDroit,node);
			}
			else if(pere.filsDroit==null){
			//fils gauche n'est pas null car pere n'est pas terminal
				return getNearestNeighbor(pere.filsGauche,node);
			}
			else{
			//sinon on doit regarder des deux cotes et on retourne le plus proche
				
				KdNode a=getNearestNeighbor(pere.filsDroit,node);
				KdNode b=getNearestNeighbor(pere.filsGauche,node);
				
				if(distsq(a,node)<distsq(b,node)){
					return a;
				}
				else return b;
			}
		}
		else{
			if(pere.filsGauche!=null && distsq(node,pere.filsGauche)<=d*d){
			//si le fils est plus proche du point que la limite de l'hyperplan
				
				return getNearestNeighbor(pere.filsGauche,node);
			}
			else if(pere.filsGauche==null){
				
				return getNearestNeighbor(pere.filsDroit,node);
			}
			else{
				
				KdNode a=getNearestNeighbor(pere.filsDroit,node);
				KdNode b=getNearestNeighbor(pere.filsGauche,node);
				
				if(distsq(a,node)<distsq(b,node)){
					return a;
				}
				else return b;
			}
		}
		
	}
		
	
}
