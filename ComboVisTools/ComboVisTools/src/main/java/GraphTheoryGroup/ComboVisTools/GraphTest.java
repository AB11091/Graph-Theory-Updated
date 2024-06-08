package GraphTheoryGroup.ComboVisTools;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
public class GraphTest {
    // public static void main(String[] args) {
    //     String input = "1 2 3 4 7|2 1 5 7 8|3 1 4 6 7|4 1 3 6 a|5 2 7 8 9|6 3 4 9 a|7 1 2 3 5|8 2 5 9 a|9 5 6 8 a|a 4 6 8 9";
    //     // input = "1 2 3|2 1 4 5 6|3 1 6|4 2 7|5 2 7|6 2 3|7 4 5"; // sourabh graph
    //     input = "a b c d|b a e f|c a g|d a h|e b i|f b j|g c k|h d l|i e m|j f n|k g|l h|m i|n j\r\n";
    //     Graph graph = new Graph(input);

    //     System.out.println(graph.nodesToString(graph.getHead().getAdjList()));
    //     ArrayList<Node> bfs = new ArrayList<>();
    //     Set<Node> vs = new HashSet<>();
    //     bfs = graph.BFSNode(graph.getHead());
    //     System.out.println(graph.nodesToString(bfs));

    //     Write writer =  new Write(graph, "main.tex");
    //     writer.groundWork();
    //     writer.parseGraph();
    //     writer.close();
    // }

    public static void main(String[] args) {
        Graph k10 = new CompleteGraph("abcde");

        String input = "a b c d|b a e f|c a g|d a h|e b i|f b j|g c k|h d l|i e m|j f n|k g|l h|m i|n j";
        // input = "1 2 3|2 1 4 5 6|3 1 6|4 2 7|5 2 7|6 2 3|7 4 5";
        // 1m2m3l2m1m4m5m6l3m1m6l4m2m7l5m2m7l6m2m3l7m4m5
        input = "1 2 3 4 7|2 1 5 7 8|3 1 4 6 7|4 1 3 6|5 2 7 8 9|6 3 4 9|7 1 2 3 5|8 2 5 9|9 5 6 8";
        Graph graph = new Graph(input);
        Write writer = new Write(graph, "main.tex");

        ArrayList<Set<Node>> colors = graph.chromaticNumber();
        System.out.println(colors);
        // Object[] o = graph.checkBipartite();
        // Boolean bipartite = (Boolean) o[0];
        // Set<Node> L = (Set<Node>) o[1];
        // Set<Node> R = (Set<Node>) o[2];

        // System.out.println((Boolean) o[0]);

        // if (bipartite) {
        //     writer.groundWork();
        //     writer.parseBipartiteGraph(L, R);
        //     writer.close();
        // }

        writer.groundWork();
        writer.parseColoredGraph(colors);
        writer.close();
        
        
    }
}
