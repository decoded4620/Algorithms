package com.learning.java.algorithmdemo.concurrency;

import com.learning.java.algorithmdemo.AlgorithmDemo;
import com.learning.java.utils.StopWatch;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * This demonstrates a basic producer consumer using an ArrayBlockingQueue. The timings output includes showing the
 * overhead of threading / context switching by comparing a naive expected completion time, with the actual completion time
 * as well as showing when the queue is blocking the producer due to 0 remaining capacity, if it occurs.
 *
 * The ArrayBlockingQueue has a bounded capacity.
 */
public class ProducerConsumer implements AlgorithmDemo {
  ExecutorService executorService;
  // How many items will be produced
  long totalToProduce = 200 + Math.round(Math.random() * 2000);
  int queueSize = (int) Math.round(totalToProduce / 10.0);

  // tracks how much pause time for the producer to put data on the Queue
  long totalPauseTime = 0L;

  AtomicLong produced = new AtomicLong(0);
  AtomicLong consumed = new AtomicLong(0);
  StopWatch stopWatch = new StopWatch();
  ArrayBlockingQueue<Data> dataQ = new ArrayBlockingQueue<>(queueSize);

  private Future<?> makeConsumer(long timeToCosume) {
    Consumer<Data> consumer = (queue) -> {

      try {
        while (consumed.get() < totalToProduce) {
          queue.take();
          consumed.incrementAndGet();
          Thread.sleep(timeToCosume);
        }
      } catch (InterruptedException ex) {
        System.out.println("Consumer could not wait for consumption ");
        ex.printStackTrace();
      }
    };

    return executorService.submit(() -> consumer.consume(dataQ));
  }

  private Future<?> makeProducer(long timeToProduce) {
    StopWatch totalProducerWaitTime = new StopWatch();
    Producer<Data> producer = (queue) -> {
      totalProducerWaitTime.start();
      var timesBlockedDueToNoRemainingCapacity = 0;
      var maxSize = 0;
      while (produced.get() < totalToProduce) {
        try {
          var remaining = queue.remainingCapacity();
          var wasPaused = false;
          if (remaining == 0) {
            totalProducerWaitTime.pause();
            wasPaused = true;
            timesBlockedDueToNoRemainingCapacity++;
          }

          queue.put(new Data());

          if (wasPaused) {
            totalPauseTime = totalProducerWaitTime.pause();
          }
          maxSize = Math.max(maxSize, queueSize - remaining);

          produced.incrementAndGet();
          Thread.sleep(timeToProduce);
        } catch (InterruptedException ex) {
          ex.printStackTrace();
          break;
        }
      }
      // slightly inaccurate by some nanos since stop not on same line.
      var totalTime = totalProducerWaitTime.currentTime(TimeUnit.MILLISECONDS);
      totalProducerWaitTime.stop();
      System.out.println(
          "Total Producer Time, waiting (ms): " + (totalPauseTime / 1_000_000.0) + ", total (ms):" + totalTime
              + ", times fully blocked: " + timesBlockedDueToNoRemainingCapacity + ", max queue size: " + maxSize);
    };

    return executorService.submit(() -> producer.produce(dataQ));
  }

  @Override
  public void run() {
    // a random time to produce a single element of data
    final int timeToProduce = 5 + (int) Math.ceil(Math.random() * 30);
    // a random time to consume a single element of data
    final int timeToConsume = 5 + (int) Math.ceil(Math.random() * 30);

    // how many consumers are needed to avoid producer contention / blocking. Always round up to ensure more
    // consumers than needed, and producer is always the bottleneck.
    final int totalConsumers = (int) Math.ceil(timeToConsume * 1.0 / timeToProduce);

    executorService = Executors.newFixedThreadPool(totalConsumers + 1);

    // total expected time based on producer time since producer should be the bottleneck
    final long expectedTimeToComplete = totalToProduce * timeToProduce;

    System.out.println(
        "Starting producer consumer, total to produce: " + totalToProduce + ", time to produce one data: "
            + timeToProduce + ", time to consume one data: " + timeToConsume + ", totalConsumers: " + totalConsumers
            + ", expected time to complete (ms): " + expectedTimeToComplete);



    // store the futures for producer / all consumers
    Future<?>[] futures = new Future[totalConsumers + 1];

    // single producer
    futures[0] = makeProducer(timeToProduce);

    // total consumers
    for (int i = 1; i < futures.length; i++) {
      futures[i] = makeConsumer(timeToConsume);
    }

    executorService.shutdown();
    stopWatch.start();
    try {
      // wait for all consumers and producer to complete.
      for (Future<?> future : futures) {
        future.get();
      }
      var totalWorkingTime = stopWatch.currentTime(TimeUnit.MILLISECONDS);
      var timeToCompleteNanos = stopWatch.stop();
      System.out.println("Total working time (ms): " + totalWorkingTime
          + ", difference from expected: " + (timeToCompleteNanos/1_000_000.0
          - expectedTimeToComplete));
    } catch (InterruptedException | ExecutionException ex) {
      System.out.println("An error or interruption occurred during execution");
      ex.printStackTrace();
    }

    /*
     * Sample Output
     * Starting producer consumer, total to produce: 275, time to produce one data: 22, time to consume one data: 6, totalConsumers: 1, expected time to complete (ms): 6050
     * Total Producer Time, waiting (ms): 0.0, total (ms):6799.513481, times fully blocked: 0, max queue size: 0
     * Total working time (ms): 6817.317509, difference from expected: 767.3190459999996
     */
  }

  interface Consumer<T> {
    void consume(BlockingQueue<T> queue);
  }

  interface Producer<T> {
    void produce(BlockingQueue<T> queue);
  }

  public static class Data {
  }
}