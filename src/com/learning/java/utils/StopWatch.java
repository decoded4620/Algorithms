package com.learning.java.utils;

/**
 * Very simple single use stopwatch for timing code blocks.
 */
public class StopWatch {
    private long _startTime = 0;

    public void start() {
        _startTime = System.nanoTime();
    }

    public long stop() {
        var val = System.nanoTime() - _startTime;
        _startTime = 0;
        return val;
    }
}
