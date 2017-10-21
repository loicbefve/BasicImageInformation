package kdtree;

public class Point {
	
	public int dim;
	public int[] coords;
	public Point(int ... _coords){
		
		dim=_coords.length;
		
		for(int i=0;i<dim;i++){
			
			coords[i]=_coords[i];
			
		}
	}

}
