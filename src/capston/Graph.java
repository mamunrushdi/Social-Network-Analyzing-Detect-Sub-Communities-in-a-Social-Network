package capston;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @MD AL MAMUNUR RASHID
 * Representing the network in a Graph
 * 	
 * 	A-------B------D------E
 *   -    -        |      |
 *    -  -         |      |
 *     -C          G------F
 *     
 *     Fig 1.1 Demo graph
 * 
 * This class also contain the following methods as a part of Grivan-Newman algorithm implement 
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
 * */		  
 
 
public class Graph 
{
	/* 
	 * using Adjacent List graph representation as our graph is a sparsely graph
	 */
	
	//store the number of vertices and edges
	private int numVertices;
	private int numEdges;
	
	//keep a list of vertices
	private List<Integer> verticesList;
	
	//map vertex id to its vertex
	private Map<Integer, Vertex> vertexLevelMap;
	
	//store a set of vertex
	//bfsVertex list only contains the vertex which has DAG (parent vertex) and not leaf vertex
	private List<Integer> bfsVertexList;
	
	private Map<Integer, HashSet<Edge>> adjListsMap;
	
	//constructor
	public Graph()
	{
		adjListsMap = new HashMap<>();
		numVertices = 0;
		numEdges = 0;
		bfsVertexList = new ArrayList<>();
		vertexLevelMap = new HashMap<>();
		verticesList = new ArrayList<>();
	}
	public Map<Integer, HashSet<Edge>> getAdjListsMap()
	{
		return adjListsMap;
	}
	
	public Map<Integer, Vertex> getVertexLevelMap()
	{
		return vertexLevelMap;
	}
	
	public int getNumVertices()
	{
		return numVertices;
	}
	public int getNumEdges()
	{
		return numEdges / 2 ;
	}
	
	public List<Integer> getVerticesList()
	{
		return verticesList;
	}
	public void addVertex(int vertex) 
	{
		//add only vertex if the given Vertex has not been added
		if(!adjListsMap.containsKey(vertex)) 
		{
			//for a given new Vertex create a list of edges 
			//which contains neighbor vertices
			HashSet<Edge> neighborsList = new HashSet<>();		
			//put the Vertex and it's edge list to the map
			adjListsMap.put(vertex, neighborsList);
			vertexLevelMap.put(vertex, new Vertex(vertex));
			verticesList.add(vertex);
			numVertices++;
		}
		
	}
	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	public void addEdge(int from, int to) 
	{
		//create the edge
		//add edges between two vertices only if booth has been added to the map
		if(!adjListsMap.containsKey(from)) 
			throw new IllegalArgumentException("One of the Vertices or both vertices have not been added to map.");
		
		//get the vertex from map which the edge will be started
		//and add the destination vertex to the from vertex
		adjListsMap.get(from).add(new Edge(from, to));	
		numEdges++;
	}

	@Override
	public String toString()
	{
		String ret = "\n";
		for(int key: this.adjListsMap.keySet())
			ret += key + "--> " + adjListsMap.get(key) + "\n";
	
		return ret;	
	}

	
	public String printingGraph() //this a method for debugging purpose
	{
		String ret = "\n";
		for(int key: vertexLevelMap.keySet())
			ret += vertexLevelMap.get(key) + "\n";
	
		return ret;	
		
	}
	//get bfs vertex set
	public List<Integer> getbfsVertexList()
	{
		return bfsVertexList;
	}
	
	/**
	 * 4. Get DAG edges with maximum betweenness 
	 */

	public List<Edge> getMaxBetweennessEdge() 
	{
		Map<Integer, Graph> bfsRepresentationList = getBfsRepresentationList();

		//keep a list of vertices

		List<Edge> maxBetweennessEdge = new ArrayList<>();
		
		Map<Edge, Double> maxBetweennessEdgeMap = getBetweennessEdgeList(bfsRepresentationList, verticesList);

 		double maxCredit = Collections.max(maxBetweennessEdgeMap.values());
		
		for(Entry<Edge, Double> entry : maxBetweennessEdgeMap.entrySet())
		{
			if(entry.getValue() == maxCredit) maxBetweennessEdge.add(entry.getKey());
		}
		return maxBetweennessEdge;
	}
	
	//find the maximum Betweenness edge of the graph
	private Map<Edge, Double> getBetweennessEdgeList(Map<Integer, Graph> bfsRepresentationList, List<Integer> verticesList)
	{
		Map<Edge, Double> maxBetweennessEdgeMap =  new HashMap<>();
		
		//fist get all the edges
		getAllTheEdges(bfsRepresentationList, verticesList, maxBetweennessEdgeMap);
		
		for(int key : bfsRepresentationList.keySet())
		{
			Graph g = bfsRepresentationList.get(key);
			//g.printBFSRepresentationGraph(key);
			
			Map<Integer, HashSet<Edge>> graphAdjListsMap = g.getAdjListsMap();
			
			for(int vertexID : graphAdjListsMap.keySet())
			{
				HashSet<Edge>  edges = graphAdjListsMap.get(vertexID);
				for(Edge edge : edges)
				{
					Edge newEdge = new Edge(edge.getStartNode(), edge.getEndNode());
					
					if(maxBetweennessEdgeMap.containsKey(newEdge))
							maxBetweennessEdgeMap.put(newEdge, maxBetweennessEdgeMap.get(newEdge) + edge.getCredit());
				}
				
			}
	
		}
		//here vertexID is the id of the vertex of the map
		
		for(Edge key : maxBetweennessEdgeMap.keySet())
		{
			maxBetweennessEdgeMap.put(key, maxBetweennessEdgeMap.get(key)/2);	
		}
		
		return maxBetweennessEdgeMap;
	}
	
	//helper method to collect all the edges of the map
	private void getAllTheEdges(Map<Integer, Graph> bfsRepresentationList, List<Integer>  verticesList, Map<Edge, Double> maxBetweennessEdgeMap)
	{
		for(int vertexID : verticesList)
		{
			//get this vertex's edge from the bfsRepresentationList
			for(int graphID : bfsRepresentationList.keySet())
			{
				//get bfs graph
				Graph bfsGraph = bfsRepresentationList.get(graphID);
				//first add all the neighbor of vertex
				List<Integer> bfsVertexID = bfsGraph.getVerticesList();
				//vertex from the graph
				for(int v : bfsVertexID)
				{
					//get the neighbors of the vertex v
					HashSet<Edge> edgeList = bfsGraph.getNeighbors(v);	
					if(v == vertexID)
					{
						//add all the edges to map
						for(Edge edge : edgeList)
						{
							int endVertex = edge.getEndNode();
							Edge linkEdge = new Edge(vertexID, endVertex);
							maxBetweennessEdgeMap.put(linkEdge, 0.0);	
						}
					}
				}
			}
		}
	}
	//get list of graph's BFS representation
	private Map<Integer, Graph> getBfsRepresentationList()
	{
		Map<Integer, Graph> bfsRepresentationList = new HashMap<>();
		
		//get graph's vertices
		List<Integer> verticesList = getVerticesList();
		
		for(int root : verticesList)
		{

			Graph bfsGraph = getbfsRepresentation(root);
			
			bfsRepresentationList.put(root, bfsGraph);
		}
		//System.out.println("vertices list: " + verticesList);

		return bfsRepresentationList;
	}
	//helper method get graphs bfs representation map from each vertex
	private Map<Integer, Graph> getGraphBfsRepresentationMap()
	{
		Map<Integer,Graph> graphBfsRepresetationMap = new HashMap<>();

		System.out.println("size of vertices list: " + verticesList);
		System.out.println("verticesList: " +  verticesList);
		for(int root: verticesList)
		{

			Graph g = getbfsRepresentation(root);
			System.out.println("printing the graph: " + g);
			//label vertex of the graph
			graphBfsRepresetationMap.put(root, g);
			
		}
		return graphBfsRepresetationMap;
	}
	
	/**
	 * 3. Set credit of each node and DAG edges
	 * 
	 */
	public void setCredit(Graph bfsRepresentedGraph, int root )
	{
		//before setting the credit reset the whole graph (graph's lable and credit
		resetGraph(bfsRepresentedGraph);

		//set the vertex label
		setVertexLabel((bfsRepresentedGraph));
		//reverse the bfsVertexList
		Collections.reverse(bfsVertexList);
				
		Queue<Integer> bfsVertexQueue = new LinkedList<>(bfsVertexList);

		//set the leaf nodes and its DAG edges credit
		setLeafNodesCredit(bfsRepresentedGraph, root);

		//After setting the leaf node's credit print the graph
		//set the maps credit
		Map<Integer, HashSet<Edge>> graphAdjListsMap = bfsRepresentedGraph.getAdjListsMap();
	
		while(!bfsVertexQueue.isEmpty())
		{
			int currNode = bfsVertexQueue.remove();

			//get neighbor edges of currentNode
			HashSet<Edge> dagEdges =  graphAdjListsMap.get(currNode);
			double totalOfDAGedgesCredit = 0;
			
			for(Edge edge : dagEdges)
			{
				//reset the
				double dagEdgeCredit = edge.getCredit();
				totalOfDAGedgesCredit += dagEdgeCredit;
			}
			
			//set current node's credit
			Vertex currVertex = vertexLevelMap.get(currNode);
			currVertex.setCredit(1.0 +  totalOfDAGedgesCredit);
			
			//set current node's above DAG edge's credit 
			List<Integer> parentList = getListOfParentNodes(currNode, bfsRepresentedGraph);
			
			int totalOfParentNodes = parentList.size();
			
			//set the DAG edge value
			for(int pNode : parentList)
			{
				double vertaxCredit = currVertex.getCredit();
				//get the edge connected with child of parentNode
				HashSet<Edge> pNodeEdges = bfsRepresentedGraph.getNeighbors(pNode);
				for(Edge edge : pNodeEdges)
				{
					if(edge.getEndNode() == currNode)
						{
							edge.addCredit(vertaxCredit/totalOfParentNodes);
						}
				}
			}//end of for loop
		}
	}
	
	//private set leaf nodes and DAG edges credit
	private void setLeafNodesCredit(Graph bfsRepresentedGraph, int root)
	{
		Map<Integer, HashSet<Edge>> graphAdjListsMap = bfsRepresentedGraph.getAdjListsMap();
		
		List<Integer> visitedList = new ArrayList<>();
		
		for(int i = 0 ; i < bfsVertexList.size(); i++)
		{
			int parentNode = bfsVertexList.get(i);
			
			//System.out.println("bfsVertexList.get(i): " + bfsVertexList.get(i));
			
			HashSet<Integer> childList =new HashSet<>();
			
			for(int node : graphAdjListsMap.keySet())
			{
					if(node == parentNode)
					{
						childList = this.getIntegerNeighborNodeRepresentation(graphAdjListsMap.get(node));
						for(int child : childList)
							{
								//check if neighbor node is a leaf
								if(!visitedList.contains(child) && isALeafVertex(child, graphAdjListsMap, root, parentNode))
								{
								//	System.out.println("testing: child: " + child);
									//get the vertex of n id
									Vertex vertex = vertexLevelMap.get(child);
									visitedList.add(child);
									//set leaf vertex credit leaf 1		
									vertex.setCredit(1.0);
									
									//get the list of parent nodes of the leaf node
									List<Integer> parentList = getListOfParentNodes(child, bfsRepresentedGraph);
									
									int totalOfParentNodes = parentList.size();
									
									//set the DAG edge value
									for(int pNode : parentList)
									{
										//get the edge connected with child of parentNode
										HashSet<Edge> pNodeEdges = bfsRepresentedGraph.getNeighbors(pNode);
										for(Edge edge : pNodeEdges)
										{
											if(edge.getEndNode() == child)
												edge.addCredit(1.0/totalOfParentNodes);
										}
									}
								}

							}//end of inner for loop
					}//end of if block
					
			}//end of outer for loop
		}
	}
	
	//A Vertex is a leaf vertex if does not have neighbor
	private boolean isALeafVertex(int childVertex, Map<Integer, HashSet<Edge>> graphAdjListsMap, int root, int parent) 
	{
		//get the neighbor list of the vertex
		Set<Integer> neighborList = getIntegerNeighborNodeRepresentation(graphAdjListsMap.get(childVertex));
		
		if(neighborList.contains(root)) neighborList.remove(root);
		if(neighborList.contains(parent)) neighborList.remove(parent);
	
		if(neighborList.size() == 0) return true;
		return false;
	}
	//helper method to get list of parent nodes of the given node
	public List<Integer> getListOfParentNodes(int leafNode, Graph bfsRepresentedGraph)
	{
		List<Integer> listOfParentNodes = new ArrayList<>();
		
		Map<Integer, HashSet<Edge>> graphMap = bfsRepresentedGraph.adjListsMap;
		
		//get each element of the map check if for parent of the given node
		for(int key : graphMap.keySet())
		{
			 HashSet<Integer> childList = getIntegerNeighborNodeRepresentation(bfsRepresentedGraph.getNeighbors(key));
			 if(childList.contains(leafNode)) listOfParentNodes.add(key);
		}
		return listOfParentNodes;
	}
	
	 /**
	  *  2. Label each node by the number of the shortest paths that reach it from the root
	  */
	//label(add Vertex weight to each Vertex 
	//here weight is the shortest distance from the root
	public void testingLabelVertex()
	{
		int root = 5;
		Graph bfsGraph = getbfsRepresentation(root);
		
		System.out.println("in testingLabelVertex - Before: " + bfsGraph);
		
		setVertexLabel(bfsGraph);

		Map<Integer, Vertex> map = bfsGraph.getVertexLevelMap();
		System.out.println("checking the graph's list: ");
		for(int key : map.keySet())
			System.out.println(map.get(key));
		
		System.out.println("testingLabelVertex - After labeing: " + bfsGraph.printingGraph());
		
	}
	//this method set label of each vertex
	private void setVertexLabel(Graph bfsGraph)
	{
		Queue<Integer> bfsVertexQueue = new LinkedList<>(bfsVertexList);
		//get the vertexLevelMap of this graph
		Map<Integer, Vertex> vertaxMap = bfsGraph.getVertexLevelMap();
		
		//set the root label 1
		if(vertaxMap.containsKey(bfsVertexQueue.peek())) 
			{
				int root = bfsVertexQueue.peek();
				Vertex vertex = new Vertex(root);
				vertex.setLevel(1);
				vertaxMap.put(root, vertex);
			}

		int parentLevel = 0;

		while(!bfsVertexQueue.isEmpty())
		{
			//get parent node
			int parent = bfsVertexQueue.remove();
			//get parent's level
			if(vertaxMap.containsKey(parent)) 
					parentLevel = vertaxMap.get(parent).getLevel();

			//get parent's children
			Set<Integer> children = getIntegerNeighborNodeRepresentation(bfsGraph.getNeighbors(parent));

			for(int child : children)
			{
				if(vertaxMap.containsKey(child))
					{
						//get the vertex
						Vertex childVertex = vertaxMap.get(child);
						//update vertex level
						childVertex.setLevel(parentLevel);
						vertaxMap.put(child, childVertex);
 					}
			}
		}//end of while loop
	}
	
	//helper method to reset the graph's lable and credit
	private void resetGraph(Graph g)
	{
		//get graph vertext list
		Map<Integer, Vertex> vMap = g.getVertexLevelMap();
		
		//reset vertex's credit and label
		for(int key : vMap.keySet())
		{
			Vertex v = vMap.get(key);
			v.resetCredit();
			v.resetLevel();
		}
		
		//reset edge's edge credit
		Map<Integer, HashSet<Edge>> graphAdjListsMap = getAdjListsMap();
		
		for(int vertexID : graphAdjListsMap.keySet())
			//get the vertex
			vertexLevelMap.get(vertexID).resetCredit();;
		
	}
	/**
	 * 1. Get BFS (Breadth First Search) representation of the graph from each each vertex of the graph)
	 */
	//build Breadth First Search Graph representation starting at each graph
	public Graph getbfsRepresentation(int root)
	{
		//keep track of node which has been added as node to the path
		Set<Integer> pathNode = new HashSet<>();
		
		//set of visited node
		Set<Integer> visited = new HashSet<>();

		Set<Integer> visitedNode = new HashSet<>();
		Queue<Integer> toVisitQueue = new LinkedList<>();
		
		//add the root vertex to the queue
		toVisitQueue.add(root);
		visitedNode.add(root);
		
		//add root to visited 
		visited.add(root);
		
		//create the map
		return createBfsReprsentMap(pathNode, visited, visitedNode, toVisitQueue, root);	  
	}
	//helper method for BFS search representation
	private Graph createBfsReprsentMap(Set<Integer> pathNode, Set<Integer> visited, Set<Integer> visitedNode,
			Queue<Integer> toVisitQueue, int root)
	{
		//reset each time the vertex list
		bfsVertexList.clear();
		Graph bfsRepresentationGraph = new Graph();
	
 		while(!toVisitQueue.isEmpty())
		{
			int vertex = toVisitQueue.remove();
			//get neighbor
			HashSet<Edge> neighbors = getNeighbors(vertex);
			
 			
			if(ifHasUnvisitedNeighobrs(neighbors, visited) || ifHaveSameLevelNeigborNodeExcludingRoot(neighbors, root, vertex))
				{
					bfsRepresentationGraph.addVertex(vertex);
					pathNode.add(vertex);
					if(!bfsVertexList.contains(vertex))
						 bfsVertexList.add(vertex);
					
				}

			//if each item of neighbors not been visited, add them as a neighbor of the vertex
			for(Edge edge : neighbors)
			{
				int edgeNode = edge.getEndNode();
				
				if(!toVisitQueue.contains(edgeNode) && !visitedNode.contains(edgeNode))
					{
						toVisitQueue.add(edgeNode);
						visitedNode.add(edgeNode);
					}
				
				//add edgeNode the queue
				if(!visited.contains(edgeNode))
				{
					bfsRepresentationGraph.addEdge(vertex, edgeNode);
					//add the
					visited.add(edgeNode);
					
					/**
					 * FOR DEBUGING
					 */
					bfsRepresentationGraph.addVertex(edgeNode); //this portion is only for debugging purpose		
					
				}
				else if(ifNodeLocatsInSameLevelNeigborNodeExcludingRoot(pathNode, edgeNode, neighbors, root, vertex))// || ifHaveSameLevelNeigborNodeExcludingRoot(neighbors, root, vertex))
				{
		
					bfsRepresentationGraph.addVertex(vertex);
					if(!bfsVertexList.contains(vertex))
							bfsVertexList.add(vertex);
					pathNode.add(vertex);
					
					bfsRepresentationGraph.addEdge(vertex, edgeNode);
					//add the
					if(!visited.contains(edgeNode)) visited.add(edgeNode);
				}
			}//end of for loop
		}//end of while loop
		
 		//set credit for this graph
 		setCredit(bfsRepresentationGraph, root);
 		
		return bfsRepresentationGraph;
	}
	//helper method to detect same level node
	private boolean ifNodeLocatsInSameLevelNeigborNodeExcludingRoot(Set<Integer> pathNode, int target_Vartex, HashSet<Edge> neighbors,
			int root, int vertex) 
	{
		
		//if the target vertex and root are the same node, return false
		if(vertex == root || target_Vartex == root) return false;
		
		//get parent of the target vertex, start searching from root
		int parentNode = getParentNode(vertex, root);
		
		if(target_Vartex == parentNode) return false;
		
		//if nodes are sibling return false
		if(distanceBetweenTwoNodes(root, vertex) == distanceBetweenTwoNodes(root, target_Vartex)) return false;
		//get parent node's neighbor list
		Set<Integer> initialParentNodeNeighborList = getIntegerNeighborNodeRepresentation(this.getNeighbors(parentNode));
		
		Set<Integer> parentNodeNeighborList = filteredSiblings(initialParentNodeNeighborList, root, parentNode, vertex);

		Set<Integer> totalNeighborList = new HashSet<>();
		
		//now loop thorough the parentNeighbourNodes
		for(int parentNeighborNode : parentNodeNeighborList)
		{
			if(parentNeighborNode != vertex)
			{
				//get neighbor of parentNeighborNode
				Set<Integer> neighborList = getIntegerNeighborNodeRepresentation(this.getNeighbors(parentNeighborNode));
				totalNeighborList.addAll(neighborList);
			}
		}
		
		//get neighbor nodes of the target vertex
		Set<Integer> targetNeighbors = getIntegerNeighborNodeRepresentation(neighbors);
		
		//get each item from target vertex neighbor list and check them if they are located in tatalNeighborList 
		for(int node : targetNeighbors)
		{
			if(totalNeighborList.contains(node) && !pathNode.contains(node)) return true;
		}
		
		return false;
	}
	//another helper method to find same level node
	private Set<Integer> filteredSiblings(Set<Integer> nodeSet, int root, int parentNode, int currentVertex)
	{		
		//if tatalNeighborList contains root, parentNode or target vertex, remove them
		if(nodeSet.contains(root)) nodeSet.remove(root);
		if(nodeSet.contains(parentNode)) nodeSet.remove(parentNode);
		if(nodeSet.contains(currentVertex)) nodeSet.remove(currentVertex);
		
		Set<Integer> siblingSet = new HashSet<>();
		//get distance between currentNode and root node
		int distance = distanceBetweenTwoNodes(root, currentVertex);
		
		for(int node : nodeSet)
		{
			if(distanceBetweenTwoNodes(root, node) == distance)
			{
				siblingSet.add(node);
			}
		}
		
		return siblingSet;
	}
	
	//another helper method find same level node
	private boolean ifHaveSameLevelNeigborNodeExcludingRoot(HashSet<Edge> neighbors, int root, int vertex) 
	{
		if(vertex == root) return false;
		
		//get parent of the target vertex, start searching from root
		int parentNode = getParentNode(vertex, root);

		//get parent node's neighbor list
		Set<Integer> initialParentNodeNeighborList = getIntegerNeighborNodeRepresentation(this.getNeighbors(parentNode));

		//filter the parentNodeNeighbor list, keep the node which are only same level as the target vertex
		Set<Integer> parentNodeNeighborList = filteredSiblings(initialParentNodeNeighborList, root, parentNode, vertex);
		
		Set<Integer> totalNeighborList = new HashSet<>();
		
		//now loop thorough the parentNeighbourNodes
		for(int parentNeighborNode : parentNodeNeighborList)
		{
			if(parentNeighborNode != vertex)
			{
				//get neighbor of parentNeighborNode
				Set<Integer> neighborList = getIntegerNeighborNodeRepresentation(this.getNeighbors(parentNeighborNode));
				totalNeighborList.addAll(neighborList);
			}
		}
		
		//if tatalNeighborList contains root, parentNode or target vertex, remove them
		if(totalNeighborList.contains(root)) totalNeighborList.remove(root);
		if(totalNeighborList.contains(parentNode)) totalNeighborList.remove(parentNode);
		if(totalNeighborList.contains(vertex)) totalNeighborList.remove(vertex);

		//get neighbor nodes of the target vertex
		Set<Integer> targetNeighbors = getIntegerNeighborNodeRepresentation(neighbors);
		
		//if targetNeighbors contains root, parentNode or any node from parent parentNodeNeighborList, remove them
		if(targetNeighbors.contains(root)) totalNeighborList.remove(root);
		if(targetNeighbors.contains(parentNode)) totalNeighborList.remove(parentNode);
		
		for(int pNode : parentNodeNeighborList)
		{
			if(targetNeighbors.contains(pNode)) targetNeighbors.remove(pNode);
		}
		
		//get each item from target vertex neighbor list and check them if they are located in tatalNeighborList 
		for(int node : targetNeighbors)
		{
			if(totalNeighborList.contains(node)) return true;
		}
		
		return false;
  }
	//helper method to find the a vertex parent node start from the root
	private int getParentNode(int targetNode, int root)
	{
		if(targetNode == root)
			return root;
		
		Queue<Integer> nodesToSearch = new LinkedList<>();
		Set<Integer> visitedNode = new HashSet<>();
		
		//add the root node to nodeToSearch
		nodesToSearch.add(root);
		
		//loop till root is not empty
		while(!nodesToSearch.isEmpty())
		{	
			int vertex = nodesToSearch.poll();
			visitedNode.add(vertex);
			
			//get the neighbor of vertex
			Set<Integer> neighborNodeList = getIntegerNeighborNodeRepresentation(this.getNeighbors(vertex));
			
			if(neighborNodeList.contains(targetNode)) 
				return vertex;
			
			else
			{
				for(int node : neighborNodeList)
				{
					if(!visitedNode.contains(node))
					{
						nodesToSearch.add(node);
					}
				}
			}
		}
		return 0;
	}
	//simple helper method to convert neighbor edges to simple node representation
	private HashSet<Integer> getIntegerNeighborNodeRepresentation(HashSet<Edge> neighborList)
	{
		HashSet<Integer> neighborNodes = new HashSet<>();
		
		for(Edge edge : neighborList)
		{
			neighborNodes.add(edge.getEndNode());
		}
		
		return neighborNodes;
 	}
	//get neighbor list of a given vertex
	private HashSet<Edge> getNeighbors(int vertex)
	{
		if(!adjListsMap.containsKey(vertex)) return null;
		
		HashSet<Edge> neiborList = adjListsMap.get(vertex);;
 		return neiborList;
	}
	//helper method check if neighbors list contain any unvisited node
	private boolean ifHasUnvisitedNeighobrs(HashSet<Edge> neighbors, Set<Integer> visited)
	{

		for(Edge edge : neighbors)
			//add edgeNode the queue
			if(!visited.contains(edge.getEndNode())) return true;
		
		return false;
	}
	//print edge with its edge details
	public void printGraphWithDetailedEdges()
	{
		//System.out.println("size of the adjListMap: " + adjListsMap.size());
		
		for(int node : adjListsMap.keySet())
		{
			//get the edges of each node
			HashSet<Edge> edges = adjListsMap.get(node);
			
			for(Edge edge : edges)
			{
				System.out.println(edge);
			}
		}
		
		//System.out.println("Queue of the nodes: " + pathVertexList);
	}
	//print edge with its edge details
	public void printGraph()
	{
		System.out.println("size of the adjListMap: " + adjListsMap.size());
		
		for(int node : adjListsMap.keySet())
		{
				System.out.println(node + " --> " + adjListsMap.get(node));
		}
		
		//System.out.println("Queue of the nodes: " + pathVertexList);
	}
	public void printBFSRepresentationGraph(int root)
	{
		//Map<Integer, Vertex> vertexLevelMap;

		System.out.println("root : " + root);
		//System.out.println(g + "\n\n");
		Map<Integer, HashSet<Edge>> graphAdjListsMap = getAdjListsMap();
		
		for(int vertexID : graphAdjListsMap.keySet())
		{
			//get the vertax
			Vertex vertex = vertexLevelMap.get(vertexID);
			System.out.println("Vertex: " +vertex + " egdes--> " + graphAdjListsMap.get(vertexID));
		}
		System.out.println("\n\n");
		//System.out.println("Queue of the nodes: " + this.nodeQueue);
	}
	public int distanceBetweenTwoNodes(int start, int goal)
	{
		return bfs(start, goal).size();
	}
	public List<Integer> bfs(int start, int goal)
	{ 
		HashSet<Integer> visited = new HashSet<>();
		Queue<Integer> toExplore = new LinkedList<>();
		HashMap<Integer, Integer> parentMap = new HashMap<>();
		
		toExplore.add(start);
		
		boolean found = false;
		
		while (!toExplore.isEmpty()) 
		{ 
			int curr = toExplore.remove();
			
			if (curr == goal) //if the goal location is found
			{
				found = true;
				break;
			}
			
			ArrayList<Integer> neighbors = new ArrayList<Integer>(this.getIntegerNeighborNodeRepresentation(getNeighbors(curr))); 
				
			Iterator<Integer> it = neighbors.iterator();
			while(it.hasNext())
			{
				int next = it.next();
				
				if(!visited.contains(next))
				{
					visited.add(next);
					parentMap.put(next, curr);
					toExplore.add(next);
				}
			}
		}
		if (!found) 
		{
			System.out.println("No path exists");
			return null;
		}
		// reconstruct the path
		 return constructPath(start, goal, parentMap); 
	}
	private static List<Integer> constructPath(int start, int goal, Map<Integer, Integer> parentMap) 
	{
		LinkedList<Integer> path = new LinkedList<>();
		int curr = goal;
		while (curr != start) 
		{
			path.addFirst(curr);
			curr = parentMap.get(curr); 
		}
		path.addFirst(start);
		return path;
	}
}
