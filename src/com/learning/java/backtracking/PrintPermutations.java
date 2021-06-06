package com.learning.java.backtracking;

import com.learning.java.AlgorithmDemo;
import com.learning.java.utils.SpaceTimeComplexity;


/**
 * An algorithm demonstration for different ways of solving the permutations problem.
 *
 * Permutations of an array of characters
 */
public class PrintPermutations implements AlgorithmDemo {
    // records different aspects of the execution.
    private final SpaceTimeComplexity _spaceTimeComplexity = new SpaceTimeComplexity();

    /**
     * Swap characters at two indices of a character list, with a left fixed anchor
     * and some index i.
     *
     * @param chars the char list
     * @param l     the anchor index
     * @param i     another index, larger than l
     */
    public void doSwap(char[] chars, int l, int i) {
        if (l == i) {
            return;
        }

        var startChar = chars[l];
        chars[l] = chars[i];
        chars[i] = startChar;
    }

    /**
     * Finds all permutations of the character array.
     *
     * @param chars the array
     * @param l the left boundary (from the beginning)
     * @param r the right boundary (from the end)
     */
    public void findPermutations(char[] chars, int l, int r) {
        // record the total calls to this method
        _spaceTimeComplexity.call();
        // increase the stack size
        _spaceTimeComplexity.push();

        // this means we have reached an answer for a single permutation of the array.
        if (l >= r) {
            // record the new permutation
            _spaceTimeComplexity.addAnswer();
        } else {
            // keep searching
            for (var i = l; i <= r; i++) {
                // swap and print remaining permutations
                doSwap(chars, l, i);
                findPermutations(chars, l + 1, r);
                // back tracking (swap back after printing remaining permutations)
                doSwap(chars, l, i);
            }
        }

        // reduce stack size
        _spaceTimeComplexity.pop();
    }

    @Override
    public void run() {
        // the maximum amount of characters that can have permutations printed recursively in reasonable time.
        var chars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd'};

        for (int i = 1; i < chars.length; i++) {
            System.out.println("Find permutations for {" + new String(chars).substring(0, i) + "}");
            _spaceTimeComplexity.timeStart();
            findPermutations(chars, 0, i);
            _spaceTimeComplexity.timeStop();
            _spaceTimeComplexity.printStats();
            _spaceTimeComplexity.reset();
        }
    }

    /*
     * The Output below shows how the number of permutations, stack size, and total number of calls
     * to the permutation method, as well as the time taken to execute each, for 1 --> N elements. at 12
     * elements the time taken begins to be 'unreasonably' long (jumping from ~2 sec to ~1 minute).
     *
     * The complexity is exponential, and even at 127358058 permutations per second, it is not feasible
     * to find permutations for arrays larger than 16 elements
     *
     * Selected class from input args: com.learning.java.backtracking.PrintPermutations
     * Find permutations for {1}
     * Total Calls: 3, Max Stack 2, Total Answers 2, Total Time 24300 (0.000024 sec)
     * Find permutations for {12}
     * Total Calls: 10, Max Stack 3, Total Answers 6, Total Time 3700 (0.000004 sec)
     * Find permutations for {123}
     * Total Calls: 41, Max Stack 4, Total Answers 24, Total Time 9800 (0.000010 sec)
     * Find permutations for {1234}
     * Total Calls: 206, Max Stack 5, Total Answers 120, Total Time 54900 (0.000055 sec)
     * Find permutations for {12345}
     * Total Calls: 1237, Max Stack 6, Total Answers 720, Total Time 161500 (0.000162 sec)
     * Find permutations for {123456}
     * Total Calls: 8660, Max Stack 7, Total Answers 5040, Total Time 248700 (0.000249 sec)
     * Find permutations for {1234567}
     * Total Calls: 69281, Max Stack 8, Total Answers 40320, Total Time 1461700 (0.001462 sec)
     * Find permutations for {12345678}
     * Total Calls: 623530, Max Stack 9, Total Answers 362880, Total Time 5012500 (0.005013 sec)
     * Find permutations for {123456789}
     * Total Calls: 6235301, Max Stack 10, Total Answers 3628800, Total Time 25924100 (0.025924 sec)
     * Find permutations for {123456789a}
     * Total Calls: 68588312, Max Stack 11, Total Answers 39916800, Total Time 401957500 (0.401958 sec)
     * Find permutations for {123456789ab}
     * Total Calls: 823059745, Max Stack 12, Total Answers 479001600, Total Time 3407176100 (3.407176 sec)
     * Find permutations for {123456789abc}
     * Total Calls: 10699776686, Max Stack 13, Total Answers 6227020800, Total Time 56522005800 (56.522006 sec)
     */
}
