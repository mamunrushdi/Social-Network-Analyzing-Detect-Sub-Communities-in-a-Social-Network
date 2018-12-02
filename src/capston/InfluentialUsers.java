package capston;

/**
 * @author MD AL MAMUNUR RASHID
 * 
 * This class detecting the possible influential users of the network
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class InfluentialUsers {

	public static void main(String[] args) 
	{
		//load the graph
		Graph network = new Graph();
		GraphLoader.loadGraph(network, "data/main_test.txt");
		
		List<Integer> influentialUserList = getInfluentialUserList(network);
		System.out.println(influentialUserList);
	}

	//user is potential influential user if he has friends more than 5% of the network user
	//this percentage must be dynamic depending on the size of the social network
	//I've set this decided percentage for group has members in the range of 10,000 - 20, 0000
	private static List<Integer> getInfluentialUserList(Graph network)
	{
		List<Integer> influentialUserList = new ArrayList<>();
		//get the total number of user in the graph
		int totalNumberOfUser = network.getNumVertices();

		//System.out.println(g + "\n\n");
		Map<Integer, HashSet<Edge>> graphAdjListsMap = network.getAdjListsMap();
		
		for(int vertexID : graphAdjListsMap.keySet())
		{
			int numberNeighbors = graphAdjListsMap.get(vertexID).size();
			if(numberNeighbors >= (totalNumberOfUser * 5)/100) influentialUserList.add(vertexID);
		}
		
		return influentialUserList;
				
	}
}
