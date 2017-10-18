package kdTreePackage;

public class KdNode {
	
	private KdNode filsDroit;
	private KdNode filsGauche;
	private int distance;
	private Point point;
	// TODO import point
	
	public KdNode(){
		
	}
	
	public boolean isTerminal(){
		
	}
	
	public KdNode getFilsDroit() {
		return this.filsDroit;
	}
	
	public KdNode getFilsGauche() {
		return this.filsGauche;
	}
	
	public int getDistance() {
		return this.distance;
	}
	
	public Point getPoint() {
		return this.point;
	}
	
	// TODO fonction de distances
	
}
