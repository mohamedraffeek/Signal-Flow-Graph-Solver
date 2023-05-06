package com.cs2025.SignalFlowGraphBackend;

import java.util.*;

public class CalcDelta extends SignalFlowGraph {

    private Map<Integer, List<List<List<Integer>>>> NonTouchingLoops;
    private ForwardPaths forwardPaths;
    private Map<Double, List<Integer>> deltaForEachForwardPath = new HashMap<>();
    private double mainDelta;

    public CalcDelta(NonTouchingLoops nonTouchingLoopsObj, ForwardPaths forwardPaths) {
        this.NonTouchingLoops = nonTouchingLoopsObj.nonTouchingLoops;
        this.forwardPaths = forwardPaths;
    }

    private double calcDelta(Map<Integer, List<List<List<Integer>>>> NonTouchingLoops) {
        double delta = 1;
        for (int i = 1; i <= NonTouchingLoops.size(); i++) {
            double temp = 0;
            for (List<List<Integer>> list : NonTouchingLoops.get(i)) {
                double gain = 1;
                for (List<Integer> loop : list) {
                    gain *= calculateGain(loop, forwardPaths.graph);
                }
                temp += gain;
            }
            delta += Math.pow(-1, i) * temp;
        }
        return delta;
    }

    public double calcMainDelta(){
        mainDelta = calcDelta(NonTouchingLoops);;
        return mainDelta;
    }

    public Map<Double, List<Integer>> calcDeltaForEachForwardPath() {
        Map<Double, List<Integer>> tempDeltaForForwardPaths = new HashMap<>();
        for (List<Integer> path : forwardPaths.getForwardPaths()) {
            Map<Integer, List<List<List<Integer>>>> nonTouchingLoopsForPathI = constructNonTouchingLoopsForPath(path);
            double deltaI = calcDelta(nonTouchingLoopsForPathI);
            tempDeltaForForwardPaths.put(deltaI, path);
        }
        deltaForEachForwardPath = tempDeltaForForwardPaths;
        return tempDeltaForForwardPaths;
    }

    public Map<Integer, List<List<List<Integer>>>> constructNonTouchingLoopsForPath(List<Integer> path) {
        Map<Integer, List<List<List<Integer>>>> nonTouchingLoops = new HashMap<>();
        for (int i = 1; i <= NonTouchingLoops.size(); i++) {
            List<List<List<Integer>>> temp = new ArrayList<>();
            for (List<List<Integer>> list : NonTouchingLoops.get(i)) {
                List<List<Integer>> temp2 = new ArrayList<>();
                for (List<Integer> loop : list) {
                    if (Collections.disjoint(loop, path)) {
                        temp2.add(loop);
                    }
                    else{
                        temp2.clear();
                        break;
                    }
                }
                if (!temp2.isEmpty()) {
                    temp.add(temp2);
                }
            }
            nonTouchingLoops.put(i, temp);
        }
        return nonTouchingLoops;
    }

    public double calcTransferFunction() {
        double numerator = 0, denominator = mainDelta;
        for (Map.Entry<Double, List<Integer>> entry : deltaForEachForwardPath.entrySet()) {
            numerator += entry.getKey() *  calculateGain(entry.getValue(), forwardPaths.graph);
        }
        double transferFunction = numerator / denominator;
        return transferFunction;
    }
}
