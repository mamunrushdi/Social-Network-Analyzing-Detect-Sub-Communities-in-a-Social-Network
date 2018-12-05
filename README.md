# Capstone: Analyzing (Social) Network Data

### This is Coursera Capston Project for University of California San Diego on Social Network analyzing using Java programming.

#### In this project my assignments tasks were:
### 1. Find the minimum number of user of the social network who need to share a post to be seen by everyone. Also print those users.
### 2. Detect the sub communities in the social network with minimum number of users. 

## Data
I started the assignments with the following data set. 
Dataset : Facebook Friendship between students at USCD in 2005
Every line of the file describes a particular friendship between two users.
This is anonymize data set, instead of name, user id has been used to identify an user.
In the dataset, there are 14747 vertices and 886442 edges.
This dataset came along with Capston warm up assignment.

## The data set will be represented in the graph as Adjacency List as the graph will be a spare graph.

## Algorithm Analysis:
### For Task One:
##### Algorithm:
1. Make a weighted node list of all node where weight is the total number of neighbors a node. 
1. Each node will consider only its un-visited neighbors. (If a neighbor node already been added to to the neighbors list of earlier node, then it will not be added.)
1. Convert the node list to a priority queue based on regarding its size of un-visited neighbors.
1. Create counter for posted node number: postedNodeCounter (node who will post the message)
1. Remove the top node, X from the queue, mark it as posted node and it and its neighbor nodes visited.
	1. Increase postedNodeCounter by 1
	1. Check if any neighbor node, Y of X has at least 3 (three) friends who has posted/shared the message.
		1. If yes, create a new priority queue2  and Y to it
		1. Remove Y, mark it as posted node and it and its neighbor nodes visited. 
		1. Remove Y from the priority queue
		1. Check if any neighbor node, Z of Y has at least 3 (three) friends who has posted/shared the message.
		1. If yes, add Z queue2
		1. Repeat this process until queue2 is empty

1. Continue this until node’s of the priority queue is empty.
1. Return postedNodeCounter

### Time complexity: O(|v|^2 + |v|), 
#### where v is the total number of vertex in the graph

# For Second Task: To solve the second task, detecting sub-communities I've used [Grivan Newman algorithm](https://en.wikipedia.org/wiki/Girvan%E2%80%93Newman_algorithm).
	
## Grivan Newman Algorithm in details:
  1. Get BFS (Breadth First Search) representation of the graph from each each vertex of the graph)
  2. Label each node by the number of the shortest paths that reach it from the root
  3. Set credit of each node and DAG edges (This part has also been implemented in Graph class) as this step
   will be repeated for every graph to detect communities)
  
 ### Time complexity: O(|v|^3)

### Testing:
I've stated the project with the above mentioned Dataset. Unfortunately my computer is not capable of calculating big computation that's why I've created three small
data set where vertcies where from 7 - 15 and edges 9 -30. In this small test my program always produce 
right answer. 
#### Now I'm preparing to test my program with much more Dataset.
