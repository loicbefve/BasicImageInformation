package kdtree;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * 
 * <p>KdTree is a binary search tree of n dimensions
 * <ul>*
 * <li>A KdTree only contains a KdNode, it is the root of the tree</li>
 * <li>KdNode is a private class, each vector is stored in a KdNode</li>
 * </ul>
 * </p>
 *
 */
public class KdTree {
	/**
	 * 
	 * <p>Each vector is stored in a Kdnode, a Kdnode is linked to at most 2 others Kdnodes, 
	 * they are used to order the vector along one dimension.
	 *</p>
	 */
	private class KdNode {
			
			private KdNode filsDroit;
			private KdNode filsGauche;
			private int direction;
			Point point;
			
			public String toString() {
				String res="p:"+this.point;
				if(this.direction != -1) {
					res+= "Di:"+this.direction;
				}
				return res;
			}
			
			public KdNode(Point point){
        
				this.direction=-1; 
				this.filsDroit=null;
				this.filsGauche=null;
				this.point = point;
  
			}
			
			/**
			 * This public method returns if the node is terminal or not
			 *
			 * @return      a boolean true:terminal , false:not terminal
			 */
			public boolean isTerminal(){
				
				return (filsDroit==null && filsGauche==null);
			}
			
		}
	
	public KdNode racine;

	public String toString(){
		
		int profondeur=4;
		int parcours=0;
		String chemin="";
		boolean n=false;
		
		for(int p=0;p<profondeur;p++){
		//on affiche les profondeurs une à une
			parcours=0;
			KdNode r=racine;
			String str="";
			for(int j=0;j<(1<<p);j++){
			//pour les 2^p noeuds
				for(int i=0;i<p;i++){
					if((parcours & (1<<i))==0){
					//permet de faire toutes les possibilités
						if(r.filsDroit==null){
							n=true;
						}
						else{
							r=r.filsDroit;
							chemin+="D";
						}
					}
					else{
						if(r.filsGauche==null){
							n=true;
						}
						else{
							r=r.filsGauche;
							chemin+="G";
						}
					}
					str+="\t\t";
				}
				System.out.print(str);
				if(n){
					n=false;
					System.out.println(chemin+": null");
				}
				else{
					System.out.println(chemin+": "+r);
				}
				r=racine;
				str="";
				parcours++;
				chemin="";
			}
		}
		String res="";
		return res;
	}
	
	/**
	 * Private function which returns the list of points given but 
	 * sorted by the direction given.
	 *
	 * @param List<Point> listePoints: the list of point to be sorted 
	 * @param int direction: an integer representing the coordinate of the point
	 * which is use to sort the list
	 * @return List<Point> ret the initial list of point sorted by direction
	 * this function call the private function fusion recursively. 
	 */
	private List<Point> tri( List<Point> listePoints , int direction ) {
		//Tri de la liste par tri/fusion
		int size = listePoints.size();
		if( size <= 1) {
			return listePoints;
		}
		else {
			List<Point> listeGauche = listePoints.subList(0, size/2);
			List<Point> listeDroite = listePoints.subList(size/2, size);
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
				ret.addAll(fusion(listeA.subList(1, listeA.size()) , listeB , direction));
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
				ret.addAll(fusion(listeA , listeB.subList(1, listeB.size()) , direction));
			}
			else {
				ret.addAll(fusion(listeA , null , direction));
			}
			return ret;
		}
	}
	/**
	 * Private function which returns the median point of a list of point sorted
	 * by a direction.
	 *
	 * @param List<Point> listePointsTriee: the list of sorted points where we want the median
	 * @param int direction: an integer representing the coordinate of the point that we have interest for
	 * @return Point: the median point of the sorted list
	 */
	private Point mediane( List<Point> listePoints , int direction) {
		//TODO: Refaire la doc
		final int dir = direction;
		listePoints.sort(new Comparator<Point>() {
		    @Override
		    public int compare(Point p1, Point p2) {
		        if(p1.getCoord(dir) > p2.getCoord(dir)){
		            return 1;
		        }
		        else if(p1.getCoord(dir) < p2.getCoord(dir)) {
		        	return -1;
		        }
		        return 0;
		     }
		});
		
		if(listePoints.size() > 0) {
			if(listePoints.size()%2 != 0) {
				return listePoints.get(listePoints.size()/2);
			}
			return listePoints.get(listePoints.size()/2-1);
		}
		else {
			return listePoints.get(0);
		}
	}
	/**
	 * Private recursive function which create a k-d tree
	 *
	 * @param List<Point> listePoints: the list of point of the k-d tree 
	 * @param int k: the dimension k of the k-d tree
	 * @param int profondeur: the stage that we are creating
	 * @return KdNode: the root of the created k-d tree 
	 */
	private KdNode createKdTree (List<Point> listePoints , int k , int profondeur) {
		
		int size = listePoints.size();
		final int direction = profondeur%k;
		
		if( listePoints.isEmpty() ) {
			return null;
		}
		
		Point point = mediane(listePoints , direction);
		KdNode res = new KdNode(point);
		res.direction = direction;
		
		if(size > 2) {
			//les listes étant triees on sait qu'elles sont du bon cote
			res.filsGauche = createKdTree(listePoints.subList(0, listePoints.indexOf(point)) , k , profondeur+1);
			res.filsDroit = createKdTree(listePoints.subList(listePoints.indexOf(point)+1 , size) , k , profondeur+1);
			return res;
		}
		else if (size == 2){
			res.filsGauche = null;
			res.filsDroit = createKdTree(listePoints.subList(listePoints.indexOf(point)+1 , size) , k , profondeur+1);
			return res;
		}
		else {
			res.filsDroit = null;
			res.filsGauche = null;
			return res;
		}
	}
	/**
	 * Constructor of the class k-d tree
	 *
	 * @param List<Point> listePoints: the list of point of the k-d tree 
	 * @param int k: the dimension k of the k-d tree
	 */
	public KdTree( List<Point> listePoints , int k ) {
		
		this.racine = createKdTree( listePoints , k , 0 );
	}
	
	private KdNode algoRecherche( KdNode noeudDepart, KdNode noeudCherch ) {
		int direction = noeudDepart.direction;
		if( noeudDepart.point == noeudCherch.point ) {
			return noeudDepart;
		}
		else if(noeudCherch.point.coord[direction] > noeudDepart.point.coord[direction] && noeudDepart.filsDroit != null) {
			algoRecherche(noeudDepart.filsDroit , noeudCherch);
		}
		else if(noeudCherch.point.coord[direction] < noeudDepart.point.coord[direction] && noeudDepart.filsGauche != null) {
			algoRecherche(noeudDepart.filsGauche , noeudCherch);
		}
		else if(noeudDepart.isTerminal() ) {
			return noeudDepart;
		}
		System.out.println("Cas non géré");
		return null;
	}
	
	public boolean recherche( Point point  ) {
		KdNode noeudCherch = new KdNode(point);
		KdNode noeudParent = algoRecherche(this.racine , noeudCherch);
		System.out.println("Parent:"+noeudParent.point+" Point:"+point);
		return noeudParent.point == point;
	}
	
	public void addNode( Point point ) {
		KdNode noeudToAdd = new KdNode(point);
		KdNode noeudParent = algoRecherche(this.racine , noeudToAdd);
		int direction = noeudParent.direction;
		if(point.coord[direction] < noeudParent.point.coord[direction]) {
			noeudParent.filsGauche = noeudToAdd;
		}
		else if(point.coord[direction] > noeudParent.point.coord[direction]) {
			noeudParent.filsDroit = noeudToAdd;
		}
	}
	
	// TODO : Plus tard
//	public void removeNode ( Point point ) {
	/**
	 * public function which find the nearest neighbor of the given point in the tree
	 *
	 * @param Point point: The point for which we search the nearest neighbor
	 * @return Point: The nearest neighbor of the Point point
	 */
	public Point getNearestNeighbor( Point point ) {
		
		KdNode startNode=new KdNode(point);
		return getNearestNeighbor(racine,startNode).point;
		
	} 
	/**
	 * Private function
	 *
	 * @param KdNode pere: a node of the tree
	 * @param KdNode node: the node that needs to find its nearest neighbor
	 */	
	private KdNode getNearestNeighbor(KdNode pere,KdNode node){
		
		if(pere.isTerminal()){
			return pere;
		}

		int d=pere.point.coord[pere.direction]-node.point.coord[pere.direction];//distance par rapport a l'hyperplan

		if(d>0){//si la distance est positive on part a droite le plus souvent
			
			if(pere.filsDroit!=null && node.point.distsq(pere.filsDroit.point)<=d*d){
			//si le fils est plus proche du point que la limite de l'hyperplan
			
				return getNearestNeighbor(pere.filsDroit,node);
			}
			else if(pere.filsDroit==null){
		
				return getNearestNeighbor(pere.filsGauche,node);
			}
			else if(pere.filsGauche==null){
			
				return getNearestNeighbor(pere.filsDroit,node);
			}
			else{
			//sinon on doit regarder des deux cotes et on retourne le plus proche
			
				KdNode a=getNearestNeighbor(pere.filsDroit,node);
				KdNode b=getNearestNeighbor(pere.filsGauche,node);
				
				if(a.point.distsq(node.point)<b.point.distsq(node.point)){
					return a;
				}
				else return b;
			}
		}
		else{//meme chose a gauche
			if(pere.filsGauche!=null && node.point.distsq(pere.filsGauche.point)<=d*d){
				
				return getNearestNeighbor(pere.filsGauche,node);
			}
			else if(pere.filsGauche==null){
				
				return getNearestNeighbor(pere.filsDroit,node);
			}
			else if(pere.filsDroit==null){
				
				return getNearestNeighbor(pere.filsGauche,node);
			}
			else{
				
				KdNode a=getNearestNeighbor(pere.filsDroit,node);
				KdNode b=getNearestNeighbor(pere.filsGauche,node);
				
				if(a.point.distsq(node.point)< b.point.distsq(node.point)){
					return a;
				}
				else return b;
			}
		}
		
	}
	/**
	 * Public fonction which cut a tree to a given depth
	 * 
	 * @param KdNode pere : at the first call it should be the root of the tree
	 * @param int prof : the number of layers we want to keep
	 */
	public void troncature(KdNode pere,int prof) {
		
		if(prof==0){
			if(pere==null){
				System.out.println("troncature ratée");
				return;
			}
			pere.filsDroit=null;
			pere.filsGauche=null;
			return;
		}
		
		else if(pere.filsGauche==null || pere.filsDroit==null){
			System.out.println("troncature ratée");
			
		}
		else{
			troncature(pere.filsDroit,prof-1);
			troncature(pere.filsGauche,prof-1);
			
		}
		
	}
	/**
	 * Public function which fill a given array with the values of the nodes.point
	 * for example if node.point.dim==3, points are hashed like this : 0x112233
	 * 
	 * @param int prof: number of the layer that is explored to fill the array
	 * @return an int[] filled with the values of the (prof)th layer of the tree
	 */
	public int[] remplissage(int prof){
		
		int palette[]=new int[1<<prof];
		KdNode n=racine;
		int parcours=0;int color=0;
		for(int j=0;j<(1<<prof);j++){
			for(int i=0;i<prof;i++){
				if((parcours & (1<<i))==0){
					n=n.filsDroit;
				}
				else{
					n=n.filsGauche;
				}
			}
			
			for(int k=n.point.dim-1;k>=0;k--){
				color|=(n.point.getCoord(n.point.dim-1-k)<<8*k);
				//chaque dimension est encodée sur 8bits, l'octet de poids fort correspond a la derniere dimension
			}
			palette[parcours]=color;
			parcours++;
			color=0;
			n=racine;
		}
		
		return palette;
	
	}
}