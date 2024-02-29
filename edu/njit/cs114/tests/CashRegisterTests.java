package edu.njit.cs114.tests;

import edu.njit.cs114.CashRegister;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Priyansh Patel
 * Date created: 2/12/2024
 */
public class CashRegisterTests extends UnitTests {

    public static final int INFINITY = Integer.MAX_VALUE;

    @Test
    public void Test1() {
        // test for non positive value check
        try {
            CashRegister reg = new CashRegister(new int [] {25, -10, 5});
            assertTrue(false);
        } catch (Exception e) {
            // Should be reached here by valid code
            assertTrue(true);
            totalScore += 1;
        }
        try {
            CashRegister reg = new CashRegister(new int [] {25, 0, 5});
            assertTrue(false);
        } catch (Exception e) {
            // Should be reached here by valid code
            assertTrue(true);
            totalScore += 2;
            success("Test1");
        }
    }

    @Test
    public void Test2() {
        // test for duplicate value check
        try {
            CashRegister reg = new CashRegister(new int [] {5, 10, 5});
            assertTrue(false);
        } catch (Exception e) {
            // Should be reached here by valid code
            assertTrue(true);
            totalScore += 2;
            success("Test2");
        }
    }

    private static boolean verifyOutput(int [] outputArr,
                                        int [] denominations,
                                        int change,
                                        int [] expectedOutputArr) {
        if (expectedOutputArr[0] == Integer.MAX_VALUE) {
            for (int i=0; i < denominations.length; i++) {
                if (outputArr[i] != Integer.MAX_VALUE) {
                    return false;
                }
            }
            return true;
        }
        int totCoins = 0;
        int totValue = 0;
        int minCoins = 0;
        for (int i=0; i < denominations.length; i++) {
            totCoins += outputArr[i];
            minCoins += expectedOutputArr[i];
            totValue += (denominations[i] * outputArr[i]);
        }
        return totValue == change && totCoins == minCoins;
    }

    @Test
    public void Test3() {
        // check for greedy solution case 1
        try {
            int [] denominations = new int [] {1, 10, 5, 25};
            CashRegister reg = new CashRegister(denominations);
            int [] coins = reg.makeChange(68);
            assertTrue(verifyOutput(coins, denominations, 68, new int [] {3, 1, 1, 2}));
            totalScore += 5;
            success("Test3");
        } catch (Exception e) {
            failure("Test3", e);
        }
    }

    @Test
    public void Test4() {
        // check for greedy solution case 2
        try {
            int [] denominations = new int [] {1, 3, 9, 27};
            CashRegister reg = new CashRegister(denominations);
            int [] coins = reg.makeChange(24);
            assertTrue(verifyOutput(coins, denominations, 24, new int [] {0, 2, 2, 0}));
            totalScore += 5;
            success("Test4");
        } catch (Exception e) {
            failure("Test4", e);
        }
    }

    @Test
    public void Test5() {
        // check for non-greedy solution case 1
        try {
            int [] denominations = new int [] {1, 3, 16, 30, 50};
            CashRegister reg = new CashRegister(denominations);
            int [] coins = reg.makeChange(65);
            assertTrue(verifyOutput(coins, denominations, 65, new int [] {0, 1, 2, 1, 0}));
            totalScore += 8;
            success("Test5");
        } catch (Exception e) {
            failure("Test5", e);
        }
    }

    @Test
    public void Test6() {
        // check for non-greedy solution case 2
        try {
            int [] denominations = new int [] {25, 1, 10};
            CashRegister reg = new CashRegister(denominations);
            int [] coins = reg.makeChange(44);
            assertTrue(verifyOutput(coins, denominations, 44, new int [] {0, 4, 4}));
            totalScore += 6;
            success("Test6");
        } catch (Exception e) {
            failure("Test6", e);
        }
    }

    @Test
    public void Test7() {
        // check for random case
        try {
            int [] denominations = new int[] {1, 49, 46, 30, 28, 41, 21, 8,
                                             31, 18, 39, 23, 13, 27, 48, 12, 34, 7, 19, 56 };
            CashRegister reg = new CashRegister(denominations);
            int change = 167;
            int [] coins = reg.makeChange(change);
            int [] minCoinsArr = new int[20];
            minCoinsArr[14] = minCoinsArr[17] = 1;
            minCoinsArr[19] = 2;
            assertTrue(verifyOutput(coins, denominations, change, minCoinsArr));
            totalScore += 8;
            success("Test7");
        } catch (Exception e) {
            failure("Test7", e);
        }
    }

    @Test
    public void Test8() {
        // check for prime number denominations other than unit value
        try {
            int [] denominations = new int[] {5, 11, 17, 29, 37, 53, 1};
            CashRegister reg = new CashRegister(denominations);
            int change = 195;
            int [] coins = reg.makeChange(change);
            int [] minCoinsArr = new int[denominations.length];
            minCoinsArr[5] = 1;
            minCoinsArr[4] = 3;
            minCoinsArr[3] = 1;
            minCoinsArr[6] = 2; //53*1+37*3+29*1+1*2
            assertTrue(verifyOutput(coins, denominations, change, minCoinsArr));
            totalScore += 10;
            success("Test8");
        } catch (Exception e) {
            failure("Test8", e);
        }
    }

    @Test
    public void Test9() {
        // check for availability constraint case with a feasible solution
        try {
            int [] denominations = new int[] {5, 11, 17, 29, 37, 53, 1};
            CashRegister reg = new CashRegister(denominations);
            int change = 319;
            int [] availableCoins = new int [] {4,4,4,4,3,4,10};
            int [] coins = reg.makeChange(change, availableCoins);
            assertTrue(Arrays.equals(availableCoins, new int [] {4,4,4,4,3,4,10}));
            int [] minCoinsArr = new int[denominations.length];
            minCoinsArr[1] = 1;
            minCoinsArr[3] = 2;
            minCoinsArr[4] = 1;
            minCoinsArr[5] = 4;
            minCoinsArr[6] = 1;
            assertTrue(verifyOutput(coins, denominations, change, minCoinsArr));
            totalScore += 10;
            success("Test9");
        } catch (Exception e) {
            failure("Test9", e);
        }
    }

    @Test
    public void Test10() {
        // check for availability constraint case with no solution
        try {
            int [] denominations = new int[] {5, 17, 37, 53, 1};
            CashRegister reg = new CashRegister(denominations);
            int change = 172;
            int [] availableCoins = new int [] {0,6,3,2,5};
            int [] coins = reg.makeChange(change,availableCoins);
            assertTrue(Arrays.equals(availableCoins, new int [] {0,6,3,2,5}));
            int [] minCoinsArr = new int[denominations.length];
            for (int i=0; i < minCoinsArr.length; i++) {
                minCoinsArr[i] = Integer.MAX_VALUE;
            }
            assertTrue(verifyOutput(coins, denominations, change, minCoinsArr));
            totalScore += 10;
            success("Test10");
        } catch (Exception e) {
            failure("Test10", e);
        }
    }

    @Test
    public void Test11() {
        // check for availability + min coins constraint case with a feasible solution
        try {
            int [] denominations = new int[] {5, 11, 17, 29, 37, 53, 1};
            int [] availableCoins = new int [] {4,4,4,4,3,4,10};
            int [] minCoinsToUse = new int [] {2,0,2,0,0,0,0};
            CashRegister reg = new CashRegister(denominations);
            int change = 319;
            int [] coins = reg.makeChange(change, availableCoins, minCoinsToUse);
            assertTrue(Arrays.equals(availableCoins, new int [] {4,4,4,4,3,4,10}));
            assertTrue(Arrays.equals(minCoinsToUse, new int [] {2,0,2,0,0,0,0}));
            int [] minCoinsArr = new int[denominations.length];
            minCoinsArr[0] = 2;
            minCoinsArr[2] = 2;
            minCoinsArr[3] = 2;
            minCoinsArr[4] = 3;
            minCoinsArr[5] = 2;
            assertTrue(verifyOutput(coins, denominations, change, minCoinsArr));
            totalScore += 10;
            success("Test11");
        } catch (Exception e) {
            failure("Test11", e);
        }
    }

    @Test
    public void Test12() {
        // check for min coins constraint case with a feasible solution
        try {
            int [] denominations = new int[] {5, 11, 17, 29, 37, 53, 1};
            int [] availableCoins = new int [] {INFINITY,INFINITY,INFINITY,INFINITY,INFINITY,INFINITY,INFINITY};
            int [] minCoinsToUse = new int [] {2,0,2,0,0,0,0};
            CashRegister reg = new CashRegister(denominations);
            int change = 319;
            int [] coins = reg.makeChange(change, availableCoins, minCoinsToUse);
            assertTrue(Arrays.equals(availableCoins, new int [] {INFINITY,INFINITY,INFINITY,INFINITY,INFINITY,INFINITY,INFINITY}));
            assertTrue(Arrays.equals(minCoinsToUse, new int [] {2,0,2,0,0,0,0}));
            int [] minCoinsArr = new int[denominations.length];
            minCoinsArr[0] = 2;
            minCoinsArr[2] = 2;
            minCoinsArr[4] = 6;
            minCoinsArr[5] = 1;
            assertTrue(verifyOutput(coins, denominations, change, minCoinsArr));
            totalScore += 6;
            success("Test12");
        } catch (Exception e) {
            failure("Test12", e);
        }
    }

    @Test
    public void Test13() {
        // check for availability + min coin constraint case with no solution
        try {
            int [] denominations = new int[] {5, 11, 17, 29, 37, 53, 1};
            int [] availableCoins = new int [] {4,4,1,4,3,4,10};
            int [] minCoinsToUse = new int [] {2,0,2,0,0,0,0};
            CashRegister reg = new CashRegister(denominations);
            int change = 319;
            int [] coins = reg.makeChange(change, availableCoins, minCoinsToUse);
            int [] minCoinsArr = new int[denominations.length];
            for (int i=0; i < minCoinsArr.length; i++) {
                minCoinsArr[i] = Integer.MAX_VALUE;
            }
            assertTrue(verifyOutput(coins, denominations, change, minCoinsArr));
            totalScore += 5;
            success("Test13");
        } catch (Exception e) {
            failure("Test13", e);
        }
    }

    @Test
    public void Test14() {
        // check if too many recursive calls are unnecessarily made
        try {
            int [] denominations = new int [] {25, 1, 5, 10};
            CashRegister reg = new CashRegister(denominations);
            int [] coins = reg.makeChange(5000004, new int [] {10, 5, 0, 1000000});
            assertTrue(verifyOutput(coins, denominations, 5000004 , new int [] {10, 4, 0, 499975}));
            totalScore += 5;
            success("Test14");
        } catch (Exception e) {
            failure("Test14", e);
        }
    }

}
