package com.learning.java.algorithmdemo;

import com.learning.java.utils.SpaceTimeComplexity;

import java.util.Arrays;

/**
 * This algorithm demonstrates the LIS problem in both brute force and iterative (dynamic) approaches.
 */
public class LongestIncreasingSubsequence implements AlgorithmDemo {
    private final SpaceTimeComplexity _spaceTimeComplexity = new SpaceTimeComplexity();

    static class MaxSequence {
        int sequence = 0;
    }

    /**
     * Recursive LIS form.
     * @param list the list to check
     * @param n the number of elements, or max idx in the list
     * @param maxRef the maximum length at the end of the execution, updated recursively.
     * @return the lis value.
     */
    private int lisRecursive(int[] list, int n, MaxSequence maxRef) {
        _spaceTimeComplexity.call();
        _spaceTimeComplexity.push();
        if(n == 1) {
            _spaceTimeComplexity.pop();
            return 1;
        }

        int maxSublist = 1;
        int maxEndingNow = 1;

        // walk the list from beginning, comparing each integer to our current end 'n - 1'
        for (int i = 1; i < n; i++) {
            // note there is an improvement here, only recurse if i > 1, because i == 1 is a base case.
            // this results in a 50% reduction in recursive calls, and thus a slight reduction in execution time
            maxSublist =  i == 1 ? 1 : lisRecursive(list, i, maxRef);

            _spaceTimeComplexity.iterate();

            // check if the previous element in the list is < than the last element in the list.
            if (list[n-1] > list[i-1]) {
                // increment the maximum subsequence current ending if the sublist max was higher than the current
                maxEndingNow = Math.max(maxSublist + 1, maxEndingNow);
            }
        }

        // update the overall max sequence size
        maxRef.sequence = (Math.max(maxRef.sequence, maxEndingNow));

        _spaceTimeComplexity.pop();
        return maxEndingNow;
    }

    private int lisDynamic(int[] list, int n) {
        _spaceTimeComplexity.push();

        if (n == 1) {
            return 1;
        }

        int[] increasingSubsequenceLengths = new int[n];
        int maxLenOfLCS = 0;

        Arrays.fill(increasingSubsequenceLengths, 1);

        // traverse the list
        for(int i = 1; i < n; i++) {

            // traverse the list from behind where we are to now
            for(int j = 0; j < i; j++) {
                _spaceTimeComplexity.iterate();

                // if the current list item is > than the previous list item
                // and the current max value, is less or equal than the previous item max value
                // reset the max value here and add 1 (we've found a higher number, so the length of lcs has increased)
                if(list[i] > list[j] && increasingSubsequenceLengths[i] <= increasingSubsequenceLengths[j]) {
                    increasingSubsequenceLengths[i] = increasingSubsequenceLengths[j] + 1;
                    maxLenOfLCS = Math.max(maxLenOfLCS, increasingSubsequenceLengths[i]);
                }
            }
        }

        _spaceTimeComplexity.pop();
        return maxLenOfLCS;
    }


    @Override
    public void run() {
        int[] arr = { 37, 10, 22, 9, 33, 21, 50, 41, 60, 2, 62, 34, 35, 67, 23, 71, 34, 80, 23, 83, 12, 85, 23, 24, 45, 90 };

        int n = arr.length;

        System.out.println("------------ LIS Recursive ----------------");
        MaxSequence maxRef = new MaxSequence();
        _spaceTimeComplexity.timeStart();
        lisRecursive(arr, n, maxRef);
        _spaceTimeComplexity.timeStop();
        System.out.printf("lis: %d", maxRef.sequence);
        _spaceTimeComplexity.addAnswer();
        _spaceTimeComplexity.printStats();
        _spaceTimeComplexity.reset();


        System.out.println("------------ LIS Dynamic (Tabulated) ----------------");

        _spaceTimeComplexity.timeStart();
        int lisI = lisDynamic(arr, n);
        _spaceTimeComplexity.timeStop();
        System.out.printf("lis: %d", lisI);
        _spaceTimeComplexity.addAnswer();
        _spaceTimeComplexity.printStats();
        _spaceTimeComplexity.reset();
    }
}
