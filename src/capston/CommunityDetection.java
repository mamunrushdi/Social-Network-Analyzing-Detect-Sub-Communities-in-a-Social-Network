package capston;
/**
 * @author MD AL MAMUNUR RASHID (Mamun)
 * 
 * This class answer the hard question of the project by detecting sub-communities in the network
 * who has minimum number of user.
 * 
 * Algorithm:
 * 
 * 1. Get BFS (Breadth First Search) representation of the graph from each each vertex of the graph)
 * 2. Label each node by the number of the shortest paths that reach it from the root
 * 3. Set credit of each node and DAG edges (This part has also been implemented in Graph class) as this step
 * 	  will be repeated for every graph to detect communities)
 * 
 * Time complexity: O(|v|^3)
 * 
 * In detail the algorithm: 	
 * 	A-------B------D------E
 *   -    -        |      |
 *    -  -         |      |
 *     -C          G------F
 *     
 *     Fig 1.1 Demo graph
 * 
 * Grivan-Newman algorithm implementation: 
 * 1. First perform Bread First Search for each node of the graph and set the how many parents each node has
 * 
 * 			   	 E
 * 				-1 -
 *			   -   -
 *			  D 	F			 
 * -		- 1-   -1
 *		   -    - -
 *		 B       G
 *		 1		 2	
 *		-  -
 *	  -     -
 *   A       C
 *   1       1
 *
 *	Fig 1.2 : Here the level of each node or how many parents each has been set
 *
 * 2. Calculate the edge credit to find the maxbetweenness edge of the graph.
 * Max betweenness edge is the edge of the edge throw which most shortest root locate.
 * 	
 * 	a. To set the edge credit, start setting the credit of bottom node( leaf node - who does not have child)
 * 	   1. Credit of node a got divided by the total edges which go from this node to above node.
 * 	    Edge from this node to the above (parent node of it) get the this node's (child node) credit.
 * 	b. Above node of the child node get credit 1 plus the credits of edges which to to this node from below.
 * 		In the figure, the leaf node, 	A, C, G has credit 1, edge 	AB, CB has 1 and GD and GD has .5.
 * 		B has total credit (1 + (1 + 1)), 1 for it's self and two from edges which join to it from below.	 	
 * 
 * 										E
 * 									   -1-
 * 									 -	   -
 * 								   -		 -					
 * 					gf-W:4.5     -				-	fe-W: 2.5
 * 							   -			      -
 * 							 -						-
 * 							 D	dW: 4.5				F	fW: 2.5			   					
 * 							-1-                    -1
 *                        -     -		     	  -																				
 * 			bd-w: 3		-		   - gd- W: .5   -
 * 					  -		   		  -        - gf-W: 1.5
 * 					-					 -   -
 * 				 -							G gW: 1					 													
 * 				B	bW: 3					2															
 * 			  - 1 -																					
 * 			-      -
 * 		  -          -
 * 		- ab-W: 1      - cb-W: 1
 * 	  -                  -
 * 	-                      -																										
 * A  aW: 1            		C	cW: 1																	
 * 1						1																	
 * 																				
 *           Figure: 1.3 Showing the edge credit setting
 *           
 *           
 * 3. After calculating each breadth first search graph's edge credit from each node, 
 * find the max edge beetweenness of the graph.
 * 
 *  4. Delete the maxbetweenness edge and create new graph from this graph.
 *  5. Keep detecting sub-communities from newly found sub-communities as long as long
 *     newly found sub-community has minimum number of nodes. 
 *     
 */		  
 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CommunityDetection 
{
	private  List<Graph> subCommunites;
	private int sizeSubCommunites;
	private int minVertices;
	private Graph mainGraph;
	
	public CommunityDetection(Graph graph, int minVertices)
	{
		mainGraph = graph;
		this.minVertices = minVertices;
		subCommunites = new ArrayList<>();
		sizeSubCommunites = 0;	

		//detect the sub communities
		getTotalSubCommunites();
	}
	
	public List<Graph> getSubCommunites()
	{
		return subCommunites;
	}
	
	public int getSizeOfSubCommunites()
	{
		return sizeSubCommunites;
	}
	//this method will detect the total communities of the network
	private void getTotalSubCommunites()
	{
		if(mainGraph.getNumVertices() < minVertices * 2) return;
		
		Queue<Graph> graphToDetectCommunity = new LinkedList<>();
		
		//add main graph to the queue
		graphToDetectCommunity.add(mainGraph);
		
		//keep detected communities till the queue is empty
		while(!graphToDetectCommunity.isEmpty())
		{
			Graph graph = graphToDetectCommunity.remove();

			List<Edge> maxBetweennessEdge = graph.getMaxBetweennessEdge();
			
			//remove maxBetweennessEdge from the graph
			removeMaxBetweennessEdge(maxBetweennessEdge,graph );
			//System.out.println("Graph after removing the maxBetweenness edges: " + graph);

			List<Graph> subCommunities = getCommunitesWithAllRoots(graph, maxBetweennessEdge, minVertices);	
			
			for(Graph g : subCommunities)
			{
				int totalVertex = g.getNumVertices();
			
				if(totalVertex >= minVertices * 2) //if community has twice of minUsernumber, then keep detecting communities
				{
					graphToDetectCommunity.add(g);
				}
				else
				{
					subCommunites.add(g);
					sizeSubCommunites++;
 				}
			}
		}
	}
	//this method will build graph using all the roots (both end vertices of the edge)
	private List<Graph> getCommunitesWithAllRoots(Graph graph, List<Edge> maxBetweennessEdge, int minVertices)
	{
		Set<Integer> roots = getSubCommunitiesRoots(maxBetweennessEdge);

		List<Graph> subCommunities = new ArrayList<>();
		for(int root : roots)
		{
			Graph g = buildSubCommunitiesWithSingleRoot(root, graph);
			subCommunities.add(g);
		}

		//Ensure graph has minimum number of vertices
		for(Graph g : subCommunities)
		{
			if(g.getNumVertices() < minVertices) 
			{
				subCommunities.add(graph);
				return subCommunities;
			}

		}
		
		return subCommunities;
	}
	//this method will build a graph with a single root
	private Graph buildSubCommunitiesWithSingleRoot(int root, Graph mainGraph) 
	{
		Graph graph = new Graph();
		Map<Integer, HashSet<Edge>> newGraphMap = mainGraph.getAdjListsMap();
		
		//add root vertex to the graph
		graph.addVertex(root);
		
		Map<Integer, HashSet<Edge>> graphMap = mainGraph.getAdjListsMap();
		//get neighbor of the root from main graph
		
		HashSet<Edge> neighbor = graphMap.get(root);
		//add neighbor to the root's new graph
		for(Edge edge: neighbor)
		{
			int end = edge.getEndNode();
			graph.addVertex(end);
			//add vertex
			graph.addEdge(root, end);
		}
		
		//add each neighbor vertex and it's neighbor to graph
		for(Edge edge: neighbor)
		{
			int end = edge.getEndNode();
			HashSet<Edge> endNeighbor = graphMap.get(end);
			for(Edge newedge : endNeighbor)
			{
				int newEnd = newedge.getEndNode();
				if(!newGraphMap.containsKey(newEnd)) graph.addVertex(newEnd);
				graph.addEdge(end, newEnd);
			}

		}
		return graph;
	}

	//to built the sub-communities, we need new root for each partitioned graph
	//hear roots will be the both end vertices of maxBetweenness edge's
	private Set<Integer> getSubCommunitiesRoots(List<Edge> maxBetweennessEdge)
	{
		Set<Integer> roots = new HashSet<>();

		for(Edge edge : maxBetweennessEdge)	
		{
			int start = edge.getStartNode();
			roots.add(start);
			
			int end = edge.getEndNode();
			roots.add(end);
		}
		return roots;
	}
	//this method will remove the maximum betweenness edges from the graph
	private void removeMaxBetweennessEdge(List<Edge> maxBetweennessEdge, Graph graph)
	{
		for(Edge edge : maxBetweennessEdge)
		{
			removeSingleEdge(edge, graph);
		}
	}
	//this method will remove one edge from the graph
	private void removeSingleEdge(Edge edge, Graph g)
	{
		//get start and end vertex of the edge
		int startVertex = edge.getStartNode();
		int endVertex = edge.getEndNode();
		//to remove an edge, we to remove both nodes from their respective start node, 
		for(int i = 0; i < 2; i++)
		{
			removeOneEndVertex(g, startVertex, endVertex);
			
			//after removing endVertex from startVertex neighbor list,
			//remove startVertex from endVertex neighbor list
			removeOneEndVertex(g, endVertex, startVertex);			
		}
		//find startVertex from the graph and remove endVertex from the graph
	}
	
	private void removeOneEndVertex(Graph g, int startVertex, int endVertex)
	{
		Map<Integer, HashSet<Edge>> graphMap = g.getAdjListsMap();
		Set<Edge> edgeList =graphMap.get(startVertex);
		
		//create temp edgeList
		Set<Edge> tempEdgeList = new HashSet<>(edgeList);
		for(Edge e : tempEdgeList)
		{
			if(e.getEndNode() == endVertex) edgeList.remove(e);
		}
	}
}
