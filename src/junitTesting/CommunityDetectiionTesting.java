package junitTesting;

/**
 * @author MD AL MAMUNUR RASHID
 * 
 * This class use JUnit to test the community detection.
 * 
 * This class test the following tests: 
 * 									a. Does graph loaded graph properly
 * 									a. Does Community Detection class detect correct number of sub-communites							
 * 												
 */
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import capston.CommunityDetection;
import capston.Graph;
import capston.GraphLoader;

public class CommunityDetectiionTesting 
{
	static Graph graph = new Graph();

	@BeforeClass
	public static void setup()
	{
		//load the graph
		GraphLoader.loadGraph(graph, "data/main_test.txt");

	}
	
	//graph's vertex and edge number
	int vertices = 7;
	int edges = 9;
	//test if the graph loads properly
	@Test
	//testing whether the graph has loaded the correct number of vertices
	public void testVerticesNumber()
	{
		assertEquals(vertices, graph.getNumVertices());
	}
	@Test
	//testing whether the graph has loaded the correct number of edges
	public void testEdgesNumber()
	{
		assertEquals(edges, graph.getNumEdges());
	}
	
	//Detect sub-communities with minimum number users
	@Test
	//testing whether community detection class has detected correct number of sub-community
	public void testGetSubComminites()
	{
		int minUsers = 3;
		
		//Create a CommunityDetection object
		CommunityDetection cd = new CommunityDetection(graph, minUsers);
		
		int totalSubCommunities = 2;
		
		assertEquals(totalSubCommunities, cd.getSizeOfSubCommunites());
	}
	
}
