package com.cs2025.SignalFlowGraphBackend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/connectToApi")
public class Controller {

    public double[][][] graph;

    @PostMapping("/sendAdjList")
    public void receivedAdjList(@RequestBody double[][][] adjacencyList) {
        graph = adjacencyList;
        int size = graph.length;
        for(int i = 0; i < size; i++) {
            int size2 = graph[i].length;
            System.out.print("Node " + i + ": ");
            for(int j = 0; j < size2; j++) {
                System.out.print("(" + (int)graph[i][j][0] + ", " + graph[i][j][1] + "), ");
            }
            System.out.println();
        }
    }

}
