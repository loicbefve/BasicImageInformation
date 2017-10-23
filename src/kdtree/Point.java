package kdtree;

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
	public String toString() {
		String ret = "";
		for(int i : coord) {
			ret+= ""+i+"," ;
		}
		return ret;
	}
}


	
	