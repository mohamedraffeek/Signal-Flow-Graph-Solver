package com.cs2025.SignalFlowGraphBackend;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class NonTouchingLoopsTest {

    @Test
    public void testGetNonTouchingLoops() {
        // create some non-touching loops
        List<List<Integer>> loops = new ArrayList<>();
        loops.add(Arrays.asList(1, 2, 3));
        loops.add(Arrays.asList(4, 5, 6));
        loops.add(Arrays.asList(7, 8, 9));

        // create the NonTouchingLoops object
        NonTouchingLoops ntl = new NonTouchingLoops(loops);

        // get the non-touching loops
        Map<Integer,List<List<List<Integer>>>> nonTouchingLoops = ntl.getNonTouchingLoops();

        // check the results
        List<List<List<Integer>>> nonTouchingLoopsOfSize2 = nonTouchingLoops.get(2);
        assertEquals(3, nonTouchingLoopsOfSize2.size());
        assertTrue(nonTouchingLoopsOfSize2.contains(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6))));
        assertTrue(nonTouchingLoopsOfSize2.contains(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(7, 8, 9))));
        assertTrue(nonTouchingLoopsOfSize2.contains(Arrays.asList(Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9))));

        List<List<List<Integer>>> nonTouchingLoopsOfSize3 = nonTouchingLoops.get(3);
        assertEquals(1, nonTouchingLoopsOfSize3.size());
        assertTrue(nonTouchingLoopsOfSize3.contains(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9))));
    }
}