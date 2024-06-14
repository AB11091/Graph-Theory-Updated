package com.example.combovistools;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
public class Write {
    private Graph graph;
    private char[][] spatial;
    public BufferedWriter writer;

    public Write(Graph input, String file_path) {
        this.graph = input;
        try {
            this.writer = new BufferedWriter(new FileWriter(file_path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // this.spatial = 
    }

    public Write(Graph input) {
        this.graph = input;
    }

    public void write(String s) {
        try {
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void groundWork() {
        this.write("\\documentclass{article}\n");
        this.write("\\usepackage{tikz}\n");
        this.write("\\input{header.tex}\n");
        this.write("\\begin{document}\n");
        this.write("\\begin{tikzpicture}[auto, node distance=3cm, every loop/.style={}," +  
            "thick,main node/.style={circle,draw,font=\\sffamily\\Large\bfseries}]\n");
    }
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        }
    }

    public void parseGraph() {
        // String vtr = "";
        // if (!graph.getMap().keySet().contains(curr)) {
        //     throw new IllegalArgumentException("Character " + curr + " is not in graph.");
        // }
        char[] nodes = this.graph.getNodeData();
        int n = nodes.length;
        double[][] nodeCoords = regularPolygon(n);
        for (int i = 0; i < n; i++) {
            double x = nodeCoords[i][0];
            double y = nodeCoords[i][1];
            String parsed_node = String.format("\\node[circle, draw] at (%f,%f) (%c) {%c};\n", x, y, nodes[i], nodes[i]);
            this.write(parsed_node);
        }

        for (Character i : this.graph.getMap().keySet()) {
            String currString = "";
            for (char c : this.graph.getMap().get(i)) {
                String parsed_edge = String.format("\\draw (%c) -- (%c);\n", i, c);
                this.write(parsed_edge);
            }
        }

        this.write("\\end{tikzpicture}\n");
        this.write("\\end{document}\n");
    }

    public void parseBipartiteGraph(Set<Node> L, Set<Node> R) {


        char[] nodes = this.graph.getNodeData();
        int n = nodes.length;
        double[][] nodeCoords = regularPolygon(n);
        for (int i = 0; i < n; i++) {
            double x = nodeCoords[i][0];
            double y = nodeCoords[i][1];

            String side = L.contains(this.graph.charToNode.get(nodes[i])) ? "blue" : "red";
            String parsed_node = String.format("\\node[circle, draw, fill=%s] at (%f,%f) (%c) {%c};\n", side, x, y, nodes[i], nodes[i]);
            this.write(parsed_node);
        }

        for (Character i : this.graph.getMap().keySet()) {
            String currString = "";
            for (char c : this.graph.getMap().get(i)) {
                String parsed_edge = String.format("\\draw (%c) -- (%c);\n", i, c);
                this.write(parsed_edge);
            }
        }

        this.write("\\end{tikzpicture}\n");
        this.write("\\end{document}\n");
    }

    //
    public void parseColoredGraph(ArrayList<Set<Node>> colors) {

        // to fill with random RGB, the format looks like
        // \definecolor{---name---}{rgb}{%d, %d, %d}
        Map<Integer, double[]> color_map = createColorMap(colors);

        for (int i = 0; i < color_map.keySet().size(); i++) {
            double[] curr_rgb = color_map.get(i);
            String tex_color = String.format("\\definecolor{color_%d}{rgb}{%f,%f,%f}\n"
                , i, curr_rgb[0], curr_rgb[1], curr_rgb[2]);
            this.write(tex_color);
        }  
        
        char[] nodes = this.graph.getNodeData();
        int n = nodes.length;
        double[][] nodeCoords = regularPolygon(n);

        for (int i = 0; i < n; i++) {
            double x = nodeCoords[i][0];
            double y = nodeCoords[i][1];

            int color = checkColor(this.graph.charToNode.get(nodes[i]), colors);
            String parsed_node = String.format("\\node[circle, draw, fill=color_%d] at (%f,%f) (%c) {%c};\n", color, x, y, nodes[i], nodes[i]);
            this.write(parsed_node);
        }

        for (Character i : this.graph.getMap().keySet()) {
            String currString = "";
            for (char c : this.graph.getMap().get(i)) {
                String parsed_edge = String.format("\\draw (%c) -- (%c);\n", i, c);
                this.write(parsed_edge);
            }
        }

        this.write("\\end{tikzpicture}\n");
        this.write("\\end{document}\n");

    }

    private int checkColor(Node curr, ArrayList<Set<Node>> colors) {
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).contains(curr)) {
                return i;
            }
        }

        throw new IllegalArgumentException(Character.toString(curr.getValue()) + " was not found in checkColor.");
    }

    private Map<Integer, double[]> createColorMap(ArrayList<Set<Node>> colors) {
        Map<Integer, double[]> color_map = new HashMap<>();

        for (int i = 0; i < colors.size(); i++) {
            double[] random_rgb = {(Math.random())
                , (Math.random()), (Math.random())};
            
            color_map.put(i, random_rgb);
        }

        return color_map;
    }
    
    public double[][] regularPolygon(int n) {
        double[][] vtr = new double[n][2];

        // n sided regular polygon of nodes inscribed in a circle of radius n/2
        // x^2 + y^2 = (n^2/4)
        for (int i = 0; i < n; i++) {
            double pi = Math.PI;
            double x = (225) * (Math.cos((2*pi*i)/n) + 1);
            double y = (225) * (Math.sin((2*pi*i)/n) + 1);

            vtr[i][0] = x;
            vtr[i][1] = y;
        }
        return vtr;
    }

    // public JSONArray regularPolygonJSON(int n) {
    //     return new JSONArray(Arrays.asList(regularPolygon(n)));
    // }
}
