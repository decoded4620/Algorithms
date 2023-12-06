package com.learning.java.algorithmdemo;

import com.learning.java.utils.SpaceTimeComplexity;

/**
 * Given a stream (array) of integer, find the longest sequence of integer x, which is followed by integer y
 * if x = 1, and y = 2; then the array { 1,1,1,2,1,1,1,1,2,1,1,1,1,1,2 } should produce { startIdx: 9, endIdx:}
 */
public class LongestContiguousSequenceFollowedBy implements AlgorithmDemo {
    private final SpaceTimeComplexity _spaceTimeComplexity = new SpaceTimeComplexity();

    static class MaxContiguousSequenceOf {
        MaxContiguousSequenceOf(int of, int followedBy) {
            this.followedBy = followedBy;
            this.of = of;
        }

        int of;
        int followedBy;

        int startIdx = -1;
        int endIdx = -1;

        int longestSequence = 0;
    }

    private void lcs(int[] arr, int of, int followedBy, MaxContiguousSequenceOf maxRef) {
        int lastVal = -1;
        int currVal = -1;
        _spaceTimeComplexity.push();

        boolean inSequence = false;
        for (int i = 0; i < arr.length; ++i) {
            _spaceTimeComplexity.iterate();
            // previous and current stream values
            lastVal = currVal;
            currVal = arr[i];

            if (lastVal == maxRef.of && currVal == lastVal) {
                if (!inSequence) {
                    inSequence = true;
                    maxRef.startIdx = i - 1;
                }
                
                maxRef.endIdx = i;
            } else {
                if (inSequence) {
                    inSequence = false;
                }

                if (currVal == maxRef.followedBy) {
                    int seqLen = maxRef.endIdx - maxRef.startIdx + 1;

                    if (maxRef.longestSequence < seqLen) {
                        maxRef.longestSequence = seqLen;
                        System.out.println("Start idx: " + maxRef.startIdx + ", End idx: " + maxRef.endIdx + ", longest seq:" + maxRef.longestSequence);
                    } 
                }
            }

        }

        _spaceTimeComplexity.pop();
    }

    @Override
    public void run() {
        int[] arr = { 1, 1, 2, 0, 0, 0, 1, 1, 1, 2, 0, 1, 1, 1, 1, 1, 2, 0, 0 };
        // int[] arr = { 1, 1, 1, 1, 2 };

        System.out.println("------------ LCSF ----------------");
        MaxContiguousSequenceOf maxRef = new MaxContiguousSequenceOf(1, 2);
        _spaceTimeComplexity.timeStart();
        lcs(arr, 1, 2, maxRef);
        _spaceTimeComplexity.timeStop();
        System.out.printf("LCSF: %d ", maxRef.longestSequence);
        _spaceTimeComplexity.addAnswer();
        _spaceTimeComplexity.printStats();
        _spaceTimeComplexity.reset();
    }
}
