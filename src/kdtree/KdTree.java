package kdtree;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
	
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
	
	//Tri de la liste par tri/fusion
	private List<Point> tri( List<Point> listePoints , int direction ) {
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
		KdNode res = new KdNode(point);
		res.direction = direction;
		if(listePointsTriee.size() > 2) {
			//les listes étant triees on sait qu'elles sont du bon cote
			res.filsGauche = createKdTree(listePointsTriee.subList(0, listePointsTriee.indexOf(point)) , k , profondeur+1);
			res.filsDroit = createKdTree(listePointsTriee.subList(listePointsTriee.indexOf(point)+1 , size) , k , profondeur+1);
			
			return res;
		}
		else if (listePointsTriee.size() == 2){
			res.filsGauche = null;
			res.filsDroit = createKdTree(listePointsTriee.subList(listePointsTriee.indexOf(point)+1 , size) , k , profondeur+1);
			return res;
		}
		else {
			res.filsDroit = null;
			res.filsGauche = null;
			return res;
		}
	}
	
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
		return getNearestNeighbor(racine,startNode).point;
		
	} 
		
	private KdNode getNearestNeighbor(KdNode pere,KdNode node){
		
		if(pere.isTerminal()){
			return pere;
		}

		int d=pere.point.coord[pere.direction]-node.point.coord[pere.direction];//distance par rapport a l'hyperplan

		if(d>0){//si la distance est positive on part a droite le plus souvent
			
			if(pere.filsDroit!=null && distsq(node,pere.filsDroit)<=d*d){
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
				
				if(distsq(a,node)<distsq(b,node)){
					return a;
				}
				else return b;
			}
		}
		else{//meme chose a gauche
			if(pere.filsGauche!=null && distsq(node,pere.filsGauche)<=d*d){
				
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
				
				if(distsq(a,node)<distsq(b,node)){
					return a;
				}
				else return b;
			}
		}
		
	}

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
		
	
}
