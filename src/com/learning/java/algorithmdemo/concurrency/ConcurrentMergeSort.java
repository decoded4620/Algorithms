package com.learning.java.algorithmdemo.concurrency;

import com.learning.java.algorithmdemo.AlgorithmDemo;
import com.learning.java.algorithmdemo.MergeSort;
import com.learning.java.utils.DataGeneration;
import com.learning.java.utils.StopWatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;


/**
 * Concurrent Merge Sort Demo. Comparison between the Classic Merge Sort to demonstrate the advantage of
 * concurrent model when sorting very large arrays.
 */
public class ConcurrentMergeSort implements AlgorithmDemo  {
  public MergeSort _classicMergeSort;
  public ParallelMergeSorter _parallelMergeSorter;

  public static class ParallelMergeSorter  {
    int workers = Runtime.getRuntime().availableProcessors();
    ForkJoinPool _pool = new ForkJoinPool(workers);

    private final int[] _inputArray;
    public ParallelMergeSorter (int[] array) {
      _inputArray = array;
    }

    /**
     * Using Java's ForkJoin framework to parallel sort using {@link RecursiveAction}
     */
    private class ParallelWorker extends RecursiveAction {
      private final int _left;
      private final int _right;

      public ParallelWorker(int left, int right) {
        _left = left;
        _right = right;
      }

      /**
       * Divide and conquer merge sort algorithm.
       */
      public void compute() {
        if (_left < _right) {
          int m = _left + (_right - _left) / 2;

          // invoke more parallel workers to continue dividing and sorting
          invokeAll(
              // tree left
              new ParallelWorker(_left, m),
              // tree right
              new ParallelWorker(m + 1, _right));

          merge(_left, m, _right);
        }
      }
    }

    public int[] sort() {
      // don't use concurrency on small arrays.
      if (_inputArray.length < 100_000) {
        new MergeSort.MergeSorter().sort(_inputArray);
      } else {
        _pool.execute(new ParallelWorker(0, _inputArray.length - 1));
      }
      return _inputArray;
    }

    private void merge(int l, int m, int r) {
      int i, j, k;
      int leftLen = m - l + 1;
      int rightLen = r - m;

      int[] left = new int[leftLen];
      int[] right = new int[rightLen];

      for (i = 0; i < leftLen; i++) {
        left[i] = _inputArray[l + i];
      }

      int midStart =  m + 1;
      for (i = 0; i < rightLen; i++) {
        right[i] = _inputArray[midStart + i];
      }

      i = 0;
      j = 0;
      k = l;

      while (i < leftLen && j < rightLen) {
        int nextLeft = left[i];
        int nextRight = right[j];

        if (nextLeft < nextRight) {
          _inputArray[k++] = nextLeft;
          i++;
        } else {
          _inputArray[k++] = nextRight;
          j++;
        }
      }

      while (i < leftLen) {
        _inputArray[k++] = left[i++];
      }

      while (j < rightLen) {
        _inputArray[k++] = right[j++];
      }
    }
  }


  @Override
  public void run() {
    int[] array = DataGeneration.randomArray(100_000_000);
    runClassic(array);

    array = DataGeneration.randomArray(100_000_000);
    runConcurrent(array);
  }

  private void runConcurrent(int[] array) {
    _parallelMergeSorter = new ParallelMergeSorter(array);

    StopWatch stopWatch = new StopWatch();

    stopWatch.start();

    int[] sortedArray = _parallelMergeSorter.sort();

    double timeTaken = stopWatch.currentTime(TimeUnit.SECONDS);

    stopWatch.stop();
    System.out.println("Concurrent Time taken: " + timeTaken);
  }

  private void runClassic(int[] array) {
    _classicMergeSort = new MergeSort(array);

    StopWatch stopWatch = new StopWatch();

    stopWatch.start();

    _classicMergeSort.run();

    double timeTaken = stopWatch.currentTime(TimeUnit.SECONDS);

    stopWatch.stop();
    System.out.println("Sequential Time taken: " + timeTaken);
  }
}
