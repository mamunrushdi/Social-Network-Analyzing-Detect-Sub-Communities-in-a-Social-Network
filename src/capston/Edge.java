package capston;
/**
 * 
 * @author MD AL MAMUNUR RASHID
 *
 *This class represent each edge of the graph
 */

public class Edge
{
	private int startNode;
	private int endNode;
	
	private double credit;
	
	public Edge(int startNode, int endNode)
	{
		this.startNode = startNode;
		this.endNode = endNode;
		credit = 0.0;
	}
	
	//add edge weight
	public void addCredit(double credit)
	{
		this.credit += credit;
	}
	//get the stat node
	public int getStartNode()
	{
		return startNode;
	}
	//get the end node
	public int getEndNode()
	{
		return endNode;
	}
	//get edge weight
	public double getCredit()
	{
		return credit;	
	}
	
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		return prime * (startNode + endNode);
	
		
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Edge other = (Edge) obj;
		
		if((this.startNode == other.endNode) && (this.endNode == other.startNode)) 
			return true;

		
		if (endNode != other.endNode)
			return false;
		if (startNode != other.startNode)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		String ret = "";
		ret =  startNode + " <--> " + endNode;
		return ret;
	}
	public void printEdgeWithCredit()
	{
		System.out.println(startNode + " <--> "+ endNode + "credit " + credit);
	}
}
