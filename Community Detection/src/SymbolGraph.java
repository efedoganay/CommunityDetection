

public class SymbolGraph {
    private ST<String, Integer> st;  // string -> index
    private String[] keys;           // index  -> string
    private EdgeWeightedGraph graph;             // the underlying graph
    private static int counter=0;

    public SymbolGraph(String filename, String delimiter) {
    	 st = new ST<String, Integer>();

         // First pass builds the index by reading strings to associate
         // distinct strings with an index
         In in = new In(filename);
         // while (in.hasNextLine()) {
         in.readLine();
         while (!in.isEmpty()) {
             String[] a = in.readLine().split(delimiter);
             for (int i = 0; i < 2 ; i++) {
                 if (!st.contains(a[i]))
                     st.put(a[i], st.size());
             }
         }
        StdOut.println("Done reading " + filename);

         // inverted index to get string keys in an aray
         keys = new String[st.size()];
         for (String name : st.keys()) {
             keys[st.get(name)] = name;
         }

         // second pass builds the graph by connecting first vertex on each
         // line to all others
         graph = new EdgeWeightedGraph(st.size());
         in = new In(filename);
         while (in.hasNextLine()) {
             String[] a = in.readLine().split(delimiter);
             Edge myEdge= new Edge(st.get(a[0]),st.get(a[1]),Double.parseDouble(a[2]));
                 graph.addEdge(myEdge);
                 counter++;
        }
    }

    public boolean contains(String s) {
        return st.contains(s);
    }

    @Deprecated
    public int index(String s) {
        return st.get(s);
    }

    public int indexOf(String s) {
        return st.get(s);
    }

    @Deprecated
    public String name(int v) {
        validateVertex(v);
        return keys[v];
    }

    public String nameOf(int v) {
        validateVertex(v);
        return keys[v];
    }

    @Deprecated
    public EdgeWeightedGraph G() {
        return graph;
    }

    public EdgeWeightedGraph graph() {
        return graph;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
         int V = graph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public static void main(String[] args) {
    	/*String filename = "https://raw.githubusercontent.com/uzay00/CMPE232/master/Project/babel.txt";
 	    String delimeter = ",";
 	    SymbolGraph sg = new SymbolGraph(filename,delimeter);
 	    EdgeWeightedGraph graph = sg.graph();*/
 	   
 	   
    }
}