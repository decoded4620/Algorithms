package com.learning.java.algorithmdemo.concurrency;

import com.learning.java.algorithmdemo.AlgorithmDemo;
import com.learning.java.utils.SpaceTimeComplexity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * This is a matrix multiplication demonstration, which shows Sequential and Concurrent approaches for
 * solving the matrix multiply problem.
 *
 * Rules of the algorithm.
 *
 * The first matrix m1 must have the same number of rows as the number of columns in m2.
 * Multiplication is such that the  elements in the current row of m1 are multiplied by the elements in the current column of m2
 * and their results are summed together.
 * Lets take the following matrices [[ 2, 2 ], [3, 3]] and [[3,4],[5,6]] as examples for m1 and m2
 *
 * taking the dot product of m1 rows and m2 cols we get (2,3) . (3,4)  and (2,3) . (5,6)
 * dot product means multiply elements in each set
 *
 * 2*3 + 3*4 = 18
 * 2*5 + 3*6 = 28
 * 2*3 + 3*4 = 18
 * 2*5 + 3*6 = 28
 *
 * Results in the final matrix [[18, 28],[18, 28]]
 */
public class MatrixMultiply implements AlgorithmDemo {
  // records different aspects of the execution.
  private static final SpaceTimeComplexity SPACE_TIME_COMPLEXITY = new SpaceTimeComplexity();

  public int[][] matrixMultiply(int[][] m1, int[][] m2) {
    return new SequentialMatrixMultiplier().computeProduct(m1, m2);
  }


  public int[][] matrixMultiplyThreaded(int[][] m1, int[][] m2) {
    return new ThreadedMatrixMultiplier().computeProduct(m1, m2);
  }

  interface MatrixMultiplier {
    /**
     * Compute the product of two matrices
     * @param m1 the first matrix
     * @param m2 the second matrix
     * @return a product matrix.
     */
    int[][] computeProduct(int[][] m1, int [][]m2);
  }

  static class SequentialMatrixMultiplier implements MatrixMultiplier {

    @Override
    public int[][] computeProduct(int[][] m1, int [][]m2) {

      int m1Rows = m1.length;
      int m1Cols = m1.length > 0 ? m1[0].length : 0;
      int m2Cols = m2.length > 0 ? m2[0].length : 0;

      if (m1Rows != m2Cols) {
        throw new IllegalArgumentException("Matrices cannot be multiplied");
      }

      boolean printResults = m1Rows * m1Cols < 36 && m2Cols < 6;

      int[][] multiplied = new int[m1Rows][m2Cols];

      // for each row, get the dot product
      for (int i = 0; i < m1Rows; i++) {
        for (int j = 0; j < m2Cols; j++) {
          int sum = 0;
          for (int k = 0; k < m1Cols; k++) {
            sum += m1[i][k] * m2[k][j];
          }

          multiplied[i][j] = sum;
          if (printResults) {
            System.out.println("Seq Result: " + Arrays.deepToString(multiplied));
          }
        }
      }
      return multiplied;
    }
  }

  static class ThreadedMatrixMultiplier implements MatrixMultiplier {
    private Boolean _printResults = false;

    int m1Rows;
    int m1Cols;
    int m2Cols;
    int[][] m1;
    int [][]m2;

    @Override
    public int[][] computeProduct(int[][] m1, int [][]m2) {
      this.m1 = m1;
      this.m2 = m2;
      m1Rows = this.m1.length;
      m1Cols = this.m1.length > 0 ? this.m1[0].length : 0;
      m2Cols = this.m2.length > 0 ? this.m2[0].length : 0;

      if (m1Rows != m2Cols) {
        throw new IllegalArgumentException("Matrices cannot be multiplied");
      }

      _printResults = m1Rows * m1Cols < 36 && m2Cols < 6;

      // how many workers is based on available processors
      int numWorkers = Runtime.getRuntime().availableProcessors(); //Math.min(Runtime.getRuntime().availableProcessors(), numRows1);

      // create the pool of workers
      ExecutorService pool = Executors.newFixedThreadPool(numWorkers);

      // how many rows will each worker calculate
      int rowsPerWorker = (int) Math.ceil((double) m1Rows / numWorkers);

      // list of concurrent worker results
      List<Future<int[][]>> workerResults = new ArrayList<>();

      // for each worker, figure out which rows are going to be calculated
      for (int w = 0; w < numWorkers; w++) {
        // submit the worker to handle the chunk
        workerResults.add(pool.submit(
            new Worker(Math.min(w * rowsPerWorker, m1Rows), Math.min((w + 1) * rowsPerWorker, m1Rows))));
      }

      // create the result matrix
      int[][] resultMatrix = new int[m1Rows][m2Cols];

      try {
        // for each worker, get the future result
        for (int w = 0; w < workerResults.size(); w++) {
          Future<int[][]> workerResultFuture = workerResults.get(w);

          // this represents the rows calculated
          int[][] workerResult = workerResultFuture.get();
          final int workerResultRows = workerResult.length;
          // for each row in the partial matrix
          for (int i = 0; i < workerResultRows; i++) {
            // get each column value
            for (int j = 0; j < m2Cols; j++) {
              // find the actual row and column and map it to the final matrix, then copy the value over.
              int actualRow = Math.min(i + (w * rowsPerWorker), m1Rows);
              resultMatrix[actualRow][j] = workerResult[i][j];
              if (_printResults) {
                System.out.println("Threaded Result: " + Arrays.deepToString(resultMatrix));
              }
            }
          }
        }
      } catch (InterruptedException | ExecutionException ex) {
        pool.shutdownNow();
        throw new RuntimeException("Could not get multiplied matrix", ex);
      }

      pool.shutdown();
      return resultMatrix;
    }

    class Worker implements Callable<int[][]> {
      int startRow;
      int endRow;
      public Worker(int startRow, int endRow) {
        this.startRow = startRow;
        this.endRow = endRow;
      }

      public int[][] call() {
        int partialRowsCount = endRow - startRow;
        int [][] partialMatrix = new int[partialRowsCount][m2Cols];

        for (int i = 0; i < partialRowsCount; i++) {
          for (int j = 0; j < m2Cols; j++) {
            int sum = 0;
            for (int k = 0; k < m1Cols; k++) {
              sum += m1[i+startRow][k] * m2[k][j];
            }

            // record the dot product
            partialMatrix[i][j] = sum;
            if (_printResults) {
              System.out.println("Partial Result: " + Arrays.deepToString(partialMatrix));
            }
          }
        }

        return partialMatrix;
      }
    }
  }


  @Override
  public void run() {
    int[][] mult1 = generateRandomMatrix(5, 3);
    int[][] mult2 = generateRandomMatrix(3, 5);

    SPACE_TIME_COMPLEXITY.timeStart();
    int [][] multipliedSeq = matrixMultiply(mult1, mult2);
    SPACE_TIME_COMPLEXITY.timeStop();
    SPACE_TIME_COMPLEXITY.printStats();

    SPACE_TIME_COMPLEXITY.reset();
    SPACE_TIME_COMPLEXITY.timeStart();
    int [][] multipliedThreaded = matrixMultiplyThreaded(mult1, mult2);
    SPACE_TIME_COMPLEXITY.timeStop();
    SPACE_TIME_COMPLEXITY.printStats();

    // check for errors between algorithms - answers should match
    if (!Arrays.deepEquals(multipliedSeq, multipliedThreaded)) {
      throw new IllegalStateException("Matrices not equal");
    }

    /*
     * For two 2000 x 2000 random matrices:
     *
     * Output Sequential
     * Total Calls: 0, Total Iterations: 0, Max Stack 0, Total Answers 0, Total Time 58983311230 (58.983311 sec)
     *
     * Output Threaded
     * Total Calls: 0, Total Iterations: 0, Max Stack 0, Total Answers 0, Total Time 9664037195 (9.664037 sec)
     */
  }

  /**
   * Generate a Matrix rows x columns with random entries from 0 -> rows * columns
   * @param rows the number of rows
   * @param columns the number of columns
   * @return an int[][] matrix
   */
  private int[][] generateRandomMatrix(int rows, int columns) {
    int [][] matrix = new int[rows][columns];
    for(int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        matrix[i][j] = (int) Math.floor(Math.random() * rows);
      }
    }

    return matrix;
  }
}
