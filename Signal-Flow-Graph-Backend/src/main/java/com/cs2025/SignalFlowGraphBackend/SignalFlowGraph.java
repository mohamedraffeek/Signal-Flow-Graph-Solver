package com.cs2025.SignalFlowGraphBackend;

import java.util.*;

public class SignalFlowGraph {
    private Map<Integer, Map<Integer, Double>> graph;
    private int size;

    public Map<Integer, Map<Integer, Double>> getGraph() {
        return graph;
    }

    public int getSize() {
        return size;
    }

    public SignalFlowGraph() {
    }

    private void graphConstruct(double[][][] graph){
        this.graph = new HashMap<>(this.size);
        for(int i=0; i<this.size; i++){
            this.graph.put(i, new HashMap<>());
            for(int j=0; j<graph[i].length; j++){
                Map<Integer, Double> innerMap = this.graph.get(i);
                double tempWeight = innerMap.containsKey((int)graph[i][j][0]) ?
                    innerMap.get((int)graph[i][j][0]) + graph[i][j][1]: graph[i][j][1];
                innerMap.put((int) graph[i][j][0], tempWeight);
            }
        }
    }

    SignalFlowGraph(double[][][] graph) {
        this.size = graph.length;
        graphConstruct(graph);
    }

    protected double calculateGain(List<Integer> path, Map<Integer, Map<Integer, Double>> graph){
        int pathLen = path.size();
        double gain = 1;
        for(int i=0; i<pathLen-1; i++){
            int firstNode = path.get(i);
            int secondNode = path.get(i+1);
            gain *= graph.get(firstNode).get(secondNode);
        }
        return gain;
    }

}
