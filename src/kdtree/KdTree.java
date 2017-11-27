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
	 * <p>Each vector is stored in a Kdnode, a Kdnode is linked to 2 others Kdnodes (that can be null), 
	 * they are used to order the vector along one dimension.
	 *</p>
	 */
	private class KdNode {
			
			private KdNode rightChild;
			private KdNode leftChild;
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
				this.rightChild=null;
				this.leftChild=null;
				this.point = point;
  
			}
			
			/**
			 * This public method returns if the node is terminal or not
			 *
			 * @return      a boolean true:terminal , false:not terminal
			 */
			@SuppressWarnings("unused")
			public boolean isTerminal(){
				
				return (rightChild==null && leftChild==null);
			}
			
			
		}
	
	public KdNode root;

	/**
	 * method used to print the KdTree nicely,
	 * it only the 4 first layers
	 */
	public String toString(){
		
		int depth=4;
		int path=0;
		String chemin="";
		boolean n=false;
		
		for(int p=0;p<depth;p++){
		//printiong depths one by one
			path=0;
			KdNode r=root;
			String str="";
			for(int j=0;j<(1<<p);j++){
			//for the 2^p nodes
				for(int i=0;i<p;i++){
					if((path & (1<<i))==0){
					//exploring the tree
						if(r.rightChild==null){
							n=true;
						}
						else{
							r=r.rightChild;
							chemin+="R";
						}
					}
					else{
						if(r.leftChild==null){
							n=true;
						}
						else{
							r=r.leftChild;
							chemin+="L";
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
				r=root;
				str="";
				path++;
				chemin="";
			}
		}
		String res="";
		return res;
	}
	
	/**
	 * Private function which returns the median point of a list of point sorted
	 * by a direction.
	 *
	 * @param List<Point> listOfPointsTriee: the list of sorted points where we want the median
	 * @param int direction: an integer representing the coordinate of the point that we have interest for
	 * @return Point: the median point of the sorted list
	 */
	private Point median( List<Point> listOfPoints , int direction) {
		
		final int dir = direction;
		listOfPoints.sort(new Comparator<Point>() {
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
		
		if(listOfPoints.size() > 0) {
			if(listOfPoints.size()%2 != 0) {
				return listOfPoints.get(listOfPoints.size()/2);
			}
			return listOfPoints.get(listOfPoints.size()/2-1);
		}
		else {
			return listOfPoints.get(0);
		}
	}
	/**
	 * Private recursive function which create a k-d tree
	 *
	 * @param List<Point> listOfPoints: the list of point of the k-d tree 
	 * @param int k: the dimension k of the k-d tree
	 * @param int depth: the stage that we are creating
	 * @return KdNode: the root of the created k-d tree 
	 */
	private KdNode createKdTree (List<Point> listOfPoints , int k , int depth,int pmax) {
		//TODO probleme de cas d'egalite a corriger, si on a plus des points egaux au niveau d'une mediane ca pose probleme (solution temporaire en supprimant les doublons)
		int size = listOfPoints.size();
		final int direction = depth%k;
		
		if( listOfPoints.isEmpty() || depth==pmax) {
			return null;
		}
		
		Point point = median(listOfPoints , direction);
		KdNode res = new KdNode(point);
		res.direction = direction;
		
		if(size > 2) {
			//list are sorted so the points are on the right side
    
			res.leftChild = createKdTree(listOfPoints.subList(0, listOfPoints.indexOf(point)) , k , depth+1,pmax);
			res.rightChild = createKdTree(listOfPoints.subList(listOfPoints.indexOf(point)+1 , size) , k , depth+1,pmax);
			return res;
		}
		else if (size == 2){
			res.leftChild = null;
			res.rightChild = createKdTree(listOfPoints.subList(listOfPoints.indexOf(point)+1 , size) , k , depth+1,pmax);
			return res;
		}
		else {
			res.rightChild = null;
			res.leftChild = null;
			return res;
		}
	}
	
		
	/**
	 * Constructor of the class k-d tree
	 *
	 * @param List<Point> listOfPoints: the list of point of the k-d tree 
	 * @param int k: the dimension k of the k-d tree
	 * @param int pmax the maximum number of layers
	 */
	public KdTree( List<Point> listOfPoints , int k, int pmax ) {

		//TODO ne gere pas egalite selon une seule dimension
		Set<Point> setPoints=new HashSet<Point>();//deleting doubles complexity : O(n)
		setPoints.addAll(listOfPoints);
		listOfPoints=new ArrayList<Point>(setPoints);
		this.root = createKdTree( listOfPoints , k , 0,pmax );
	}
	
	private KdNode algoRecherche( KdNode startingNode, KdNode searchedNode ) {
		
		int direction = startingNode.direction;
		
		if( startingNode.point.equals(searchedNode.point) ) {
			return startingNode;
		}
		else if(searchedNode.point.coord[direction] <= startingNode.point.coord[direction]) {
			
			if(startingNode.leftChild==null){
				
				return startingNode;
			}
			else{
				return algoRecherche(startingNode.leftChild , searchedNode);
			}
		}
		else if(searchedNode.point.coord[direction] >= startingNode.point.coord[direction]) {
			if(startingNode.rightChild == null){
				return startingNode;
			}
			else{
				return algoRecherche(startingNode.rightChild , searchedNode);
			}
		}
		
		System.out.println("Cas non géré");
		return null;
	}
	
	/**
	 * 
	 * @param Point point the vector we are looking for in the tree
	 * @return true if the vector is in the tree, false otherwise
	 */
	public boolean recherche( Point point  ) {
		KdNode searchedNode = new KdNode(point);
		KdNode fatherNode = algoRecherche(this.root , searchedNode);
		System.out.println("Parent:"+fatherNode.point+" Point:"+point);
		return fatherNode.point.equals(point);
	}
	
	/**
	 * Add a vector in the tree, if it is already in, does nothing
	 * @param Point point vector we want to add in the tree
	 * @param k number of dimensions we 
	 * @return
	 */
	public boolean addNode( Point point,int k ) {
		
		KdNode noeudToAdd = new KdNode(point);
		KdNode fatherNode = algoRecherche(this.root , noeudToAdd);
		if(fatherNode.point.equals(point)){
			return false;
		}
		int direction = fatherNode.direction;
		noeudToAdd.direction=(fatherNode.direction+1)%k;
		if(point.coord[direction] <= fatherNode.point.coord[direction]) {
			fatherNode.leftChild = noeudToAdd;
			return true;
		}
		else if(point.coord[direction] >= fatherNode.point.coord[direction]) {
			fatherNode.rightChild = noeudToAdd;
			return true;
		}
		return false;
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
		
		if(root==null){
			return null;
		}
		return getNearestNeighbor(root,point,root.point);
		
	} 
	/**
	 * Private function
	 *
	 * @param KdNode father: a node of the tree
	 * @param KdNode node: the node that needs to find its nearest neighbor
	 */	
	
	private Point getNearestNeighbor(KdNode father,Point newPoint,Point candidate/*ou Point pointproche*/){
		
		if(newPoint.distsq(father.point)<newPoint.distsq(candidate)){
			candidate=father.point;
		}
		//if it's greater than 0 father<point => searching right
		int dist1D=newPoint.getCoord(father.direction)-father.point.getCoord(father.direction);
		
		KdNode n1=(dist1D>0)?father.rightChild:father.leftChild;
		KdNode n2=(dist1D<=0)?father.rightChild:father.leftChild;
		
		if(n1!=null){
			candidate=getNearestNeighbor(n1,newPoint,candidate);
	
		}
		if(n2!=null && dist1D*dist1D<newPoint.distsq(candidate)){
			candidate=getNearestNeighbor(n2,newPoint,candidate);

		}
		
		return candidate;
		
	}
	/**
	 * Public fonction which cut a tree to a given depth
	 * 
	 * @param KdNode father : at the first call it should be the root of the tree
	 * @param int depth : the number of layers we want to keep
	 */
	public void truncation(KdNode father,int depth) {
		
		if(depth==0){
			if(father==null){
				System.out.println("error while cutting");
				return;
			}
			father.rightChild=null;
			father.leftChild=null;
			return;
		}
		
		else if(father.leftChild==null || father.rightChild==null){
			System.out.println("error while cutting");
			
		}
		else{
			truncation(father.rightChild,depth-1);
			truncation(father.leftChild,depth-1);
			
		}		
	}
	/**
	 * @brief try to fill a list with 2^maxLayer colors, the harvesting of colors starts at the level maxLayer
	 *  if it is not enough, the search is remade at maxLayer-1 etc
	 * @param maxLayer first search level
	 * @return a List containing the colors
	 */
	public List<Point> getColors(int maxLayer) {
		List<Point> result=new ArrayList<Point>();
		int hl=0;
		int numberOfColorsMax=1<<maxLayer;
		
		while(hl<=maxLayer && result.size()<numberOfColorsMax){
			getColors(result,root,maxLayer,hl);
			hl++;
		}
		
		return result;
	}
	
	private List<Point> getColors(List<Point> result,KdNode father,int maxLayer,int harvestLayer){
		if(maxLayer==harvestLayer && father!=null){
			result.add(father.point);
		}
		if(father.rightChild!=null){
			getColors(result,father.rightChild,maxLayer-1,harvestLayer);
			
		}
		if(father.leftChild!=null){
			getColors(result,father.leftChild,maxLayer-1,harvestLayer);
			
		}
		return result;
	}
}