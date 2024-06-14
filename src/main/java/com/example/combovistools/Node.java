package com.example.combovistools;
import java.util.ArrayList;
import java.util.HashSet;
public class Node {
    private char value;
    private ArrayList<Node> adj;
    private HashSet<Node> set;


    public Node(char data, ArrayList<Node> adj) {
        this.value = data;
        this.adj = adj;
        this.set = new HashSet<>();
    }

    public char getValue() {
        return value;
    }

    public ArrayList<Node> getAdjList() {
        return adj;
    }

    public HashSet<Node> getSet() {
        return set;
    }
    public Node(char data) {
        this.value = data;
        this.adj = new ArrayList<>();
        this.set = new HashSet<>();
    }

    public void addAdj(Node node) {
        if (!set.isEmpty() || !set.contains(node)) {
            this.adj.add(node);
            this.set.add(node);
        }
    }


    public boolean equals(Node node) {
        return (this.value == node.value);
    }
}
