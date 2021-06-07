package com.learning.java.algorithmdemo;

import com.learning.java.algorithmdemo.AlgorithmDemo;
import com.learning.java.utils.SpaceTimeComplexity;

/**
 * This class demonstrates the LCS algorithm using 3 different solutions, brute force, memoized, and tabulated (dynamic)
 */
public class LongestCommonSubsequence implements AlgorithmDemo {

    private final SpaceTimeComplexity _spaceTimeComplexity = new SpaceTimeComplexity();

    /**
     * Calculates the longest common subsequence (non-contiguous) between two character sequences
     *
     * @param seq1 the first sequence
     * @param seq2 the second sequence
     * @param idx1 the idx within the first sequence, starting from the end.
     * @param idx2 the idx within the second sequence, starting from the end.
     * @return the length of the longest common subsequence
     */
    private int lcsRecursive(char[] seq1, char[] seq2, int idx1, int idx2) {
        _spaceTimeComplexity.call();
        _spaceTimeComplexity.push();
        if (idx1 == 0 || idx2 == 0) {
            _spaceTimeComplexity.pop();
            return 0;
        }

        int lcs;
        // if the character in the sequence at idx1 / idx2 is the same then it is part of a common subsequence.
        if (seq1[idx1 - 1] == seq2[idx2 - 1]) {
            lcs = 1 + lcsRecursive(seq1, seq2, idx1 - 1, idx2 - 1);
        } else {
            lcs = Math.max(lcsRecursive(seq1, seq2, idx1 - 1, idx2), lcsRecursive(seq1, seq2, idx1, idx2 - 1));
        }
        _spaceTimeComplexity.pop();
        return lcs;
    }


    /**
     * Calculates the LCS using memoization technique to reduce the number of calls.
     * @param seq1 the first sequence
     * @param seq2 the second sequence
     * @param idx1 the index on the first sequence
     * @param idx2 the index on the second sequence
     * @param cache the memoization cache
     * @return the longest common subsequence.
     */
    private int lcsMemoized(char[] seq1, char[] seq2, int idx1, int idx2, int[][] cache) {
        _spaceTimeComplexity.call();
        _spaceTimeComplexity.push();
        if (idx1 == 0 || idx2 == 0) {
            _spaceTimeComplexity.pop();
            return 0;
        }

        if (cache[idx1][idx2] != 0) {
            _spaceTimeComplexity.pop();
            return cache[idx1][idx2];
        }

        int lcs;

        // if the character in the sequence at idx1 / idx2 is the same then it is part of some common subsequence.
        if (seq1[idx1 - 1] == seq2[idx2 - 1]) {
            lcs = 1 + lcsMemoized(seq1, seq2, idx1 - 1, idx2 - 1, cache);
            cache[idx1][idx2] = lcs;
        } else {
            // the lcs is defined by the max lcs of either
            // 1. previous index of seq1, and the current index of seq2
            // 2. the current index of seq1, and the previous index of seq2
            lcs = Math.max(lcsMemoized(seq1, seq2, idx1 - 1, idx2, cache), lcsMemoized(seq1, seq2, idx1, idx2 - 1, cache));
        }
        _spaceTimeComplexity.pop();
        return lcs;
    }

    /**
     * Calculates the LCS using dynamic programming technique
     * @param seq1 the first sequence
     * @param seq2 the second sequence
     * @param idx1 the index on the first sequence
     * @param idx2 the index on the second sequence
     * @return the longest common subsequence.
     */
    private int lcsDynamic(char[] seq1, char[] seq2, int idx1, int idx2) {
        _spaceTimeComplexity.push();

        // store the lcs for a given indices. Note this handles initializing the base case as well (e.g. i or j == 0) since
        // the matrix is filled with 0's already.
        final int[][] lcsMatrix = new int[idx1 + 1][idx2 + 1];

        // skip the base case (i = 0 || j = 0) and find the lcs for all other cases.
        for (int i = 1; i <= idx1; i++) {
            for (int j = 1; j <= idx2; j++) {
                _spaceTimeComplexity.call();
                if (seq1[i - 1] == seq2[j - 1]) {
                    lcsMatrix[i][j] = lcsMatrix[i - 1][j - 1] + 1;
                } else {
                    lcsMatrix[i][j] = Math.max(lcsMatrix[i - 1][j], lcsMatrix[i][j - 1]);
                }
            }
        }

        _spaceTimeComplexity.pop();
        return lcsMatrix[idx1][idx2];
    }

    @Override
    public void run() {
        char[] seq1 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'g', 'b', 'b', 'e', 'f', 'h', 'z','b', 'e', 'y', 'f', 'h'};
        char[] seq2 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'b', 'g', 'b', 'e', 'y', 'f', 'h','b', 'e', 'f', 'h', 'z'};

//        char[] seq1 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p','q', 'r', 's', 't', 'a'};
//        char[] seq2 = new char[]{'a', 't', 's', 'r', 'q', 'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g', 'f','e', 'd', 'c', 'b', 'a'};

        System.out.println("----------------- Recursive LCS run --------------------");
        _spaceTimeComplexity.timeStart();
        int lcsR = lcsRecursive(seq1, seq2, seq1.length, seq2.length);
        _spaceTimeComplexity.timeStop();
        System.out.printf("Lcs: %d, ", lcsR);
        _spaceTimeComplexity.addAnswer();
        _spaceTimeComplexity.printStats();
        _spaceTimeComplexity.reset();

        System.out.println("----------------- Memoized LCS run --------------------");
        _spaceTimeComplexity.timeStart();
        int lcsM = lcsMemoized(seq1, seq2, seq1.length, seq2.length, new int[seq1.length + 1][seq2.length + 1]);
        _spaceTimeComplexity.timeStop();
        System.out.printf("Lcs: %d, ", lcsM);
        _spaceTimeComplexity.addAnswer();
        _spaceTimeComplexity.printStats();
        _spaceTimeComplexity.reset();


        System.out.println("----------------- Dynamic LCS run --------------------");
        _spaceTimeComplexity.timeStart();
        int lcsD = lcsDynamic(seq1, seq2, seq1.length, seq2.length);
        _spaceTimeComplexity.timeStop();
        System.out.printf("Lcs: %d, ", lcsD);
        _spaceTimeComplexity.addAnswer();
        _spaceTimeComplexity.printStats();
        _spaceTimeComplexity.reset();
    }

    /*
     * The Outputs
     *
     * For Recursive function the behavior will be wildly different for inputs with sparse matching characters.
     *
     * For instance using a sequence like:
     *
     * char[] seq1 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p','q', 'r', 's', 't', 'a'};
     * char[] seq2 = new char[]{'a', 't', 's', 'r', 'q', 'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g', 'f','e', 'd', 'c', 'b', 'a'};
     *
     * ----------------- Recursive LCS run --------------------
     * Lcs: 3, Total Calls Or Iterations: 61615654444, Max Stack 40, Total Answers 1, Total Time 216005636700 (216.005637 sec)
     * ----------------- Memoized LCS run --------------------
     * Lcs: 3, Total Calls Or Iterations: 2097130, Max Stack 40, Total Answers 1, Total Time 13157100 (0.013157 sec)
     * ----------------- Dynamic LCS run --------------------
     * Lcs: 3, Total Calls Or Iterations: 441, Max Stack 1, Total Answers 1, Total Time 36500 (0.000037 sec)
     *
     * Vs:
     *
     * char[] seq1 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'g', 'b', 'b', 'e', 'f', 'h', 'z','b', 'e', 'y', 'f', 'h'};
     * char[] seq2 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'a', 'b', 'g', 'b', 'e', 'y', 'f', 'h','b', 'e', 'f', 'h', 'z'};
     * ----------------- Recursive LCS run --------------------
     * Lcs: 18, Total Calls Or Iterations: 5960207094, Max Stack 42, Total Answers 1, Total Time 20135395400 (20.135395 sec)
     * ----------------- Memoized LCS run --------------------
     * Lcs: 18, Total Calls Or Iterations: 2374044292, Max Stack 41, Total Answers 1, Total Time 9426058000 (9.426058 sec)
     * ----------------- Dynamic LCS run --------------------
     * Lcs: 18, Total Calls Or Iterations: 441, Max Stack 1, Total Answers 1, Total Time 35900 (0.000036 sec)
     *
     * We can also see that for sparsely similar sequences, the memoized version is also magnitudes faster than the brute force.
     * However, the dynamic / tabulated version stays consistent and predictable.
     */
}
