package com.cs2025.SignalFlowGraphBackend;

import java.util.*;

public class NonTouchingLoops {

    public List<List<Integer>> loops;
    public Map<Integer,List<List<List<Integer>>>> nonTouchingLoops;
    private List<String> result = new ArrayList<>();

    public NonTouchingLoops(List<List<Integer>> loops) {
        this.loops = loops;
    }

    /**
     * @return Map<Integer,List<List<List<Integer>>>> which contains the non touching loops(2,3,4,5,...)
     */
    public Map<Integer,List<List<List<Integer>>>> getNonTouchingLoops(){

        Map<Integer,List<List<List<Integer>>>> nonTouchingLoopsMap = new HashMap<>();

        // Loop through all possible combinations of non-touching loops
        for (int i = 2; i <= loops.size(); i++) { // size of non-touching loops
            List<List<List<Integer>>> nonTouchingLoopsOfSizeI = new ArrayList<>();
            // Generate all combinations of loops of size i
            List<List<Integer>> currentLoopCombination = generateCombinations(i);
            // Check if each combination is non-touching
            for (List<Integer> combination : currentLoopCombination) {
                if (areLoopsNonTouching(combination)) {
                    nonTouchingLoopsOfSizeI.add(getLoops(combination));
                }
            }
            // Add non-touching loops of size i to the result
            if (!nonTouchingLoopsOfSizeI.isEmpty()) {
                nonTouchingLoopsMap.put(i, nonTouchingLoopsOfSizeI);
                result.add("Loops: " + nonTouchingLoopsOfSizeI + ", of Size: " + i);
            }
        }
        addIndividualLoops(nonTouchingLoopsMap);
        this.nonTouchingLoops = nonTouchingLoopsMap;
        return nonTouchingLoopsMap;
    }

    private void addIndividualLoops(Map<Integer,List<List<List<Integer>>>> nonTouchingLoopsMap){
        List<List<List<Integer>>> nonTouchingLoopsOfSize1 = new ArrayList<>();
        for(List<Integer> loop: loops){
            nonTouchingLoopsOfSize1.add(Arrays.asList(loop));
        }
        nonTouchingLoopsMap.put(1, nonTouchingLoopsOfSize1);
        result.add("Loops: " + nonTouchingLoopsOfSize1 + ", of Size: 1");
    }

    public List<String> getNonTouchingLoopsAsStr() {
        return result;
    }

    /**
     * Check if a combination of loops is non-touching
     */
    private boolean areLoopsNonTouching(List<Integer> combination) {
        for (int i = 0; i < combination.size() - 1; i++) {
            List<Integer> loop1 = getLoop(combination.get(i));
            for (int j = i + 1; j < combination.size(); j++) {
                List<Integer> loop2 = getLoop(combination.get(j));
                if (areLoopsTouching(loop1, loop2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if two loops are touching (i.e., have common nodes)
     */
    private boolean areLoopsTouching(List<Integer> loop1, List<Integer> loop2) {
        for (int node : loop1) {
            if (loop2.contains(node)) {
                return true;
            }
        }
        return false;
    }

    private List<List<Integer>> getLoops(List<Integer> loopIndices) {
        List<List<Integer>> loops = new ArrayList<>();
        for (int i : loopIndices) {
            loops.add(getLoop(i));
        }
        return loops;
    }

    public List<Integer> getLoop(int index) {
        if (index < 0 || index >= loops.size()) {
            throw new IndexOutOfBoundsException("Invalid loop index");
        }
        return loops.get(index);
    }

    private List<List<Integer>> generateCombinations( int size) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        int[] indices = new int[size];
        for (int i = 0; i < loops.size(); i++) {
            list.add(i);
        }
        for (int i = 0; i < size; i++) {
            indices[i] = i;
        }
        while (indices[0] <= list.size() - size) {
            List<Integer> combination = new ArrayList<>();
            for (int index : indices) {
                combination.add(list.get(index));
            }
            result.add(combination);
            int i = size - 1;
            while (i >= 0 && indices[i] == i + list.size() - size) {
                i--;
            }
            if (i < 0) {
                break;
            }
            indices[i]++;
            for (int j = i + 1; j < size; j++) {
                indices[j] = indices[j - 1] + 1;
            }
        }
        return result;
    }

}


