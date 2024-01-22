package edu.njit.cs114;

public class Search {

    public static void main(String[] args) {

        int[] a = { -2, 5, 10, 15, 22, 27 };
        int target = 22;

        // System.out.println(binSearch(a, target));
        // System.out.println(fact(10));

    }
    public static int binSearch(int[] a, int target, int low, int high) {
        if (low > high) {
            return -1;
        }

        int m = low + (high - low) / 2;

        if (target == a[m]) {
            return m;
        } else if (target > a[m]) {
            return binSearch(a, target, m + 1, high);
        } else {
            return binSearch(a, target, low, m - 1);
        }

    }

    public static int binSearch(int[] a, int target) {
        return binSearch(a, target, 0, a.length - 1);
    }

    public static int fact(int n) {
        if (n == 0) {
            return 1;
        }

        return n * fact(n - 1);
    }

}
