package edu.njit.cs114;

import java.util.Arrays;

/**
 * Author: Priyansh Patel
 * Date created: 1/18/2024
 */

public class ArrayStudy {
    public static double[][] manipulate(double[][] a, double fraction) {
        // Perform step (a)
        double[][] a1 = extract(a, fraction);
        System.out.println("Content of array after step (a) for fraction " +
                fraction + " :");
        printArray(a1);
        // Perform step (b)
        double[][] a2 = replace(a1);
        System.out.println("Content of array after step (b) for fraction " +
                fraction + " :");
        printArray(a2);
        return a2;
    }

    // prints 2D array
    private static void printArray(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(String.format("%.2f", a[i][j]) + ",");
            }
            System.out.println();
        }
    }

    private static double[][] extract(double[][] a, double fraction) {
        // Input check
        if (a.length == 0) {
            return a;
        }
        int fractionNumCols = 0;
        int noOfNegativesInRow = 0;
        int noOfRowsRemoved = 0;
        boolean[] removeRow = new boolean[a.length];

        // Loop through each row of a to check if number of negatives is at least
        // halfCol
        for (int i = 0; i < a.length; i++) {
            fractionNumCols = (int) Math.ceil(a[i].length * fraction);
            noOfNegativesInRow = 0;

            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0) {
                    noOfNegativesInRow++;
                }
                if (noOfNegativesInRow >= fractionNumCols) {
                    removeRow[i] = true;
                    noOfRowsRemoved++;
                    break;
                }
            }
        }

        // allocate a new array
        double[][] b = new double[a.length - noOfRowsRemoved][];
        int validRowIndex = 0;

        for (int i = 0; i < a.length; i++) {
            if (!removeRow[i]) {
                b[validRowIndex] = Arrays.copyOf(a[i], a[i].length);
                validRowIndex++;
            }
        }
        return b;
    }

    private static double[][] replace(double[][] b) {
        double columnSum = 0.0; // Column sum
        double columnAverage = 0.0;// Column average
        int numNonNegatives = 0; // number of positives in the column

        // Create a new 2d array to avoid tampering object b
        double[][] c = new double[b.length][b[0].length];

        // For each column, find the sum and average of
        // non-negative numbers in that column
        // Then, assign values to the new 2d array with appropriate requirements
        for (int j = 0; j < b[0].length; j++) {
            columnSum = 0.0;
            numNonNegatives = 0;
            for (int i = 0; i < b.length; i++) {
                if (b[i][j] >= 0) {
                    columnSum += b[i][j];
                    numNonNegatives++;
                }
            }

            columnAverage = numNonNegatives > 0 ? columnSum / numNonNegatives : 0;

            for (int i = 0; i < b.length; i++) {
                c[i][j] = b[i][j] < 0 ? columnAverage : b[i][j];
            }
        }
        return c;
    }

    public static void main(String[] args) {
        double[][] a = {
                { -1, 4, 3, 2, -3, 2 },
                { -2, 3, 5, -4, 0, 1 },
                { -1, -3, -4, 1, -1, 0 },
                { -1, 2, -3, 6, 5, 3 },
                { -3, 2, -3, -5, 0, 0 }
        }; // A 5 x 6 Dimension;
        System.out.println("Printing input array...");
        printArray(a);

        // Test1
        double[][] b = manipulate(a, 0.5);

        // Test 2
        b = manipulate(a, 0.6);

        System.out.println("Printing original array...");
        printArray(a);
    }
}