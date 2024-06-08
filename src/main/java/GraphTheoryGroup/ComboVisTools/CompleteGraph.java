package GraphTheoryGroup.ComboVisTools;
import java.util.*;
public class CompleteGraph extends Graph {
    private int n;

    // input will just be the names of the nodes with no spaces
    // no adjacencies are needed because a given node connects to all others in a complete graph
    public CompleteGraph(String input) {
        super(input);
        parseCompleteInput(input);
        parseCompleteAdjList();
    }

    public void parseCompleteInput(String input) {
        char[] nodes = input.toCharArray();
        Set<Character> nodeSet = new HashSet<>();
        boolean repeat = false;
        ArrayList<Node> nodeArrayList = new ArrayList<>();

        Map<Character, Node> charToNode = new HashMap<>();

        String repeatMessage = "The following repeated nodes were found in your input: ";
        for (char c : nodes) {
            if  (nodeSet.contains(c)) {
                repeat = true;
                repeatMessage += String.format("%c, ", c);

            } else {
                nodeSet.add(c);
                charToNode.put(c, new Node(c));
                nodeArrayList.add(new Node(c));
            }
        }

        repeatMessage += ". They have been ommitted.";
        if (repeat) {
            System.out.println(repeatMessage);
        }
        char[] noRepeats = new char[nodeSet.size()];

        int i = 0;
        for (Character c : nodeSet) {
            noRepeats[i] = c;
            i++;
        }

        super.charToNode = charToNode;
        super.nodeData = noRepeats;
        // super.nodes = nodeArrayList;
        // super.head = super.nodes.get(0);
        this.n = noRepeats.length;

        // creating adjacency list will be in a separate method

    }

    public void parseCompleteAdjList() {
        Map<Character, char[]> vtr = new HashMap<>();

        for (int i = 0; i < this.nodeData.length; i++) {

            char[] currentAdj = new char[n - 1];
            boolean removed = false;
            for (int j = 0; j < this.nodeData.length; j++) {
                if ((i == j)) {
                    removed = true;
                    continue;
                } else {
                    if (!removed) {
                        currentAdj[j] = this.nodeData[j];
                    } else {
                        currentAdj[j - 1] = this.nodeData[j];
                    }
                }
            }

            vtr.put(this.nodeData[i], currentAdj);
        }

        super.adjList = vtr;

        ArrayList<Node> nodes = new ArrayList<>();
        for (char c : vtr.keySet()) {
            Node node = super.charToNode.get(c);
            for (char adj : super.adjList.get(c)) {
                node.addAdj(super.charToNode.get(adj));
            }

        }

        nodes = new ArrayList<>(charToNode.values());

        super.nodes = nodes;
        super.head = nodes.get(0);
        System.out.println("test");        
    }
}
