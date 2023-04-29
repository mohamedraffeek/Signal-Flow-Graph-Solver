package com.cs2025.SignalFlowGraphBackend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForwardPaths extends SignalFlowGraph{

    private List<List<Integer>> forwardPaths;
    private List<Map<Double, List<Integer>>> pathsWithGain = new ArrayList<>();
    private Map<Integer, Map<Integer, Double>> graph;
    private int size;

    ForwardPaths(int size, Map<Integer, Map<Integer, Double>> graph) {
        this.size = size;
        this.graph = graph;
        findForwardPaths();
        findForwardPathsWithGain();
    }

    private void findForwardPaths(){
        this.forwardPaths = getAllPathsBetweenTwoNodes(0, this.size - 1);
    }


    private void findForwardPathsWithGain(){
        for(List<Integer> tempList: forwardPaths){
            double gain = calculateGain(tempList, this.graph);
            Map<Double, List<Integer>> tempMap = new HashMap<>();
            tempMap.put(gain, tempList);
            pathsWithGain.add(tempMap);
        }
    }

    public List<List<Integer>> get(){
        return this.forwardPaths;
    }

    public List<Map<Double, List<Integer>>> getWithGain() {
        return pathsWithGain;
    }



    private void dfsForForwardPath(int node, int destination, boolean []visited, List<Integer> path, List<List<Integer>> paths) {
        visited[node] = true;
        path.add(node);
        if (node == destination) {
            paths.add(new ArrayList<>(path));
        }
        else {
            Map<Integer, Double> neighbours = this.graph.get(node);
            for(Map.Entry<Integer, Double> element : neighbours.entrySet()){
                Integer neighbour = element.getKey();
                if (!visited[neighbour]) {
                    dfsForForwardPath(neighbour, destination, visited, path, paths);
                }
            }
        }
        visited[node] = false;
        path.remove(path.size() - 1);
    }

    private List<List<Integer>> getAllPathsBetweenTwoNodes(int source, int destination){
        List<List<Integer>> paths = new ArrayList<>();
        boolean []visited = new boolean[this.size];
        dfsForForwardPath(source, destination, visited, new ArrayList<>(), paths);
        return paths;
    }
}
