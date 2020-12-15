import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.ListIterator;
public class DijkstraUndirectedSP {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    static double dweight;
    static SeparateChainingHashST<Integer,String>myTable;
    static SeparateChainingHashST<Integer, Integer>myTable2;
    static int v1,v2,count;
    static EdgeWeightedGraph myGraph;
    static ArrayList<Edge>edges;
    
    
    public DijkstraUndirectedSP(EdgeWeightedGraph G, int s) {
        for (Edge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adj(v))
                relax(e, v);
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update pq if changed
    private void relax(Edge e, int v) {
        int w = e.other(v);
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<Edge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<Edge> path = new Stack<Edge>();
        int x = v;
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[x]) {
            path.push(e);
            x = e.other(x);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e = v-w:            distTo[w] <= distTo[v] + e.weight()
    // (ii) for all edge e = v-w on the SPT: distTo[w] == distTo[v] + e.weight()
    private boolean check(EdgeWeightedGraph G, int s) {

        // check that edge weights are nonnegative
        for (Edge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v-w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v-w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            Edge e = edgeTo[w];
            if (w != e.either() && w != e.other(e.either())) return false;
            int v = e.other(w);
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
    //FIND ALL SHORTEST PATHS
    private static SeparateChainingHashST<Tuple, Tuple2> findAllShortestPaths(EdgeWeightedGraph G){
    	SeparateChainingHashST<Tuple, Tuple2>sp = new SeparateChainingHashST<Tuple,Tuple2>();
    	
    	for (int i = 0; i < G.V(); i++) {
			DijkstraUndirectedSP path = new DijkstraUndirectedSP(G, i);
			for (int j = 0; j < G.V(); j++) {
				if(j!=i){
					if(path.hasPathTo(j) == false){
						continue;
					}
				Tuple myTuple=new Tuple(i,j);
				Tuple2 myTuple2=new Tuple2(path.pathTo(j),path.distTo(j));
				sp.put(myTuple, myTuple2);				
				}
			}
		}
    	return sp;
    }
    //GET EDGES FROM PATH
    private static ArrayList<Edge>  getEdgesFromPath(Iterable<Edge>path){
    	edges = new ArrayList<Edge>();
    	for (Edge e : path) {
			edges.add(e);
		}
    	return edges;
    }
    //NUMBER OF SHORTEST PATHS PASSING THROUGH EDGES
    private static SeparateChainingHashST<Edge, Integer> numberOfShortestPathsPassingThroughEdges(EdgeWeightedGraph G){
    	SeparateChainingHashST<Tuple, Tuple2> sp = new SeparateChainingHashST<Tuple,Tuple2>();
    	sp=findAllShortestPaths(G);
    	SeparateChainingHashST<Edge, Integer> onsp = new SeparateChainingHashST<Edge,Integer>();
    	for (Tuple t : sp.keys()) {
			for (Edge edge : getEdgesFromPath(sp.get(t).getEdge())) {
				if(onsp.contains(edge)){
					int a = onsp.get(edge);
					onsp.put(edge, a+1);
				}
				else{
					onsp.put(edge, 1);
				}
			}
		}
    	return onsp;
    }
    //GET EDGE WITH THE MAXIMUM SHORTEST PATHS
    private static Edge getEdgeWithMaxSP(EdgeWeightedGraph G){
    	SeparateChainingHashST<Edge, Integer> onsp = new SeparateChainingHashST<Edge,Integer>();
    	onsp = numberOfShortestPathsPassingThroughEdges(G);
    	ArrayList<Integer>sortList = new ArrayList<>();
    	Edge edgeMaxSP= new Edge(0, 0, 0);
    	for (Edge edge : onsp.keys()) {
    		sortList.add(onsp.get(edge));
    	}	
    		Collections.sort(sortList);
    		int max=sortList.get(sortList.size()-1);
		
    	for (Edge edge : onsp.keys()) {
			if(onsp.get(edge)==max){
				edgeMaxSP=edge;
			}
		}
    	return edgeMaxSP;
    }
    
    //REMOVE EDGE THAT MAXIMUM SHORTEST PATHS PASSING THROUGH IT
    private static EdgeWeightedGraph removeMax(EdgeWeightedGraph G){
    	Edge rmvEdge = getEdgeWithMaxSP(G);
    	G.removeEdge(rmvEdge);
    	return G;
    }
    

    public static void main(String[] args) {
    	String filename = "https://raw.githubusercontent.com/uzay00/CMPE232/master/Project/babel.txt";
 	    String delimeter = ",";
 	    SymbolGraph sg = new SymbolGraph(filename,delimeter);
 	    EdgeWeightedGraph graph = sg.graph();
 	    DijkstraUndirectedSP myPath = new DijkstraUndirectedSP(graph, 0);
 	    
 	   for (int i = 1; i <= 10; i++) {
			removeMax(graph);
			myPath = new DijkstraUndirectedSP(graph, 0);
			CC numberOfComponents = new CC(graph);
			System.out.println(+i+"th"+" Removal"+" Number of Components:"+" "+numberOfComponents.count());
			System.out.println(sg.nameOf(getEdgeWithMaxSP(graph).either()) + " - " + sg.nameOf(getEdgeWithMaxSP(graph).other(getEdgeWithMaxSP(graph).either())) + " " + getEdgeWithMaxSP(graph).weight());
			System.out.println(numberOfShortestPathsPassingThroughEdges(graph).get(getEdgeWithMaxSP(graph)));
			if(i == 10){
				System.out.println(numberOfComponents.count() + " " + "Communities are found.");
			}
    }
    
   
    

}
}
