package capston;

import java.util.List;

public class TestingCommunityDetection 
{
	public static void main(String[] args) 
	{
		//load the graph
		Graph graph = new Graph();
		GraphLoader.loadGraph(graph, "data/main_test.txt");
		
		graph.printGraph();
		
		CommunityDetection cd = new CommunityDetection(graph, 3);
		
		
		System.out.println("size: " + cd.getSizeOfSubCommunites());
		
	}

}
