package com.cs2025.SignalFlowGraphBackend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonTouchingLoops extends Loops{

    private List<List<Integer>> loops;
    NonTouchingLoops(List<List<Integer>> loops) {
        this.loops = loops;
    }

    public List<List<Integer>> findNonTouchingLoops() {
        List<List<Integer>> nonTouchingLoops = new ArrayList<>();
        for (int i = 2; i <= loops.size(); i++) {
            for (List<Integer> combination : combinations(loops, i)) {
                if (areNonTouching(combination, loops)) {
                    nonTouchingLoops.add(combination);
                }
            }
        }
        return nonTouchingLoops;
    }

    private boolean areNonTouching(List<Integer> loop1, List<Integer> loop2, List<List<Integer>> loops) {
        Set<Integer> set1 = new HashSet<>(loop1);
        Set<Integer> set2 = new HashSet<>(loop2);
        for (int i = 0; i < loops.size(); i++) {
            if (i != loop1.get(0) && i != loop2.get(0)) {
                Set<Integer> set3 = new HashSet<>(loops.get(i));
                if (set1.containsAll(set3) || set2.containsAll(set3)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean areNonTouching(List<Integer> combination, List<List<Integer>> loops) {
        for (int i = 0; i < combination.size() - 1; i++) {
            for (int j = i + 1; j < combination.size(); j++) {
                int loopIndex1 = combination.get(i);
                int loopIndex2 = combination.get(j);
                if (!areNonTouching(loops.get(loopIndex1), loops.get(loopIndex2), loops)) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<List<Integer>> combinations(List<List<Integer>> loops, int length) {
        List<List<Integer>> combinations = new ArrayList<>();
        combinations(loops, length, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private void combinations(List<List<Integer>> loops, int length, int start, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == length) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < loops.size(); i++) {
            current.add(i);
            combinations(loops, length, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
