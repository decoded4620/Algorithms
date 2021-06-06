package com.learning.java.dynamic;

import com.learning.java.AlgorithmDemo;
import com.learning.java.utils.SpaceTimeComplexity;

import java.util.HashMap;
import java.util.Map;

/**
 * An algorithm demo which demonstrates three solutions to calculating a Fibonacci sequence to the Nth number.
 */
public class CalculateFibonacciSequence implements AlgorithmDemo {

    // records different aspects of the execution.
    private final SpaceTimeComplexity _spaceTimeComplexity = new SpaceTimeComplexity();

    /**
     * Calculates the nth Fibonacci number using brute force recursion.
     *
     * @param n the nth Fibonacci number to calculate
     * @return the final (Nth) Fibonacci number.
     */
    private int fibonacciRecursive(int n) {
        _spaceTimeComplexity.call();
        _spaceTimeComplexity.push();
        if (n == 0 || n == 1) {
            _spaceTimeComplexity.pop();
            return n;
        }

        int fibonacci = fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
        _spaceTimeComplexity.pop();
        return fibonacci;
    }

    private final Map<Integer, Integer> memoizedFibonacciMap = new HashMap<>();

    /**
     * Calculates the nth Fibonacci number using memoized recursion
     * @param n the Fibonacci sequence number to calculate
     * @return the final Nth Fibonacci number.
     */
    private int fibonacciMemoized(int n) {
        _spaceTimeComplexity.call();
        _spaceTimeComplexity.push();
        if (n == 0 || n == 1) {
            _spaceTimeComplexity.pop();
            return n;
        }

        if (memoizedFibonacciMap.containsKey(n)) {
            return memoizedFibonacciMap.get(n);
        }

        int lastFibo1 = fibonacciMemoized(n - 1);
        int lastFibo2 = fibonacciRecursive(n - 2);

        memoizedFibonacciMap.put(n - 1, lastFibo1);
        memoizedFibonacciMap.put(n - 2, lastFibo2);

        var result = lastFibo1 + lastFibo2;
        memoizedFibonacciMap.put(n, result);
        _spaceTimeComplexity.pop();
        return lastFibo1 + lastFibo2;
    }

    private int fibonacciTabulated(int n) {
        _spaceTimeComplexity.call();
        _spaceTimeComplexity.push();
        if (n == 0 || n == 1) {
            _spaceTimeComplexity.pop();
            return n;
        }

        int firstFibo = 0;
        int nextFibo = 1;

        int fibo = 0;
        for (int i = 2; i <= n; i++) {
            fibo = firstFibo + nextFibo;
            int saveNext = nextFibo;
            nextFibo = firstFibo + nextFibo;
            firstFibo = saveNext;
        }

        _spaceTimeComplexity.pop();
        return fibo;
    }

    @Override
    public void run() {
        final int totalRuns = 10;
        System.out.println("--------------------- Recursive Fibonacci ---------------------");
        // recursive run (complexity is O(2^N) with O(n) space)
        for(int i = 0; i < totalRuns; i++) {
            _spaceTimeComplexity.timeStart();
            int fiboNumber = fibonacciRecursive(i);
            _spaceTimeComplexity.timeStop();
            _spaceTimeComplexity.addAnswer();
            System.out.printf("Fibo: %d, ", fiboNumber);
            _spaceTimeComplexity.printStats();
            _spaceTimeComplexity.reset();
        }

        System.out.println("--------------------- Memoized Fibonacci ---------------------");

        // memoized run
        // recursive run (complexity is O(2^N) with O(n) space)
        for(int i = 0; i < totalRuns; i++) {
            _spaceTimeComplexity.timeStart();
            int fiboNumber = fibonacciMemoized(i);
            _spaceTimeComplexity.timeStop();
            System.out.printf("Fibo: %d, ", fiboNumber);
            _spaceTimeComplexity.addAnswer();
            _spaceTimeComplexity.printStats();
            _spaceTimeComplexity.reset();
        }
        System.out.println("--------------------- Tabulated Fibonacci ---------------------");
        // tabulated run
        // recursive run (complexity is O(n) with O(1) space)
        for(int i = 0; i < totalRuns; i++) {
            _spaceTimeComplexity.timeStart();
            int fiboNumber = fibonacciTabulated(i);
            _spaceTimeComplexity.timeStop();
            System.out.printf("Fibo: %d ,", fiboNumber);
            _spaceTimeComplexity.addAnswer();
            _spaceTimeComplexity.printStats();
            _spaceTimeComplexity.reset();
        }
    }

    /*
     * The output is as follows for the first 10 fibonacci numbers
     *
     * --------------------- Recursive Fibonacci ---------------------
     * Fibo: 0, Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 18100 (0.000018 sec)
     * Fibo: 1, Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 2000 (0.000002 sec)
     * Fibo: 1, Total Calls: 3, Max Stack 2, Total Answers 1, Total Time 2500 (0.000003 sec)
     * Fibo: 2, Total Calls: 5, Max Stack 3, Total Answers 1, Total Time 3800 (0.000004 sec)
     * Fibo: 3, Total Calls: 9, Max Stack 4, Total Answers 1, Total Time 5900 (0.000006 sec)
     * Fibo: 5, Total Calls: 15, Max Stack 5, Total Answers 1, Total Time 5300 (0.000005 sec)
     * Fibo: 8, Total Calls: 25, Max Stack 6, Total Answers 1, Total Time 6600 (0.000007 sec)
     * Fibo: 13, Total Calls: 41, Max Stack 7, Total Answers 1, Total Time 9500 (0.000010 sec)
     * Fibo: 21, Total Calls: 67, Max Stack 8, Total Answers 1, Total Time 17200 (0.000017 sec)
     * Fibo: 34, Total Calls: 109, Max Stack 9, Total Answers 1, Total Time 35900 (0.000036 sec)
     * --------------------- Memoized Fibonacci ---------------------
     * Fibo: 0, Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 5600 (0.000006 sec)
     * Fibo: 1, Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1400 (0.000001 sec)
     * Fibo: 1, Total Calls: 3, Max Stack 2, Total Answers 1, Total Time 20300 (0.000020 sec)
     * Fibo: 2, Total Calls: 3, Max Stack 3, Total Answers 1, Total Time 11300 (0.000011 sec)
     * Fibo: 3, Total Calls: 5, Max Stack 4, Total Answers 1, Total Time 6200 (0.000006 sec)
     * Fibo: 5, Total Calls: 7, Max Stack 5, Total Answers 1, Total Time 5200 (0.000005 sec)
     * Fibo: 8, Total Calls: 11, Max Stack 6, Total Answers 1, Total Time 14700 (0.000015 sec)
     * Fibo: 13, Total Calls: 17, Max Stack 7, Total Answers 1, Total Time 10800 (0.000011 sec)
     * Fibo: 21, Total Calls: 27, Max Stack 8, Total Answers 1, Total Time 17200 (0.000017 sec)
     * Fibo: 34, Total Calls: 43, Max Stack 9, Total Answers 1, Total Time 29800 (0.000030 sec)
     * --------------------- Tabulated Fibonacci ---------------------
     * Fibo: 0 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 5500 (0.000006 sec)
     * Fibo: 1 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1000 (0.000001 sec)
     * Fibo: 1 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1200 (0.000001 sec)
     * Fibo: 2 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1200 (0.000001 sec)
     * Fibo: 3 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1200 (0.000001 sec)
     * Fibo: 5 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1300 (0.000001 sec)
     * Fibo: 8 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1200 (0.000001 sec)
     * Fibo: 13 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1400 (0.000001 sec)
     * Fibo: 21 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1300 (0.000001 sec)
     * Fibo: 34 ,Total Calls: 1, Max Stack 1, Total Answers 1, Total Time 1000 (0.000001 sec)
     */
}
