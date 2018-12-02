package capston;


/**
 * @author MD AL MAMUNUR RASHID (Mamun)
 * 
 * This class find the minimum number of users of undirected network i.e. in Facebook 
 * need to post a message be seen the message by everyone in the network.
 * 
 * Time complexity: O(|v|^2)
 * 
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class FindingMinNumUserToReachAPost 
{

	public static void main(String[] args)
	{
		Graph graph = new Graph();
		//load the graph
		GraphLoader.loadGraph(graph, "data/facebook_2000.txt");
	
		List<Integer> minNumOfUser = findMinNumOfUserToReachAMessage(graph);
		
		System.out.println("At least " + minNumOfUser.size() +
				" user have to post the message to be seen by everyone in the network.");		
		System.out.println("The follwoing users have to post the message: ");
		minNumOfUser.forEach(user -> System.out.print(user + " "));
		System.out.println();
	}
	//this backbone method of this class which will calculate the minimum of users to post the message
	public static List<Integer> findMinNumOfUserToReachAMessage(Graph graph)
	{
		/* Create counter for posted node number: minNumOfUser 
		 *  which will be returned at end of the program.	
		*/
		List<Integer> minNumOfUser = new ArrayList<>();
		
		
		/**	Create a priority queue of all nodes where the weight of a node is the total
		 *  number of its neighbor.	
		 */
		
		PriorityQueue<Vertex> vertexPriorityQueue = new PriorityQueue<>();
		
		//vertex list to store posted node
		List<Integer> posted = new ArrayList<>();
		
		//vertex list to store visited node
		List<Integer> visited = new ArrayList<>();
		
		//get map of the graph
		Map<Integer, HashSet<Edge>> adjListMap = graph.getAdjListsMap();
		
		//get vertex list of the graph
		List<Integer> verticesList = new ArrayList<>(adjListMap.keySet());//graph.getVerticesList();
		
		//check each user of the network
		for(int v : verticesList)
		{
			//get the neighbor of the of v
			//ArrayList<Integer> neighbor = getNeighborEndNode(adjListMap.get(v));
			HashSet<Edge> neighborList = adjListMap.get(v);
			Vertex vertex = new Vertex(v);
			vertex.setLevel(neighborList.size());
			vertexPriorityQueue.add(vertex);
		}

		//Continue this until node’s of the priority queue has zero weight.
		while(!vertexPriorityQueue.isEmpty())
		{
			//	Remove the top node, X from the queue,
			Vertex vertex = vertexPriorityQueue.remove();
			//vertex weight is 0, then return
			if(vertex.getLevel() <= 0) 
				{
					return minNumOfUser;
				}
			int vertexID = vertex.getVertex();
			
			//add vertex id to posted node counter list: minNumOfUser
			minNumOfUser.add(vertexID);

			//mark X as posted node and it and its neighbor nodes visited.
			posted.add(vertexID);
			
			//get the neighbor of vertex
			HashSet<Integer> neighbor = getNeighborEndNode(adjListMap.get(vertexID));
			visited.addAll(neighbor);

			//Update the priority queue’s remaining node’s weight by resetting their number of 
			//unvisited node by removing the X’s visited neighbor nodes from their neighbor node lists
			updatePriorityQueueNodeWeight(vertexID, neighbor, vertexPriorityQueue, adjListMap);
			
			//Check if any neighbor node, v of vertex has at 10% of his/her friends who has posted/shared the message.
			for(int v : neighbor)
			{
				if(!posted.contains(v))
				{
					if(hasRequiredNumberOfPostedFriends(v, posted, adjListMap) && unseenFreinds(v, vertexPriorityQueue))
					{
						//If yes, create a new priority queue2  and add v to it and start to visit them
						visitNeighborNodes(v, vertexPriorityQueue, adjListMap, visited, posted);
					}
				}
			}
		}
		//Return postedNodeCounter
		return minNumOfUser;
	}

	//this method check if the neighbor has unvisited neighbors
	private static boolean unseenFreinds(int v, PriorityQueue<Vertex> priorityQueue) 
	{	
		Vertex vertex = getVertexFromPriorityQueue(v, priorityQueue);
		if(vertex.getLevel() > 0) return true;
		return false;
	}

	//helper method to visit friends of a user
	private static void visitNeighborNodes(int v, PriorityQueue<Vertex> vertexPriorityQueue,
			Map<Integer, HashSet<Edge>> adjListMap, List<Integer> visited, List<Integer> posted) 
	{
		//get the vertex from priority queue
		Vertex priorityVertex = getVertexFromPriorityQueue(v, vertexPriorityQueue);
	
		PriorityQueue<Vertex> subPriorityQueu = new PriorityQueue<>();
		
		subPriorityQueu.add(priorityVertex);
		
		while(!subPriorityQueu.isEmpty())
		{
			if(vertexPriorityQueue.isEmpty()) return;
			//Remove vertex, mark it as posted node and it and its neighbor nodes visited. 
			Vertex pVertex = subPriorityQueu.remove();
			
			if(pVertex.getLevel() <= 0) return;
			int vId = pVertex.getVertex();
			posted.add(vId);

			//get the neighbor of vertex
			HashSet<Integer> neighborNode = getNeighborEndNode(adjListMap.get(vId));
			visited.addAll(neighborNode);
			
			//unvisited node by removing the X’s visited neighbor nodes from their neighbor node lists
			updatePriorityQueueNodeWeight(v, neighborNode, vertexPriorityQueue, adjListMap);

			//Remove the vertex from the priority queue
			vertexPriorityQueue.remove(pVertex);
			//Check if any neighbor node, Z of Y has at least 3 (three) friends who has posted/shared the message.
			for(int pv : neighborNode)
			{
				if(hasRequiredNumberOfPostedFriends(pv, posted, adjListMap))
				{
					Vertex subPrVertex = getVertexFromPriorityQueue(v, vertexPriorityQueue);
					if(subPrVertex != null) subPriorityQueu.add(subPrVertex);
				}//end of if block
			}//end of for loop
		}//end of while loop
	}//end of the method

	//this method simply extract the vertex from the priority queue using a given vertex id, v
	private static Vertex getVertexFromPriorityQueue(int v, PriorityQueue<Vertex> vertexPriorityQueue) 
	{
		for(Vertex  vertex : vertexPriorityQueue)
		{
			if(vertex.getVertex() == v) return vertex;
		}
		return null;
	}

	//this method check whether a user has required number of friends who has posted/shared the message to
	//share the post
	private static boolean hasRequiredNumberOfPostedFriends(int v, List<Integer> posted, Map<Integer, HashSet<Edge>> adjListMap) 
	{
		//threshold defines if a user's 10% friends has posted/shared the post
		HashSet<Integer> neighborEndNodes = getNeighborEndNode(adjListMap.get(v));

		double size = neighborEndNodes.size();
		double threshold = (10 * size) / 100;
		
		int postedVertexCounter = 0;
	
		//get v's neighbor from graph map
		for(int node : neighborEndNodes)
		{
			if(posted.contains(node)) postedVertexCounter++;
		}
		
		if(postedVertexCounter >= threshold) return true;
		
		return false;
	}

	//updating (decreasing) the priority of nodes by decreasing their weight 
	//if their neighbor already been visited
	private static void updatePriorityQueueNodeWeight(int root, HashSet<Integer> neighbor,
			PriorityQueue<Vertex> vertexPriorityQueue, Map<Integer, HashSet<Edge>> adjListMap) 
	{
		//add root the neighbor so root will be not counted again
		ArrayList<Integer> tempNeighbor = new ArrayList<>(neighbor);
		tempNeighbor.add(root);
	
		//get remaining of node in priority queue
		for(Vertex vertex : vertexPriorityQueue)
		{
			//get it's neighbor from graph map
			Set<Integer> neighborEndNodes = new HashSet<>(getNeighborEndNode(adjListMap.get(vertex.getVertex())));
			//check if its any vertex from neighbor was in the visited neighbor or equal to root
			neighborEndNodes.remove(root);
			
			for(int v : neighborEndNodes)
			{
				if(tempNeighbor.contains(v)) vertex.setLevel(-1); //decreasing weigh by 1
			}
		}
	}

	//helper method to extract neighbor end nodes
	public static HashSet<Integer> getNeighborEndNode(HashSet<Edge> neighborEdgeList)
	{
		HashSet<Integer> neighborEndNodes = new HashSet<>();
		
		for(Edge edge : neighborEdgeList)
		{
			int endNode = edge.getEndNode();
			neighborEndNodes.add(endNode);
		}
		
		return neighborEndNodes;
	}
}
