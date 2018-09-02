
public class Tuple2 {
	private Iterable<Edge> edge;
	private double cost;
	
	public Tuple2(Iterable<Edge>edge,double cost) {
		// TODO Auto-generated constructor stub
		this.edge=edge;
		this.cost=cost;
	}

	public Iterable<Edge> getEdge() {
		return edge;
	}

	public void setEdge(Iterable<Edge> edge) {
		this.edge = edge;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
}
