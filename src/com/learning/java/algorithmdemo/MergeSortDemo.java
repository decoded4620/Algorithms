package com.learning.java.algorithmdemo;

import com.learning.java.utils.SpaceTimeComplexity;


/**
 * Demonstration of the sequential merge sort algorithm
 */
public class MergeSortDemo implements AlgorithmDemo {
  private final SpaceTimeComplexity _spaceTimeComplexity = new SpaceTimeComplexity();

  /**
   * Merges values from left and right lists into the input list
   * @param inputArray the list being sorted
   * @param l the left index
   * @param m the current mid point
   * @param r the right index
   */
  public void merge(int[] inputArray, int l, int m, int r) {
    _spaceTimeComplexity.push();

    int i, j, k;

    int leftLen = m - l + 1;
    int rightLen = r - m;

    int[] left = new int[leftLen];
    int[] right = new int[rightLen];

    // copy into left
    for (i = 0; i < leftLen; i++) {
      _spaceTimeComplexity.iterate();
      left[i] = inputArray[l + i];
    }

    // copy into right
    for (i = 0; i < rightLen; i++) {
      _spaceTimeComplexity.iterate();
      right[i] = inputArray[m + 1 + i];
    }

    i = 0;
    j = 0;

    // k starts at left index
    k = l;

//    System.out.print("Conquer: [" + l + " <-- " + m + " --> " + r + "]: ");
//    System.out.println(Arrays.toString(left) + " -- " + Arrays.toString(right));
    while (i < leftLen && j < rightLen) {
      _spaceTimeComplexity.iterate();
      int leftVal = left[i];
      int rightVal = right[j];

      if (leftVal < rightVal) {
        // only increase i when left is chosen
        i++;
        inputArray[k++] = leftVal;
      } else {
        // only increase j when right is chosen
        j++;
        inputArray[k++] = rightVal;
      }
    }

    while (i < leftLen) {
      _spaceTimeComplexity.iterate();
      inputArray[k++] = left[i++];
    }

    while (j < rightLen) {
      _spaceTimeComplexity.iterate();
      inputArray[k++] = right[j++];
    }

    _spaceTimeComplexity.pop();
  }

  /**
   * Divide and conquer merge sort algorithm.
   *
   * @param inputArray the unsorted array
   * @param left the begin index
   * @param right the end index
   */
  public void mergeSort(int[] inputArray, int left, int right) {
//    System.out.println("Divide: " + left + ", " + right);
    _spaceTimeComplexity.push();
    // only have something to do if there are elements between begin and end.
    if (left < right) {
      int m = left + (right - left) / 2;

      // tree left
      mergeSort(inputArray, left, m);

      // tree right
      mergeSort(inputArray, m + 1, right);

      // merge two halves in place
      merge(inputArray, left, m, right);
    }
    _spaceTimeComplexity.pop();
  }

  @Override
  public void run() {
    // example array
    int[] unsortedArray = { 2, 34, 2, 30, 2, 10, 2, 26, 2, 98, 2, 11, 4, 7, 1, 12 };

    mergeSort(unsortedArray, 0, unsortedArray.length - 1);

//    System.out.println("Final sorted array : " + Arrays.toString(unsortedArray));
    _spaceTimeComplexity.printStats();
  }
}
