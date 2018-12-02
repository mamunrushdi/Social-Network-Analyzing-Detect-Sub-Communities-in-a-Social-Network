package capston;
/**
 * 
 * @author MD AL MAMUNUR RASHID
 * 
 * This class represent each vertex of the graph
 *
 */
public class Vertex implements Comparable<Vertex>
{
	private int vertex;
	private int level; //distance from the root node or number of unvisited neighbors
	/**
	 * credit is for calculated the edge density
	 * Each leaf in DAG node get a credit of 1 
	 * 
	 * Each node that is not a leaf gets a credit = 1 + sum of the credits of the 
	 * DAG edges from this Vertex to the level below
	 * 
	 */
	
	private double credit; //
	
	public Vertex(int vertex) 
	{
		this.vertex = vertex;
		level = 0;
		credit = 0.0;
	}
	
	public int getVertex() {
		return vertex;
	}

	public void setVertex(int vertex) {
		this.vertex = vertex;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level += level;
	}
	public void resetLevel()
	{
		level = 0;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double crd) {
		credit = credit + crd;
	}
	
	//reset credit
	public void resetCredit()
	{
		credit = 0.0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(credit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + level;
		result = prime * result + vertex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (Double.doubleToLongBits(credit) != Double.doubleToLongBits(other.credit))
			return false;
		if (level != other.level)
			return false;
		if (vertex != other.vertex)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return vertex + "";
	}

	@Override
	public int compareTo(Vertex o) 
	{
		if(this.getLevel() < o.getLevel()) return 1;
		if(this.getLevel() > o.getLevel()) return -1;

		return 0;
	}
}
