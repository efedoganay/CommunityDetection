# Community Detection

## Steps Involved:

1. Import the data from https://moviegalaxies.com/movies/download/92/Babel

2. Create the graph.

3. Repeat 10 Times:
    - Find shortest path between every pair in the graph, using Dijkstra's shortest path algorithm.
    - For every edge, count the number of shortest paths passing through.
    - Find and remove the edge on which the maximum number of shortest paths pass.
   
## Notes:

Most of the supplementary codes are taken from https://algs4.cs.princeton.edu/code/

The main implementation is in [DijkstraUndirectedSP.java](../master/Community%20Detection/src/DijkstraUndirectedSP.java)

## Example Animation:

![Alt Text](https://media.giphy.com/media/8FF7aL37fO1zX7v4iT/giphy.gif)
