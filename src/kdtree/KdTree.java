package kdtree;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
			
			private int distsq(Point point){
				return this.point.distsq(point);
			}
			
		}
	
	public KdNode racine;

	public String toString(){
		
		int profondeur=6;
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
	private KdNode createKdTree (List<Point> listePoints , int k , int profondeur,int pmax) {
		//TODO probleme de cas d'egalite a corriger, si on a plus des points egaux au niveau d'une mediane ca pose probleme
		int size = listePoints.size();
		final int direction = profondeur%k;
		
		if( listePoints.isEmpty() || profondeur==pmax) {
			return null;
		}
		
		Point point = mediane(listePoints , direction);
		KdNode res = new KdNode(point);
		res.direction = direction;
		
		if(size > 2) {
			//les listes étant triees on sait qu'elles sont du bon cote
			
			res.filsGauche = createKdTree(listePoints.subList(0, listePoints.indexOf(point)) , k , profondeur+1,pmax);
			res.filsDroit = createKdTree(listePoints.subList(listePoints.indexOf(point)+1 , size) , k , profondeur+1,pmax);
			
			return res;
		}
		else if (size == 2){
			res.filsGauche = null;
			res.filsDroit = createKdTree(listePoints.subList(listePoints.indexOf(point)+1 , size) , k , profondeur+1,pmax);
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
	public KdTree( List<Point> listePoints , int k,int pmax ) {
		//petit ajout qui permet de supprimer les doublons en temps linéaire cependant ce n'est surement pas la bonne méthode
		Set<Point> setPoints=new HashSet<Point>();
		setPoints.addAll(listePoints);
		listePoints=new ArrayList<Point>(setPoints);
		this.racine = createKdTree( listePoints , k , 0,pmax );
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
	
	public KdNode addNode( Point point,int k ) {
		
		KdNode noeudToAdd = new KdNode(point);
		KdNode noeudParent = algoRecherche(this.racine , noeudToAdd);
		int direction = noeudParent.direction;
		noeudToAdd.direction=(noeudParent.direction+1)%k;
		if(point.coord[direction] < noeudParent.point.coord[direction]) {
			noeudParent.filsGauche = noeudToAdd;
		}
		else if(point.coord[direction] > noeudParent.point.coord[direction]) {
			noeudParent.filsDroit = noeudToAdd;
		}
		return noeudParent;
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
		
		Integer distance=Integer.MAX_VALUE;
		return getNearestNeighbor(racine,point,distance);
		
	} 
	/**
	 * Private function
	 *
	 * @param KdNode pere: a node of the tree
	 * @param KdNode node: the node that needs to find its nearest neighbor
	 */	
	
	private Point getNearestNeighbor(KdNode pere,Point point_a_placer,Integer distance){
		
		if(pere.isTerminal()){
			return pere.point;
		}
		
		int dpere=pere.distsq(point_a_placer);
		distance=Math.min(dpere,distance);
		
		Point estimation1=null,estimation2=null,nearest=null;
		KdNode restant=null;
		//distance par rapport a l'hyperplan
		if(pere.filsGauche!=null && point_a_placer.getCoord(pere.direction)<pere.point.getCoord(pere.direction)){
			estimation1=getNearestNeighbor(pere.filsGauche,point_a_placer,distance);
			if(pere.filsDroit!=null){
				restant=pere.filsDroit;
			}
		}
		else if(pere.filsDroit!=null){
			estimation1=getNearestNeighbor(pere.filsDroit,point_a_placer,distance);
			if(pere.filsGauche!=null){
				restant=pere.filsGauche;
			}
		}
		
		if(restant!= null && Math.abs(pere.point.getCoord(pere.direction)-point_a_placer.getCoord(pere.direction))<distance){
			estimation2=getNearestNeighbor(restant,point_a_placer,distance);
		}
		
		if(estimation2!=null && estimation2.distsq(point_a_placer)<estimation1.distsq(point_a_placer)){
			nearest=estimation2;
		}
		else nearest=estimation1;
		
		if(dpere<nearest.distsq(point_a_placer)){
			nearest=pere.point;
		}
		
		return nearest;
		
			
		
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
	 * @brief try to fill a list with 2^maxLayer colors, it starts the harvesting of colors at the level maxLayer
	 *  if it is not enough, the search remade at maxLayer-1 etc
	 * @param maxLayer first search level
	 * @return a List containing the colors
	 */
	public List<Point> getColors(int maxLayer) {
		List<Point> result=new ArrayList<Point>();
		int hl=0;
		int nombreCouleursMax=1<<maxLayer;
		
		while(hl<=maxLayer && result.size()<nombreCouleursMax){
			getColors(result,racine,maxLayer,hl);
			hl++;
		}
		
		return result;
	}
	
	private List<Point> getColors(List<Point> result,KdNode pere,int maxLayer,int harvestLayer){
		if(maxLayer==harvestLayer && pere!=null){
			result.add(pere.point);
		}
		if(pere.filsDroit!=null){
			getColors(result,pere.filsDroit,maxLayer-1,harvestLayer);
			
		}
		if(pere.filsGauche!=null){
			getColors(result,pere.filsGauche,maxLayer-1,harvestLayer);
			
		}
		return result;
	}
	
	
	
	
	
	
	
	
}