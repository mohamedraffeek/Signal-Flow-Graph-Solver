package com.cs2025.SignalFlowGraphBackend;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public List<String> getForwardPathsWithGain(){
        forwardPaths = new ForwardPaths(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph(), source, destination);
        return forwardPaths.getPathsWithGainAsStr();
    }

    @GetMapping("/getLoopsWithGain")
    public List<String> getLoopsWithGain(){
        loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
        return this.loops.getWithGainAsStr();
    }

    @GetMapping("/getNonTouchingLoops")
    public List<String> getNonTouchingLoopsObj(){
        if(loops == null)
            loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
        nonTouchingLoopsObj = new NonTouchingLoops(loops.getLoops());
        this.nonTouchingLoopsObj.getNonTouchingLoops();
        if(nonTouchingLoopsObj.getNonTouchingLoopsAsStr().get(0).equalsIgnoreCase( "Loops: [], of Size: 1"))
            return new ArrayList<>();
        return this.nonTouchingLoopsObj.getNonTouchingLoopsAsStr();
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
    public List<String> getDeltaForEachForwardPath(){
        if( nonTouchingLoopsObj == null ) {
            if(loops == null)
                loops = new Loops(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph());
            nonTouchingLoopsObj = new NonTouchingLoops(loops.getLoops());
        }
        if(forwardPaths == null)
            forwardPaths = new ForwardPaths(signalFlowGraphObj.getSize(), signalFlowGraphObj.getGraph(), source, destination);
        calcDeltaObj.calcDeltaForEachForwardPath();
        return calcDeltaObj.calcDeltaForEachForwardPathAsStr();
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
