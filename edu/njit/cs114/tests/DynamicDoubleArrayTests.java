package edu.njit.cs114.tests;

import edu.njit.cs114.DynamicDoubleArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Ravi Varadarajan
 * Date created: 2/19/2024
 */
public class DynamicDoubleArrayTests extends UnitTests {

    @Test
    public void addTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray();
            arr.add(10.5);
            assertEquals(arr.size(), 1);
            assertEquals(arr.nCopies(), 0);
            totalScore += 2;
            arr.add(45);
            assertEquals(arr.size(), 2);
            assertEquals(arr.nCopies(), 1);
            totalScore += 2;
            arr.add(-5);
            arr.add(10.8);
            arr.add(13);
            assertEquals(arr.nCopies(), 7);
            assertEquals(arr.toString(), "(8)[10.5,45.0,-5.0,10.8,13.0]");
            totalScore += 4;
            success("addTest");
        } catch (Exception e) {
            failure("addTest", e);
        }
    }

    @Test
    public void addIndexTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray(4);
            arr.add(40.4);
            arr.add(0, 35.2);
            assertEquals(arr.size(), 2);
            assertEquals(arr.nCopies(), 0);
            totalScore += 3;
            arr.add(0, -15.1);
            arr.add(1, 25);
            arr.add(2, 28);
            arr.add(3, 30);
            arr.add(1, 20);
            arr.add(5, 33);
            arr.add(5, 32);
            assertEquals(arr.size(), 9);
            assertEquals(arr.nCopies(), 12);
            assertEquals(arr.toString(), "(16)[-15.1,20.0,25.0,28.0,30.0,32.0,33.0,35.2,40.4]");
            totalScore += 6;
            success("addIndexTest");
        } catch (Exception e) {
            failure("addIndexTest", e);
        }
    }

    @Test
    public void setTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray(20);
            arr.add(40.1);
            arr.add(50.45);
            arr.add(25);
            // check for invalid index < 0
            try {
                arr.set(-1, 15);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            assertEquals(arr.set(1, 34), 50.45);
            assertEquals(arr.size(), 3);
            totalScore += 2;
            assertEquals(arr.toString(), "(20)[40.1,34.0,25.0]");
            totalScore += 1;
            // check for invalid index >= size
            try {
                arr.set(3, 10);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            assertEquals(arr.set(1, -56.67), 34);
            assertEquals(arr.size(), 3);
            totalScore += 2;
            assertEquals(arr.toString(), "(20)[40.1,-56.67,25.0]");
            totalScore += 1;
            success("setTest");
        } catch (Exception e) {
            failure("setTest", e);
        }
    }

    @Test
    public void getTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray(20);
            // check for invalid index >= size
            try {
                arr.get(0);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            arr.add(40);
            assertEquals(arr.get(0), 40);
            assertEquals(arr.size(), 1);
            totalScore += 1;
            arr.add(50);
            arr.add(25);
            arr.add(13);
            arr.add(1, 33);
            // check for invalid index < 0
            try {
                arr.get(-4);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            assertEquals(arr.get(1), 33);
            assertEquals(arr.get(2), 50);
            assertEquals(arr.size(), 5);
            totalScore += 2;
            success("getTest");
        } catch (Exception e) {
            failure("getTest", e);
        }
    }

    @Test
    public void removeTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray(20);
            // remove from empty array
            try {
                arr.remove();
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            arr.add(40);
            assertEquals(arr.remove(), 40);
            assertEquals(arr.size(), 0);
            totalScore += 3;
            assertEquals(arr.toString(), "(10)[]");
            totalScore += 4;
            arr.add(50.25);
            arr.add(25.1);
            arr.add(1, -33);
            arr.add(13);
            assertEquals(arr.remove(), 13);
            assertEquals(arr.size(), 3);
            totalScore += 3;
            assertEquals(arr.toString(), "(10)[50.25,-33.0,25.1]");
            totalScore += 5;
            success("removeTest");
        } catch (Exception e) {
            failure("removeTest", e);
        }
    }

    @Test
    public void removeIndexTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray(20);
            arr.add(40.1);
            // check for invalid index < 0
            try {
                arr.remove(-1);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            assertEquals(arr.remove(0), 40.1);
            assertEquals(arr.size(), 0);
            totalScore += 2;
            assertEquals(arr.toString(), "(10)[]");
            totalScore += 5;
            arr.add(50);
            arr.add(25);
            arr.add(1, 33);
            arr.add(13);
            // check for invalid index >= size
            try {
                arr.remove(5);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 2;
            }
            assertEquals(arr.remove(1), 33); // 25,13
            assertEquals(arr.size(), 3);
            totalScore += 3;
            assertEquals(arr.remove(0), 50);
            assertEquals(arr.size(), 2);
            totalScore += 3;
            assertEquals(arr.toString(), "(5)[25.0,13.0]");
            totalScore += 4;
            success("removeIndexTest");
        } catch (Exception e) {
            failure("removeIndexTest", e);
        }
    }

    @Test
    public void removeRangeTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray(20);
            arr.add(40.1);
            arr.add(50);
            arr.add(25);
            arr.add(1, 33);
            arr.add(13);
            arr.add(11);
            arr.add(-15);
            // check for invalid fromIndex < 0
            try {
                arr.removeRange(-2, 4);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            // check for invalid toIndex >= size
            try {
                arr.removeRange(2, 8);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            // check for invalid fromIndex > toIndex
            try {
                arr.removeRange(5, 2);
                assertFalse(true);
            } catch (IndexOutOfBoundsException e) {
                assertTrue(true);
                totalScore += 1;
            }
            arr.removeRange(1, 4);
            assertEquals(arr.size(), 4);
            assertEquals(arr.toString(), "(10)[40.1,13.0,11.0,-15.0]");
            totalScore += 2;
            arr.removeRange(3, 4);
            assertEquals(arr.size(), 3);
            assertEquals(arr.toString(), "(10)[40.1,13.0,11.0]");
            success("removeRangeTest");
        } catch (Exception e) {
            failure("removeRangeTest", e);
        }
    }

    @Test
    public void removeLargeTest() {
        try {
            int[] nItemsArr = new int[] { 0, 10000, 20000, 40000 };
            DynamicDoubleArray arr = new DynamicDoubleArray();
            for (int k = 1; k < nItemsArr.length; k++) {
                for (int i = 0; i < nItemsArr[k] - nItemsArr[k - 1]; i++) {
                    arr.add(i + 1);
                }
            }
            int nCopiesForInsert = arr.nCopies();
            int size = arr.size();
            int nCopiesArr[] = { 0, 32767, 16384, 0 };
            for (int k = nItemsArr.length - 1; k > 0; k--) {
                for (int i = 0; i < nItemsArr[k] - nItemsArr[k - 1]; i++) {
                    arr.remove();
                }
                assertEquals(arr.size(), nItemsArr[k - 1]);
                assertEquals(arr.nCopies() - nCopiesForInsert, nCopiesArr[k]);
            }
            totalScore += 9;
            success("removeLargeTest");
        } catch (Exception e) {
            failure("removeLargeTest", e);
        }
    }

    @Test
    public void ensureCapacityTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray();
            arr.add(10.5);
            arr.add(11);
            arr.add(-10);
            int nc = arr.nCopies();
            arr.ensureCapacity(32);
            assertEquals(arr.size(), 3);
            assertEquals(arr.nCopies(), nc + 3);
            assertEquals(arr.toString(), "(32)[10.5,11.0,-10.0]");
            totalScore += 4;
            nc = arr.nCopies();
            arr.remove(0);
            assertEquals(arr.size(), 2);
            assertEquals(arr.nCopies(), nc + 2);
            assertEquals(arr.toString(), "(16)[11.0,-10.0]");
            totalScore += 4;
            success("ensureCapacityTest");
        } catch (Exception e) {
            failure("ensureCapacityTest", e);
        }
    }

    @Test
    public void trimToSizeTest() {
        try {
            DynamicDoubleArray arr = new DynamicDoubleArray();
            arr.add(10.5);
            arr.add(11);
            arr.add(-10);
            arr.add(21);
            arr.add(-9.5);
            int nc = arr.nCopies();
            arr.trimToSize();
            assertEquals(arr.size(), 5);
            assertEquals(arr.nCopies(), nc + 5);
            assertEquals(arr.toString(), "(5)[10.5,11.0,-10.0,21.0,-9.5]");
            totalScore += 4;
            success("trimToSizeTest");
        } catch (Exception e) {
            failure("trimToSizeTest", e);
        }
    }

}