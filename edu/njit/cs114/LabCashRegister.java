package edu.njit.cs114;

import java.util.Arrays;

/**
 * Author: Priyansh Patel
 * Date created: 2/8/2024
 */
public class LabCashRegister {

    private static final int INFINITY = Integer.MAX_VALUE;

    private int[] denominations;

    /**
     * Constructor
     * 
     * @param denominations values of coin types with no particular order
     * @throws Exception when a coin of denomination 1 does not exist
     */
    public LabCashRegister(int[] denominations) throws Exception {
        if (denominations.length == 0) {
            throw new Exception("No denomination values inputted");
        }

        // Linear search to find a unit value
        boolean hasOne = false;
        for (int i = 0; i < denominations.length; i++) {
            if (denominations[i] == 1) {
                hasOne = true;
            }
        }

        if (!hasOne) {
            throw new Exception("Unit value missing");
        } else {
            this.denominations = Arrays.copyOf(denominations, denominations.length);
        }
    }

    // make an array of same size as denominations array with each entry
    // equal to nCoins
    public int[] makeCoinsArray(int nCoins) {
        int[] changes = new int[denominations.length];
        for (int i = 0; i < changes.length; i++) {
            changes[i] = nCoins;
        }
        return changes;
    }

    /**
     * Recursive function that finds the minimum number of coins to make change for
     * the given
     * value using only denominations that are in indices
     * startDenomIndex, startDenomIndex+1,.....denominations.length-1 of the
     * denominations array.The coins used in a denomination should not exceed
     * the available coins for that denomination
     * 
     * @param availableCoins
     * @param startDenomIndex
     * @param remainingValue
     * @return
     */
    private int minimumCoinsForChange(int[] availableCoins, int startDenomIndex, int remainingValue) {

        // Base cases if no denominations remain
        if (startDenomIndex == this.denominations.length) {
            if (remainingValue == 0) {
                return 0;
            } else {
                return INFINITY;
            }
        }

        // Max coins is the minimum of available coin and the
        // floor divison of remaining change and value of coin
        int maxDenomCoins = Math.min(availableCoins[startDenomIndex],
                remainingValue / this.denominations[startDenomIndex]);

        int minResult = INFINITY;

        // Iterate through each possible number of coins chosen
        for (int nCoins = 0; nCoins <= maxDenomCoins; nCoins++) {

            // In the case of choosing nCoins, find optimium case with remaining
            // denominations and modified value
            int greedyResult = minimumCoinsForChange(availableCoins, startDenomIndex + 1,
                    remainingValue - (this.denominations[startDenomIndex] * nCoins));

            // Save optimimum result
            if (greedyResult != INFINITY) {
                minResult = Math.min(minResult, nCoins + greedyResult);
            }
        }

        return minResult;
    }

    /**
     * Wrapper function that finds the minimum number of coins to make change
     * for the given value with restrictions on number of coins available.
     * The coins used in a denomination should not exceed the available coins
     * for that denomination.
     * 
     * @param availableCoins
     * @param value          value for which to make change
     * @return
     */
    public int minimumCoinsForChange(int[] availableCoins, int value) {
        return minimumCoinsForChange(availableCoins, 0, value);
    }

    /**
     * Wrapper function that finds the minimum number of coins to make change for
     * the given value
     * 
     * @param value
     * @return minimum number of coins needed for change
     */
    public int minimumCoinsForChange(int value) {
        return minimumCoinsForChange(makeCoinsArray(INFINITY), 0, value);
    }

    public static void main(String[] args) throws Exception {
        LabCashRegister reg = null;
        try {
            new LabCashRegister(new int[] { 50, 25, 10, 5 });
            assert false;
        } catch (Exception e) {
            assert true;
        }
        reg = new LabCashRegister(new int[] { 25, 1, 50, 5, 10 });
        // should return a total of 7 coins
        int nCoins = reg.minimumCoinsForChange(98);
        System.out.println("Minimum coins to make change for " + 98
                + " from {25,1,50,5,10} = " + nCoins);
        assert nCoins == 7;
        // should return a total of 5 coins
        nCoins = reg.minimumCoinsForChange(58);
        System.out.println("Minimum coins to make change for " + 58
                + " from {25,1,50,5,10} = " + nCoins);
        assert nCoins == 5;
        reg = new LabCashRegister(new int[] { 25, 10, 1 });
        // should return a total of 7 coins
        nCoins = reg.minimumCoinsForChange(34);
        System.out.println("Minimum coins to make change for " + 34
                + " from {25,10,1} = " + nCoins);
        assert nCoins == 7;
        reg = new LabCashRegister(new int[] { 7, 24, 1, 42 });
        // should return a total of 2 coins
        nCoins = reg.minimumCoinsForChange(48);
        System.out.println("Minimum coins to make change for " + 48
                + " from {7,24,1,42} = " + nCoins);
        assert nCoins == 2;
        reg = new LabCashRegister(new int[] { 50, 1, 3, 16, 30 });
        // should return a total of 3 coins
        nCoins = reg.minimumCoinsForChange(35);
        System.out.println("Minimum coins to make change for " + 35
                + " from {50,1,3,16,30} = " + nCoins);
        assert nCoins == 3;
        reg = new LabCashRegister(new int[] { 50, 25, 10, 5, 1 });
        // should return a total of 15 coins
        nCoins = reg.minimumCoinsForChange(new int[] { 2, 3, 20, 100, 100 }, 243);
        System.out.println("Minimum coins to make change for " + 243
                + " from {50, 25, 10, 5, 1}} with available coins = "
                + Arrays.toString(new int[] { 2, 3, 20, 100, 100 }) + " = " + nCoins);
        assert nCoins == 15;
        reg = new LabCashRegister(new int[] { 1, 7, 24, 42 });
        // should return a total of 7 coins
        nCoins = reg.minimumCoinsForChange(new int[] { 100, 20, 1, 1 }, 48);
        System.out.println("Minimum coins to make change for " + 48
                + " from {1, 7, 24, 42}} with available coins = "
                + Arrays.toString(new int[] { 100, 20, 1, 1 }) + " = " + nCoins);
        assert nCoins == 7;
    }
}
