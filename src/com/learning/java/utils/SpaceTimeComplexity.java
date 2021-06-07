package com.learning.java.utils;

public class SpaceTimeComplexity {
    private final StopWatch _stopWatch = new StopWatch();
    // records different aspects of the execution.
    private long _totalCalls = 0;
    private long _totalIterations = 0;
    private long _totalAnswers = 0;
    private long _maxStack = 0;
    private long _recursionIdx = 0;
    private long _totalTime = 0;

    public void timeStart() {
        _stopWatch.start();
    }

    public void timeStop() {
        _totalTime = _stopWatch.stop();
    }

    public void call() {
        _totalCalls++;
    }

    public void iterate() {
        _totalIterations++;
    }

    public void push() {
        _recursionIdx++;
        _maxStack = Math.max(_recursionIdx, _maxStack);
    }

    public void pop() {
        _recursionIdx--;
    }

    public void addAnswer() {
        _totalAnswers++;
    }

    public void reset() {
        _recursionIdx = 0;
        _maxStack = 0;
        _totalCalls = 0;
        _totalTime = 0;
        _totalAnswers = 0;
    }
    public void printStats() {
        System.out.printf("Total Calls: %d, Total Iterations: %d, Max Stack %d, Total Answers %d, Total Time %d (%f sec)%n",
                _totalCalls, _totalIterations, _maxStack, _totalAnswers, _totalTime, _totalTime / 1_000_000_000.0);
    }
}
