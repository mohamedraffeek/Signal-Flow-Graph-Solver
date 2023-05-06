package com.cs2025.SignalFlowGraphBackend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Loops extends SignalFlowGraph{

    private List<List<Integer>> loops = new ArrayList<>();
    private List<Map<Double, List<Integer>>> loopsWithGain = new ArrayList<>();
    private List<String> result = new ArrayList<>();
    private Map<Integer, Map<Integer, Double>> graph;
    private int size;

    Loops(int size, Map<Integer, Map<Integer, Double>> graph) {
        this.size = size;
        this.graph = graph;
        foundLoops();
        foundLoopsWithGain();
    }

    public List<List<Integer>> getLoops() {
        return loops;
    }

    private void foundLoops(){
        boolean[] visited = new boolean[this.size];
        for(int i = 0; i < this.size; i++) {
            if(!visited[i]){
                LoopDfs(i, i, visited, new ArrayList<>(), this.loops);
            }
        }
        for(List<Integer> loop: this.loops){
            loop.add(loop.get(0));
        }
    }

    private void foundLoopsWithGain(){
        for(List<Integer> tempList: this.loops){
            double gain = calculateGain(tempList, this.graph);
            Map<Double, List<Integer>> tempMap = new HashMap<>();
            tempMap.put(gain, tempList);
            this.loopsWithGain.add(tempMap);
            result.add("Loop: " + tempList + ", Gain = " + gain);
        }
    }

    public List<Map<Double, List<Integer>>> getWithGain(){
        return this.loopsWithGain;
    }

    public List<String> getWithGainAsStr() {
        return result;
    }

    private void LoopDfs(int node, int start, boolean[] visited, List<Integer> path, List<List<Integer>> loops) {
        visited[node] = true;
        path.add(node);
        Map<Integer, Double> neighbours = this.graph.get(node);
        for (Map.Entry<Integer, Double> element : neighbours.entrySet()) {
            Integer neighbour = element.getKey();
            if (neighbour == start) {
                int minNode = path.get(0);
                for(int i = 1; i < path.size(); i++) {
                    minNode = Math.min(minNode, path.get(i));
                }
                if(path.get(0) == minNode) {
                    loops.add(new ArrayList<>(path));
                }
            }
            else if (!visited[neighbour]) {
                LoopDfs(neighbour, start, visited, path, loops);
            }
        }
        path.remove(path.size() - 1);
        visited[node] = false;
    }
}
