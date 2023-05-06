package com.cs2025.SignalFlowGraphBackend;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/connectToApi")
public class Controller {
    public SignalFlowGraph signalFlowGraphObj;
    public ForwardPaths forwardPaths;
    public Loops loops;
    public NonTouchingLoops nonTouchingLoopsObj;
    public CalcDelta calcDeltaObj;

    private int source, destination;

    @PostMapping("/sendAdjList")
    public void receivedAdjList(@RequestBody double[][][] adjacencyList) {
        this.signalFlowGraphObj = new SignalFlowGraph(adjacencyList);
    }

    @PostMapping("/sendSource")
    public void receivedSource(@RequestBody int source) {
        this.source = source;
        System.out.println("source: " + source);
    }

    @PostMapping("/sendDestination")
    public void receivedDestination(@RequestBody int destination) {
        this.destination = destination;
        System.out.println("destination: " + destination);
    }

    @GetMapping("/getPathsWithGain")
    public List<Map<List<Integer>,Double>> getForwardPathsWithGain(){
        forwardPaths = new ForwardPaths(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph(), source, destination);
        return forwardPaths.getPathsWithGain();
    }

    @GetMapping("/getLoopsWithGain")
    public List<Map<Double, List<Integer>>> getLoopsWithGain(){
        loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
        return this.loops.getWithGain();
    }

    @GetMapping("/getNonTouchingLoops")
    public Map<Integer,List<List<List<Integer>>>> getNonTouchingLoopsObj(){
        if(loops == null)
            loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
        nonTouchingLoopsObj = new NonTouchingLoops(loops.getLoops());
        return this.nonTouchingLoopsObj.getNonTouchingLoops();
    }

    @GetMapping("/getMainDelta")
    public double getMainDelta(){
        if( nonTouchingLoopsObj == null) {
            if(loops == null)
                loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
            nonTouchingLoopsObj = new NonTouchingLoops(loops.getLoops());
        }
        calcDeltaObj = new CalcDelta(nonTouchingLoopsObj, forwardPaths);
        return  calcDeltaObj.calcMainDelta();
    }

    @GetMapping("/getDeltaForEachForwardPath")
    public Map<Double, List<Integer>> getDeltaForEachForwardPath(){
        if( nonTouchingLoopsObj == null ) {
            if(loops == null)
                loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
            nonTouchingLoopsObj = new NonTouchingLoops(loops.getLoops());
        }
        if(forwardPaths == null)
            forwardPaths = new ForwardPaths(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph(), source, destination);
        return  calcDeltaObj.calcDeltaForEachForwardPath();
    }

    @GetMapping("/getTransferFunction")
    public double getTransferFunction(){
        if( nonTouchingLoopsObj == null ) {
            if(loops == null)
                loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
            nonTouchingLoopsObj = new NonTouchingLoops(loops.getLoops());
        }
        if(forwardPaths == null)
            forwardPaths = new ForwardPaths(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph(), source, destination);
        return  calcDeltaObj.calcTransferFunction();
    }
}
