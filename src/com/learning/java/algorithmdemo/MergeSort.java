package com.learning.java.algorithmdemo;

import com.learning.java.utils.DataGeneration;
import com.learning.java.utils.SpaceTimeComplexity;


/**
 * Demonstration of the sequential merge sort algorithm
 */
public class MergeSort implements AlgorithmDemo {
  private static final SpaceTimeComplexity SPACE_TIME_COMPLEXITY = new SpaceTimeComplexity();
  private final int[] unsortedArray;

  public MergeSort() {
    unsortedArray = DataGeneration.randomArray(100000);
  }

  public MergeSort(int[] input) {
    unsortedArray = input;
  }


  public static class MergeSorter {

    /**
     * Merges values from left and right lists into the input list
     * @param inputArray the list being sorted
     * @param l the left index
     * @param m the current mid point
     * @param r the right index
     */
    private void merge(int[] inputArray, int l, int m, int r) {
      SPACE_TIME_COMPLEXITY.push();

      int i, j, k;

      int leftLen = m - l + 1;
      int rightLen = r - m;

      int[] left = new int[leftLen];
      int[] right = new int[rightLen];

      // copy into left
      for (i = 0; i < leftLen; i++) {
        SPACE_TIME_COMPLEXITY.iterate();
        left[i] = inputArray[l + i];
      }

      // copy into right
      for (i = 0; i < rightLen; i++) {
        SPACE_TIME_COMPLEXITY.iterate();
        right[i] = inputArray[m + 1 + i];
      }

      i = 0;
      j = 0;

      // k starts at left index
      k = l;

      while (i < leftLen && j < rightLen) {
        SPACE_TIME_COMPLEXITY.iterate();
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
        SPACE_TIME_COMPLEXITY.iterate();
        inputArray[k++] = left[i++];
      }

      while (j < rightLen) {
        SPACE_TIME_COMPLEXITY.iterate();
        inputArray[k++] = right[j++];
      }
//    System.out.print("Conquer: [" + l + " <-- " + m + " --> " + r + "]: ");
//    System.out.println(Arrays.toString(left) + " -- " + Arrays.toString(right));
      SPACE_TIME_COMPLEXITY.pop();
    }

    /**
     * Divide and conquer merge sort algorithm.
     *
     * @param inputArray the unsorted array
     * @param left the begin index
     * @param right the end index
     */
    public void sort(int[] inputArray, int left, int right) {
//    System.out.println("Divide: " + left + ", " + right);
      SPACE_TIME_COMPLEXITY.push();
      // only have something to do if there are elements between begin and end.
      if (left < right) {
        int m = left + (right - left) / 2;

        // tree left
        sort(inputArray, left, m);

        // tree right
        sort(inputArray, m + 1, right);

        // merge two halves in place
        merge(inputArray, left, m, right);
      }

      SPACE_TIME_COMPLEXITY.pop();
    }

    public int[] sort(int[] inputArray) {
      sort(inputArray, 0, inputArray.length - 1);
      return inputArray;
    }
  }
  @Override
  public void run() {
    SPACE_TIME_COMPLEXITY.timeStart();
    MergeSorter mergeSort = new MergeSorter();
    int[] sortedArray = mergeSort.sort(unsortedArray);
    SPACE_TIME_COMPLEXITY.timeStop();
//    System.out.println("Final sorted array : " + Arrays.toString(unsortedArray));
    SPACE_TIME_COMPLEXITY.printStats();
  }
}
