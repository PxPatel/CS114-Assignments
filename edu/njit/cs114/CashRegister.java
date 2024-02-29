package edu.njit.cs114;

import java.util.Arrays;

/**
 * Author: Ravi Varadarajan
 * Date created: 2/9/2024
 */
public class CashRegister {

    public static final int INFINITY = Integer.MAX_VALUE;

    private int[] denominations;

    /**
     * Constructor
     * 
     * @param denominations values of coin types not in any order
     * @throws Exception when there are non positive or duplicate denomination
     *                   values
     */
    public CashRegister(int[] denominations) throws Exception {
        // Check for non-positive values
        for (int denomination : denominations) {
            if (denomination <= 0) {
                throw new Exception("Only denominations of positive value allowed ");
            }
        }

        // Check for duplicate values (using a duplicate array to not tamper original)
        int[] tempArry = Arrays.copyOf(denominations, denominations.length);
        Arrays.sort(tempArry);

        for (int i = 1; i < tempArry.length; i++) {
            if (tempArry[i] == tempArry[i - 1]) {
                throw new Exception("No duplicate denomination allowed");
            }
        }

        // If all checks passed, assign the denominations array
        this.denominations = Arrays.copyOf(denominations, denominations.length);
    }

    // make an array of same size as denominations array with each entry equal to
    // nCoins
    public int[] makeCoinsArray(int nCoins) {
        int[] changes = new int[denominations.length];
        for (int i = 0; i < changes.length; i++) {
            changes[i] = nCoins;
        }
        return changes;
    }

    // Helper function to total number of coins in a solution
    private int numCoins(int[] changeCoins) {
        int sum = 0;
        for (int i = 0; i < changeCoins.length; i++) {
            if (changeCoins[i] == INFINITY) {
                return INFINITY;
            }
            sum += changeCoins[i];
        }
        return sum;
    }

    /**
     * Make change for value using minimum number of coins
     * using available coins of the different denominations
     * 
     * @param value          amount to make change
     * @param availableCoins coins available in each denomination
     * @param minCoinsToUse  minimum coins to use in each denomination (note
     *                       minCoinsToUse[i] <= availableCoins[i])
     * @return array of same length as denominations array that specifies
     *         coins of each denomination (in the same order as specified in
     *         denomination values array)
     *         to use in making given change with minimum number of coins. Each
     *         entry in the returned
     *         array should not exceed the corresponding entry in the availableCoins
     *         array and should not be
     *         smaller than the corresponding entry in minCoinsToUseArray
     */
    public int[] makeChange(int value, int[] availableCoins, int[] minCoinsToUse) {
        return makeChange(value, availableCoins, minCoinsToUse, 0);
    }

    // Recursive Function
    public int[] makeChange(int remainingValue, int[] availableCoins, int[] minCoinsToUse, int index) {
        // Base Cases: if reached end of denomination array
        if (index == this.denominations.length) {
            if (remainingValue == 0) {
                return makeCoinsArray(0);
            } else {
                return makeCoinsArray(INFINITY);
            }
        }

        // Upper limit of how many coins of a particular denomination possible in
        // solution
        int maxDenomCoins = Math.min(availableCoins[index],
                remainingValue / this.denominations[index]);

        // Store best solution in a variable
        int[] minCoinChanges = makeCoinsArray(INFINITY);

        // Iterate through each nCoin choice to find best solution
        for (int nCoins = minCoinsToUse[index]; nCoins <= maxDenomCoins; nCoins++) {
            // Call function recursively to find optimal solution
            int[] minRemCoinChanges = makeChange(remainingValue - (nCoins * this.denominations[index]), availableCoins,
                    minCoinsToUse, index + 1);

            // If numCoins is INFINITY, means no solution, so continue iteration
            if (numCoins(minRemCoinChanges) == INFINITY) {
                continue;
            }
            // If new best solution found, update stored variable
            else if ((nCoins + numCoins(minRemCoinChanges)) < numCoins(minCoinChanges)) {
                minCoinChanges = Arrays.copyOf(minRemCoinChanges, minRemCoinChanges.length);
                minCoinChanges[index] = nCoins;
            }
        }
        return minCoinChanges;
    }

    /**
     * Make change for value using minimum number of coins
     * using available coins of the different denominations
     * 
     * @param value          amount to make change
     * @param availableCoins coins available in each denomination
     * @return array of same length as denominations array that specifies
     *         coins of each denomination (in the same order as specified in
     *         denomination values array)
     *         to use in making given change with minimum number of coins. Each
     *         entry in the returned
     *         array should not exceed the corresponding entry in the availableCoins
     *         array
     */
    public int[] makeChange(int value, int[] availableCoins) {
        return makeChange(value, availableCoins, makeCoinsArray(0));
    }

    /**
     * Make change for value using minimum number of coins
     * assuming unlimited availability of coins of each denomination
     * 
     * @param value
     * @return array of same length as denominations array that specifies
     *         coins of each denomination (in the same order as specified in
     *         denomination values array)
     *         to use in making given change with minimum number of coins.
     */
    public int[] makeChange(int value) {
        return makeChange(value, makeCoinsArray(INFINITY));
    }

    /**
     * Specifies description of change in coins if solution exists
     * 
     * @param coins
     * @return
     */
    public void printValues(int[] coins) {
        if (coins[0] == INFINITY) {
            System.out.println("Change not possible");
            return;
        }
        StringBuilder builder = new StringBuilder();
        int totalCoins = 0;
        for (int i = 0; i < denominations.length; i++) {
            if (coins[i] > 0) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(coins[i] + " coins of value " + denominations[i]);
                totalCoins += coins[i];
            }
        }
        builder.append(" for a total of " + totalCoins + " coins");
        System.out.println(builder.toString());
    }

    public static void main(String[] args) throws Exception {
        CashRegister reg = new CashRegister(new int[] { 50, 25, 10, 5, 1 });
        System.out.println(
                "Change for " + 96 + " from denominations " + Arrays.toString(new int[] { 50, 25, 10, 5, 1 }) + ":");
        int[] change = reg.makeChange(96);
        // should have a total of 5 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 5;
        System.out.println("Change for " + 98 + " from denominations " + Arrays.toString(new int[] { 50, 25, 10, 5, 1 })
                + " with restricted coin availability "
                + Arrays.toString(new int[] { 2, 3, 1, 2, 100 }) + ":");
        change = reg.makeChange(98, new int[] { 2, 3, 1, 2, 100 });
        // should have a total of 8 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 8;
        System.out.println("Change for " + 98 + " from denominations " + Arrays.toString(new int[] { 50, 25, 10, 5, 1 })
                + " with restricted coin availability "
                + Arrays.toString(new int[] { 2, 3, 1, 2, 100 })
                + " and minimum coins to use " + Arrays.toString(new int[] { 0, 2, 0, 0, 0 }) + ":");
        change = reg.makeChange(98, new int[] { 2, 3, 1, 2, 100 }, new int[] { 0, 2, 0, 0, 0 });
        // should have a total of 9 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 9;
        System.out.println(
                "Change for " + 58 + " from denominations " + Arrays.toString(new int[] { 50, 25, 10, 5, 1 }) + ":");
        change = reg.makeChange(58);
        // should have a total of 5 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 5;
        System.out.println("Change for " + 58 + " from denominations " + Arrays.toString(new int[] { 50, 25, 10, 5, 1 })
                + " with restricted coin availability "
                + Arrays.toString(new int[] { 2, 2, 2, 2, 2 }) + ":");
        change = reg.makeChange(58, new int[] { 2, 2, 2, 2, 2 });
        // should have no solution
        reg.printValues(change);
        assert change[0] == INFINITY;
        reg = new CashRegister(new int[] { 25, 10, 1 });
        System.out
                .println("Change for " + 34 + " from denominations " + Arrays.toString(new int[] { 25, 10, 1 }) + ":");
        change = reg.makeChange(34);
        // should have a total of 7 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 7;
        reg = new CashRegister(new int[] { 1, 7, 24, 42 });
        System.out.println(
                "Change for " + 48 + " from denominations " + Arrays.toString(new int[] { 1, 7, 24, 42 }) + ":");
        change = reg.makeChange(48);
        // should have a total of 2 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 2;
        System.out.println("Change for " + 48 + " from denominations " + Arrays.toString(new int[] { 1, 7, 24, 42 })
                + " with restricted coin availability "
                + Arrays.toString(new int[] { 100, 20, 1, 1 }) + ":");
        change = reg.makeChange(48, new int[] { 100, 20, 1, 1 });
        // should have a total of 7 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 7;
        System.out.println("Change for " + 48 + " from denominations " + Arrays.toString(new int[] { 1, 7, 24, 42 })
                + " with restricted coin availability "
                + Arrays.toString(new int[] { 100, 20, 1, 1 }) + ":");
        change = reg.makeChange(48, new int[] { 100, 20, 1, 1 });
        // should have a total of 7 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 7;
        System.out.println("Change for " + 48 + " from denominations " + Arrays.toString(new int[] { 1, 7, 24, 42 })
                + " with restricted coin availability "
                + Arrays.toString(new int[] { 5, 20, 1, 1 })
                + " and minimum coins to use " + Arrays.toString(new int[] { 0, 0, 0, 1 }) + ":");
        change = reg.makeChange(48, new int[] { 5, 20, 1, 1 }, new int[] { 0, 0, 0, 1 });
        // should have no solution
        reg.printValues(change);
        assert change[0] == INFINITY;
        reg = new CashRegister(new int[] { 50, 1, 3, 16, 30 });
        System.out.println(
                "Change for " + 35 + " from denominations " + Arrays.toString(new int[] { 50, 1, 3, 16, 30 }) + ":");
        change = reg.makeChange(35);
        // should have a total of 3 coins
        reg.printValues(change);
        assert Arrays.stream(change).sum() == 3;
    }
}
