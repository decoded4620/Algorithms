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
}
