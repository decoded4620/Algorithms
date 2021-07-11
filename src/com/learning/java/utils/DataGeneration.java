package com.learning.java.utils;

public class DataGeneration {

  public static int[] randomArray(int size, int maxElementValue) {
    int[] randomA = new int[size];

    for(int i = 0; i < size; i++) {
      randomA[i] = (int) Math.round(Math.random() * maxElementValue);
    }

    return randomA;
  }

  public static int[] randomArray(int size) {
    return randomArray(size, (int) Math.round(Math.random() * size));
  }
}
