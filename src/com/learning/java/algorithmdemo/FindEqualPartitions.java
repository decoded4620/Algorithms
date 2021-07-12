package com.learning.java.algorithmdemo;

import com.learning.java.utils.SpaceTimeComplexity;


/**
 * This is a demonstration of the classic partitioning problem. It demonstrates the recursive and the dynamic
 * solutions.
 */
public class FindEqualPartitions implements AlgorithmDemo {

  private final SpaceTimeComplexity _spaceTimeComplexity = new SpaceTimeComplexity();
  // find the sum of some elements in an array
  int sumOfElements(int[] array, int n) {
    int sum = 0;
    for (int i = 0; i < n; i++) {
      _spaceTimeComplexity.iterate();
      sum += array[i];
    }
    return sum;
  }

  /**
   * Recursive mechanism to check for two partitions with equal sums.
   *
   * @param array the input
   * @param n the current index
   * @param remainingSum the remaining sum to match given the sums so far.
   * @return true if subset sum is matched.
   */
  public boolean isSubsetSumMatch(int[] array, int n, int remainingSum) {
    _spaceTimeComplexity.push();
    // we've reached a base case where sum can match
    if (remainingSum == 0) {
      _spaceTimeComplexity.pop();
      return true;
    }

    // we have exhausted elements, remaining sum is not 0
    if (n == 0) {
      _spaceTimeComplexity.pop();
      return false;
    }

    int prevIdx = n - 1;

    // if the previous element would be greater than the remaining sum, ignore it
    if (array[prevIdx] > remainingSum) {
      boolean isMatch = isSubsetSumMatch(array, prevIdx, remainingSum);
      _spaceTimeComplexity.pop();
      return isMatch;
    }

    // check if previous element would make sum 0, otherwise, subtract it from remaining sum and continue
    boolean isMatch = isSubsetSumMatch(array, prevIdx, remainingSum) || isSubsetSumMatch(array, prevIdx,
        remainingSum - array[prevIdx]);
    _spaceTimeComplexity.pop();
    return isMatch;
  }

  /**
   * The recursive solution is worst case O(2^n) since it may need to check every subsequence, and splits into
   * two recursive branches.
   *
   * @param array the array
   * @return true if sum can be matched with recursion
   */
  public boolean equalSumsRecursive(int[] array) {
    _spaceTimeComplexity.addSize(array.length);
    int sum = sumOfElements(array, array.length);
    // check that sum is even
    if (sum % 2 != 0) {
      return false;
    }

    // find a subset of elements which add together to equal sumToMatch, can be non contiguous
    return isSubsetSumMatch(array, array.length, sum / 2);
  }

  /**
   * The dynamic solution for partition problem. This requires a table which takes up (sum/2 + 1) * (n+1) space to
   * track the sums in a partition table. The table can't be cut to two rows since it must use previous row values
   * to track the sum.
   *
   * @param array the input array
   * @param n the length of the array
   * @return true if the array can be partitioned.
   */
  public boolean equalSumDynamic(int[] array, int n) {
    int sum = sumOfElements(array, n);
    // check that sum is even
    if (sum % 2 != 0) {
      return false;
    }

    int i, j;

    int sumBoundary = sum / 2 + 1;
    boolean[][] partitionTable = new boolean[sumBoundary][n + 1];
    _spaceTimeComplexity.addSize(n);
    _spaceTimeComplexity.addSize((long) sumBoundary * (n + 1));

    // first row column set to true, if the sum adds to 0, its always true.
    for (i = 0; i <= n; i++) {
      _spaceTimeComplexity.iterate();
      partitionTable[0][i] = true;
    }

    // iterate for each row of the partition table
    // i acts as the current sum, up to sum/2
    for (i = 1; i < partitionTable.length; i++) {
      // iterate each column of the partition table, starting at 1
      for (j = 1; j <= n; j++) {
        _spaceTimeComplexity.iterate();
        int prevCol = j - 1;
        // bring the previous column value up
        partitionTable[i][j] = partitionTable[i][prevCol];

        // if current count is greater than the value at the previous column
        if (i >= array[prevCol]) {
          partitionTable[i][j] = partitionTable[i][j] || partitionTable[i - array[prevCol]][prevCol];
        }
      }
    }

    return partitionTable[sumBoundary - 1][n];
  }

  @Override
  public void run() {
    int[] array = new int[]{2, 500, 2, 500, 2000, 1000, 1000 };
    _spaceTimeComplexity.timeStart();
    boolean isSubsetSum = equalSumsRecursive(array);
    _spaceTimeComplexity.timeStop();
    System.out.println("Recursive - is subset sum: " + isSubsetSum);
    _spaceTimeComplexity.printStats();
    _spaceTimeComplexity.reset();

    _spaceTimeComplexity.timeStart();
    isSubsetSum = equalSumDynamic(array, array.length);
    System.out.println("Dynamic - is subset sum: " + isSubsetSum);
    _spaceTimeComplexity.timeStop();
    _spaceTimeComplexity.printStats();

  }
}
