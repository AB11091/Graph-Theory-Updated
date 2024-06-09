package GraphTheoryGroup.ComboVisTools;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import com.google.gson.Gson;
import java.util.*;
// import org.springframework.web.bind.annotation.;

@RestController
// @CrossOrigin(origins = {"http://localhost:8000"})
public class Controller {

    private Graph graph;
    private double[][] coords;


    @GetMapping("/api/hello")
    public String hello() {
        // System.out.println("hello sent");
        return "hello test from backend";
        
    }

    /*
     * By default, when the nodes of the graph are initialized, they will form a 'circle'
     * this method returns the coordinates of each one
     */
    @GetMapping("/api/VertexCoordinates")
    public double[][] getVertexCoords(@RequestParam String param) throws IllegalArgumentException {
        // System.out.println("vertex coords reached");

        if (this.graph == null) {
            this.graph = new Graph(param);
            System.out.println("---------------------------");
            Write w = new Write(this.graph);
            this.coords = w.regularPolygon(graph.nodes.size());
            return this.coords;
        } else {
            throw new IllegalArgumentException("Graph has already been created");
        }
        
    }

    /**
     * @return adjancency map of the graph 
     * @throws IllegalArgumentException if graph has not been created.
     * Users should not be able to see this error, as this endpoint will only be reached after a graph is created
     * it is for the developer
     */
    @GetMapping("/api/AdjMap")
    public Object getAdjMap(){

        if (this.graph == null) {
            System.out.println("Graph is null when trying to return Adjacency Map");
        } else {
            Gson gson = new Gson();
            return gson.toJson(this.graph.getMap());
        }

        return 1;

    }

    /**
     * resets the graph to null internally
     * frontend graph is erased separately
     * @return placeholder number, treat it as an exit code
     */
    @GetMapping("/api/removeGraph")
    public int removeGraph() {
        this.graph = null;
        return 1;
    }

    /**
     * @return Stringified JSON object of map {node : coordinates on SVG grid}
     */
    @GetMapping("/api/CoordinateMap")
    public String getCoordinateMap() {
        Map<Character, double[]> vtr = new HashMap<>();
        char[] chars = this.graph.getNodeData();
        for (int i = 0; i < chars.length; i++) {
            vtr.put((Character) chars[i], this.coords[i]);
        }

        Gson gson = new Gson();

        return gson.toJson(vtr);
    }

    @GetMapping("api/head")
    public char getGraphHead() {
        return this.graph.head.getValue();
    }
}
