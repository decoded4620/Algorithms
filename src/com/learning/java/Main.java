package com.learning.java;

import com.learning.java.backtracking.PrintPermutations;

import java.lang.reflect.InvocationTargetException;

/**
 * Use this class for testing different algorithms.
 */
public class Main {
    private static Class<? extends AlgorithmDemo> demoClass;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // change this to any other class, or pass it in as an argument
        if (args.length > 0) {
            String className = args[0];

            try {
                demoClass = (Class<? extends AlgorithmDemo>) Class.forName(className);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                System.err.printf("Class supplied %s was not found %n", className);
            } catch (ClassCastException ex) {
                System.err.printf("Class supplied %s was not an AlgorithmDemo class %n", className);
                ex.printStackTrace();
            }
        }

        demoClass = PrintPermutations.class;

        AlgorithmDemo instance = null;

        try {
            instance = demoClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException ex) {
            System.err.printf("Cannot find constructor for %s%n", demoClass.getName());
            ex.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.err.printf("Cannot create instance of %s%n", demoClass.getName());
            ex.printStackTrace();
        }

        if (instance != null) {
            instance.run();
        } else {
            System.exit(1);
        }
    }
}
