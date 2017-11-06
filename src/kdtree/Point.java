package kdtree;

//import kdtree.KdTree.KdNode;

public class Point {

	int dim = 0;
	int coord[];
	
	public Point( int...is ) {
		this.coord = is;
		this.dim = is.length;
	}
	
	public int getCoord( int dim ) {
		return this.coord[dim];
	}
	
	public void setCoord(int ...c){
		assert(c.length == this.dim);
		for(int i=0;i<c.length;i++){
			coord[i]=c[i];
		}
	}
	public String toString() {
		String ret = "(";
		for(int i : coord) {
			ret+= ""+i+"," ;
		}
		return ret+")";
	}
	
	public int distsq(Point p2){
		
		int res=0;
		for(int i=0;i<this.dim;i++){
			
			res+=(this.coord[i]-p2.coord[i])*(this.coord[i]-p2.coord[i]);
		}
		return res;
	}
}


	
	