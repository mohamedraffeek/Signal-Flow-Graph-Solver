package com.cs2025.SignalFlowGraphBackend;

import java.util.*;

public class SignalFlowGraph {
    private Map<Integer, Map<Integer, Double>> graph;
    private int size;
    private List<List<Integer>> forwardPaths;

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
    SignalFlowGraph(){

    }


    private void dfs(int node, int destination, boolean []visited, List<Integer> path, List<List<Integer>> paths) {
        visited[node] = true;
        path.add(node);

        if (node == destination) {
            paths.add(new ArrayList<>(path));
        }
        else {
            Map<Integer, Double> neighbours = graph.get(node);
            for(Map.Entry<Integer, Double> element : neighbours.entrySet()){
                Integer neighbour = element.getKey();
                if (!visited[neighbour]) {
                    dfs(neighbour, destination, visited, path, paths);
                }
            }
        }
        visited[node] = false;
        path.remove(path.size() - 1);
    }

    private List<List<Integer>> getAllPathsBetweenTwoNodes(int source, int destination){
            List<List<Integer>> paths = new ArrayList<>();
            boolean []visited = new boolean[this.size];
            dfs(source, destination, visited, new ArrayList<>(), paths);
            return paths;
    }

    private double calculateGain(List<Integer> path){
        int pathLen = path.size();
        double gain = 1;
        for(int i=0; i<pathLen-1; i++){
            int firstNode = path.get(i);
            int secondNode = path.get(i+1);
            gain *= this.graph.get(firstNode).get(secondNode);
        }

        return gain;
    }

    public List<Map<Double, List<Integer>>> getForwardPath() {
        this.forwardPaths = getAllPathsBetweenTwoNodes(0, this.size-1);
        List<Map<Double, List<Integer>>> pathsWithGain = new ArrayList<>();
        for(List<Integer> tempList: forwardPaths){
            double gain = calculateGain(tempList);
            Map<Double, List<Integer>> tempMap = new HashMap<>();
            tempMap.put(gain, tempList);
            pathsWithGain.add(tempMap);
        }


        return pathsWithGain;
    }
    public double[][] getLoops() {
        return null;
    }
    public double[][] getNonTouchingLoops() {
        return null;
    }

}
