package GraphTheoryGroup.ComboVisTools;
import java.util.*;

public class Graph {
    protected Node head;
    protected ArrayList<Node> nodes;
    protected Map<Character, char[]> adjList;
    protected char[] nodeData;
    protected Map<Character, Node> charToNode;
    protected ArrayList<Integer> degreeSequence;

    public Graph(String input) {
        this.adjList = parseInput(input);
        this.nodeData = parseNodeData();
        this.nodes = parseAdjList();
        this.head = nodes.get(0);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public Node getHead() {
        return head;
    }

    public Map<Character, char[]> getMap() {
        return adjList;
    }

    public char[] getNodeData() {
        return this.nodeData;
    }

    public ArrayList<Integer> parseDegreeSequence() throws NullPointerException {
        ArrayList<Integer> vtr = new ArrayList<>();

        for (char[] i : this.getMap().values()) {
            vtr.add(i.length);
        }

        Collections.sort(vtr);
        return vtr;
        
    }
    public Map<Character, char[]> parseInput(String input) throws IllegalArgumentException {
        // input format will be something like 1 2 3 4 7|2 1 5 7 8| ... etc
        // where the adjaceny list would look something like {1: 2, 3, 4, 7} {2: 1, 5, 7, 8}, etc.
        Map<Character, char[]> vtr = new HashMap<>();
        String[] spl = input.split("l");
        // System.out.println((spl.length));
        char[] vtrs = new char[spl.length];

        int index = 0;
        for (String node : spl) {
            String[] curr = node.split("m");
            try {
                vtr.put(Character.valueOf(node.charAt(0)), stc(Arrays.copyOfRange(curr, 1, curr.length)));
            } catch (IllegalArgumentException e) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAEIAE");
            }
            vtrs[index] = node.charAt(0);
            index++;
        }
        
        
        // System.out.println(index);
        return vtr;
    }
    
    public char[] stc(String[] s) {
        char[] vtr = new char[s.length];
        for (int i = 0; i < s.length; i++) {
            vtr[i] = s[i].charAt(0);
        }

        return vtr;
    }

    public ArrayList<Node> DFSNode(Node root, ArrayList<Node> vtr, Set<Node> visited_set) {
        // visited_set = new HashSet<>();
        visited_set.add(root);
        vtr.add(root);
        // System.out.println(Character.toString(root.getValue()));
        for (Node n : root.getAdjList()) {
            if (!visited_set.isEmpty() && !visited_set.contains(n)) {
                System.out.println("Visiting " + Character.toString(root.getValue()) + " to " + Character.toString(n.getValue()));
                DFSNode(n, vtr, visited_set);
            }
        }
        return vtr;
    }

    public ArrayList<Node> BFSNode(Node root) {

        ArrayList<Node> vtr = new ArrayList<>();
        Queue<Node> q = new LinkedList<>();
        Set<Node> visited_set = new HashSet<>();

        visited_set.add(root);
        vtr.add(root);
        q.add(root);

        while(!q.isEmpty() && !(visited_set.size() == nodes.size())) {
            Node n = q.poll();
            for (Node a : n.getAdjList()) {
                if (!visited_set.contains(a)) {
                    visited_set.add(a);
                    vtr.add(a);
                    q.add(a);
                }
            }
        }

        return vtr;
    }

    public ArrayList<Node> BFSRoot() {
        return BFSNode(this.head);
    }
    
    public char[] parseNodeData() {
        Set<Character> keySet = adjList.keySet();

        // Convert the set of characters into a char[]

        // System.out.println("size: " + keySet.size());
        char[] charArray = new char[keySet.size()];
        int index = 0;
        for (char c : keySet) {
            charArray[index++] = c;
        }

        return charArray;
    }
    
    public ArrayList<Node> parseAdjList() {
        Map<Character, Node> hm = new HashMap<>();
        Set<Character> exChar = new HashSet<>();
        // ArrayList<Node> vtr = new ArrayList<>();
        for (char c : nodeData) {
            // System.out.println(Character.toString(c));
            Node curr = null;

            if (!exChar.contains(c)) {
                exChar.add(c);
                curr = new Node(c);
                hm.put(c, curr);
            } else {
                curr = hm.get(c);
            }
            // System.out.println("-------");
            for (char adj : adjList.get(c)) {
                // System.out.println(adj);
                Node adjNode = null;
                if (!exChar.contains(adj)) {
                    // System.out.println("test");
                    exChar.add(adj);
                    adjNode = new Node(adj);

                    hm.put(adj, adjNode);
                    // System.out.println("size: " + hm.size());
                }
                // System.out.println(hm.get(adj));
                curr.addAdj(hm.get(adj));
                // System.out.println(curr.getAdjList());

            }
        }

        this.charToNode = hm;
        ArrayList<Node> vtr = new ArrayList<>(hm.values());

        return vtr;
        
    }

    public String nodesToString(ArrayList<Node> nodes) {
        String vtr = "Graph nodes:";

        for (Node n : nodes) {
            vtr = vtr + ", " + Character.toString(n.getValue());
        }

        return vtr;
    }

    // format of return value will be [boolean, Set<Node>, Set<Node>]
    // [is it bipartite?, 1 coloring, 2 coloring]
    public Object[] checkBipartite() {

        Set<Node> L = new HashSet<>();
        Set<Node> R = new HashSet<>();

        Queue<Object[]> q = new LinkedList<>(); // [Node, String] -> [Node, L/R]
        Set<Node> visited_set = new HashSet<>();

        visited_set.add(this.head);
        Object[] test = {this.head, "L"};
        q.add(test);

        while(!q.isEmpty() && !(visited_set.size() == nodes.size())) {

            Object[] n = q.poll();
            Node curr = (Node) n[0];
            Set<Node> curr_side = ((String) n[1]).equals("L") ? L : R;

            visited_set.add(curr);
            curr_side.add(curr);
            for (Node a : curr.getAdjList()) {
                if (visited_set.contains(a)) {
                    if (curr_side.contains(a)) { // adjacent node can't be on curr's side
                        Object[] vtr = {false, L, R};
                        return vtr;
                    }
                } else {
                    // if not visited yet, we add [adjacent_node, opposite_side] to queue
                    String opposite_side = ((String) n[1]).equals("L") ? "R" : "L";
                    Object[] temp = {a, opposite_side};
                    q.add(temp);
                }
            }
        }

        Object[] vtr = {true, L, R};
        return vtr;
    }

    // return value will be <Set<Node>, Set<Node>, ...>
    public ArrayList<Set<Node>> chromaticNumber() {

        // Map<Node, Set<Integer>> used_colors = new HashMap<>();
        ArrayList<Set<Node>> vtr = new ArrayList<>();
        // start a BFS from the root

        Set<Node> visited_set = new HashSet<>();
        Queue<Node> q = new LinkedList<>();

        q.add(this.head);
        // visited_set.add(this.head);

        // Set<Node> first_color = new HashSet<>();
        // first_color.add(this.head);
        // vtr.add(first_color);

        while (!(q.isEmpty() && visited_set.size() == this.nodeData.length)) {
            Node curr = q.poll();
            
            
            Set<Integer> used_colors = new HashSet<>();
            for (Node a : curr.getAdjList()) {
                
                if (visited_set.contains(a)) {
                    int adj_color = whichColor(a, vtr); // get the color of adjacent node
                    used_colors.add(adj_color); // curr cannot be that color

                } else {
                    q.add(a);
                }
            }

            // check if we need to assign a brand new color to CURR
            if (used_colors.size() == vtr.size()) {
                // create a brand new color
                Set<Node> new_color = new HashSet<>();
                new_color.add(curr);
                vtr.add(new_color);

            } else { // we don't need to assign a new color to curr
                // assign existing color to curr
                vtr.get(assignExistingColor(curr, vtr, used_colors)).add(curr);
            }

            visited_set.add(curr);
        }
        
        return vtr;
    }

    // returns the lowest possible color group for curr
    // assumes that we DON'T need to create a new color
    private int assignExistingColor(Node curr, ArrayList<Set<Node>> colors, Set<Integer> used_colors) {
        // there are colors.size() amount of different colors (indexed from 0 ... colors.size() - 1)
        // used colors for a certain node tells me which colors the current node CANT be
        for (int i = 0; i < colors.size(); i++) {
            if (!used_colors.contains(i)) {
                return i;
            }
        }

        throw new IllegalArgumentException("something went wrong in assign existing color.");
    }


    private int whichColor(Node curr, ArrayList<Set<Node>> colors) {
        for (int i = 0; i < colors.size(); i++) {
            // System.out.println(curr.getValue());
            if ((colors.get(i)).contains(curr)) {
                return i;
            }
        }

        throw new NoSuchElementException("Did not find the node in any color.");
    }
}
