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
     * Find permutations for {1}
     * Permutations: 2 Max Stack: 2, Total Calls: 3 Total time: 0.000022 sec
     * Find permutations for {12}
     * Permutations: 8 Max Stack: 3, Total Calls: 13 Total time: 0.000003 sec
     * Find permutations for {123}
     * Permutations: 32 Max Stack: 4, Total Calls: 54 Total time: 0.000008 sec
     * Find permutations for {1234}
     * Permutations: 152 Max Stack: 5, Total Calls: 260 Total time: 0.000044 sec
     * Find permutations for {12345}
     * Permutations: 872 Max Stack: 6, Total Calls: 1497 Total time: 0.000051 sec
     * Find permutations for {123456}
     * Permutations: 5912 Max Stack: 7, Total Calls: 10157 Total time: 0.000251 sec
     * Find permutations for {1234567}
     * Permutations: 46232 Max Stack: 8, Total Calls: 79438 Total time: 0.001915 sec
     * Find permutations for {12345678}
     * Permutations: 409112 Max Stack: 9, Total Calls: 702968 Total time: 0.003858 sec
     * Find permutations for {123456789}
     * Permutations: 4037912 Max Stack: 10, Total Calls: 6938269 Total time: 0.025696 sec
     * Find permutations for {123456789a}
     * Permutations: 43954712 Max Stack: 11, Total Calls: 75526581 Total time: 0.339806 sec
     * Find permutations for {123456789ab}
     * Permutations: 522956312 Max Stack: 12, Total Calls: 898586326 Total time: 2.786352 sec
     * Find permutations for {123456789abc}
     * Permutations: 6749977112 Max Stack: 13, Total Calls: 11598363012 Total time: 52.918371 sec
     */
}
