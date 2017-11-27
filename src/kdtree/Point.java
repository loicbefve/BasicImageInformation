package kdtree;

public class Point {

	int dim = 0;
	int coord[];
	
	public Point( int...is ) {
		this.coord = is;
		this.dim = is.length;
	}
	
	public Point(Point p){
		dim=p.dim;
		coord=new int[dim];
		for(int i=0;i<p.coord.length;i++){
			coord[i]=p.coord[i];
		}
	}
	
	@Override
	public boolean equals(Object p2){
		//can be a problem to indexof!
		if(this==p2) return true;
		if(p2==null) return false;
		if (getClass() != p2.getClass());
			
		Point p=(Point) p2;
		if(p.dim!=dim){
			return false;
		}
		if(coord==null){
			if(p.coord!=null){
				return false;
			}
		}
		else{
			for(int i=0;i<this.dim;i++){
				if(coord[i]!=p.getCoord(i)){
					return false;
					}
			}
		
		}
		return true;
		
	}
	@Override
	public int hashCode(){
		int h=0;
		for(int i=0;i<this.dim;i++){
			h += this.getCoord(i);
			h += h<<10;
			h ^= h>>6;
			h += h << 3;
			h ^= h >> 11;
			h += h << 15;   
			       
		}
		return h;
	}
	
	public int getCoord( int dim ) {
		return this.coord[dim];
	}
	
	public int[] getCoords() {
		return this.coord;
	}
	
	public void setCoord(int ...c){
		assert(c.length == this.dim);
		for(int i=0;i<c.length;i++){
			coord[i]=c[i];
		}
	}
	
	public void setOneCoord(int dim,int val){
		assert(dim< this.dim);
		coord[dim]=val;
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
		for(int i=0;i<Math.min(this.dim, p2.dim);i++){
			
			res+=(this.coord[i]-p2.coord[i])*(this.coord[i]-p2.coord[i]);
		}
		return res;
	}
}


	
	