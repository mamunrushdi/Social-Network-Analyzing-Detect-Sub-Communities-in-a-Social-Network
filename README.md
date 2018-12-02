# Capstone: Analyzing (Social) Network Data

## This is Coursera Capston Project for University of California San Diego on Social Network analyzing using 
## Java programming.

##In this project my assignments tasks were:
	###1. Find the minimum number of user of the social network who need to share a post to be seen by everyone.
	###	  Also print these users.
	###2. Detect the sub communities in the social network with minimum number of users. 

##Data
I started the assignments with the following data set. 
Dataset : Facebook Friendship between students at USCD in 2005
Every line of the file describes a particular friendship between two users.
This is anonymize data set, instead of name, user id has been used to identify an user.
In the dataset, there are 14747 vertices and 886442 edges.
This dataset came along with Capston warm up assignment.

## The data set will be represented in the graph as Adjacency List as the is spare graph.

## Algorithm Analysis:
###For Task One:
Algorithm:
Make a weighted node list of all node where weight is the total number of neighbors a node. 
Each node will count only its un-visited neighbors. 
If a neighbor node already been added to to the neighbors list of earlier node, then it will not be added.
Convert the node list to a priority queue base on its size of un-visited neighbors.
Create counter for posted node number: postedNodeCounter
Remove the top node, X from the queue, mark it as posted node and it and its neighbor nodes visited.
Increase postedNodeCounter by 1
Check if any neighbor node, Y of X has at least 3 (three) friends who has posted/shared the message.
If yes, create a new priority queue2  and Y to it
Remove Y, mark it as posted node and it and its neighbor nodes visited. 
Remove Y from the priority queue
Check if any neighbor node, Z of Y has at least 3 (three) friends who has posted/shared the message.
If yes, add Z queue2
Repeat this process until queue2 is empty

Continue this until node’s of the priority queue is empty.
Return postedNodeCounter

###Time complexity: O(|v|^2 + |v|), 
####where v is the total number of vertex in the graph

###For Second Task:
	To solve the second task, detecting sub-communities I've used Grivan Newman algorithm.
	
## Grivan Newman Algorithm in details:
 * 
 * 1. Get BFS (Breadth First Search) representation of the graph from each each vertex of the graph)
 * 2. Label each node by the number of the shortest paths that reach it from the root
 * 3. Set credit of each node and DAG edges (This part has also been implemented in Graph class) as this step
 * 	  will be repeated for every graph to detect communities)
 * 
 * Time complexity: O(|v|^3)
 * 
 * Graph example:
  	
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

1. Read social network data from data file;
2. Calculate average ratings;
3. Calculate how similar a given rater is to another user based on ratings; and
4. Recommend movies to a given user based on ratings. 

###Testing:
I've stated the project with the above mentioned Dataset. Unfortunately my computer is not capable of calculating big computation that's why I've created three small
data set where vertcies where from 7 - 15 and edges 9 -30. In this small test my program always produce 
right answer. 
####Now I'm preparing to test my program with much more Dataset.

