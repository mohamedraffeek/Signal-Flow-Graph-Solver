package com.cs2025.SignalFlowGraphBackend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/connectToApi")
public class Controller {

    public double[][][] graph;
    public SignalFlowGraph object;
    public ForwardPaths forwardPaths;
    public Loops loops;
    public NonTouchingLoops nonTouchingLoops;

    @PostMapping("/sendAdjList")
    public void receivedAdjList(@RequestBody double[][][] adjacencyList) {
        this.object = new SignalFlowGraph(adjacencyList);
    }

    @PostMapping("/sendSource")
    public void receivedSource(@RequestBody int source) {
        System.out.println("source: " + source);
    }

    @PostMapping("/sendDestination")
    public void receivedDestination(@RequestBody int destination) {
        System.out.println("destination: " + destination);
    }

    @GetMapping("/getPathsWithGain")
    public List<Map<Double, List<Integer>>> getForwardPathsWithGain(){
        forwardPaths = new ForwardPaths(object.getSize(), object.getGraph());
        return forwardPaths.getWithGain();
    }

    @GetMapping("/getLoopsWithGain")
    public List<Map<Double, List<Integer>>> getLoopsWithGain(){
        loops = new Loops(object.getSize(), object.getGraph());
        return this.loops.getWithGain();
    }

//    @GetMapping("/getNon")
//    public List<List<Integer>> getNon(){
//        List<List<Integer>> loops = new ArrayList<>(2);
//        loops.add(new ArrayList<>());
//        loops.add(new ArrayList<>());
//        loops.get(0).add(1);
//        loops.get(0).add(2);
//        loops.get(0).add(3);
//        loops.get(0).add(1);
//        loops.get(1).add(4);
//        loops.get(1).add(5);
//        loops.get(1).add(6);
//        loops.get(1).add(4);
//
//        nonTouchingLoops = new NonTouchingLoops(loops);
//        return this.nonTouchingLoops.findNonTouchingLoops();
//    }


}
