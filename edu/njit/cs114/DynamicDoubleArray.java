package edu.njit.cs114;

/** Write Lab Part 2 Answers below  :
 *  (a) How does the time complexity of array append vary as n increases ?
 *      As the number of items (n) in the array increases, the time complexity of the array add usually grows linearly. 
 *      This is because adding to an array frequently involves copying all current elements to a new array of double length to fit the new element, which has a time complexity of O(n).
 * 
 *  (b) How does the number of element copies during array append vary as n increases ?
 *      The number of element copies during array add grows linearly with the number of items (n). 
 *      Each time the array has to be resized to accommodate more items, all existing elements are duplicated to the new array, resulting in O(n) copies.
 * 
 *  (c) Does it make much difference if inital size of the array is 5000 instead of 1 ?
 *      The array's starting size can have significant impact on performance. Starting with an initial size of 5000 instead of 1 lowers the frequency of resizing operations since the array may hold more elements before needing to be resized. 
 *      This results in fewer copy operations and overall improved performance, particularly for large numbers of insertions, as seen in the results.
 * 
 */

/**
 * Author: Priyansh Patel
 * Date created: 2/15/2024
 */
public class DynamicDoubleArray {

    private static final int DEFAULT_INITIAL_CAPACITY = 1;

    private Double[] arr;
    private int size = 0;
    // keeps track of number of element copies made during array expansion or
    // contraction
    private int nCopies;

    public DynamicDoubleArray(int initialCapacity) {
        arr = new Double[initialCapacity];
    }

    public DynamicDoubleArray() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public void expandArray() {

        Double[] newArr = new Double[2 * this.arr.length];

        for (int i = 0; i < this.arr.length; i++) {
            newArr[i] = this.arr[i];
            this.nCopies++;
        }

        this.arr = newArr;
    }

    /**
     * Add element at specified index position shifting to right elements at
     * positions higher than
     * or equal to index
     * jj
     * 
     * @param index
     * @param elem
     * @throws IndexOutOfBoundsException if index < 0 or index > size()
     */
    public void add(int index, double elem) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        if (this.size == this.arr.length) {
            expandArray();
        }

        // Traverse backwards from last element in arr to index (not including index)
        for (int i = size - 1; i >= index; i--) {
            this.arr[i + 1] = this.arr[i];
        }

        this.arr[index] = elem;
        this.size++;
    }

    /**
     * Append element to the end of the array
     * 
     * @param elem
     */
    public void add(double elem) {
        if (this.size == this.arr.length) {
            expandArray();
        }

        this.arr[this.size] = elem;

        this.size++;
    }

    /**
     * Set the element at specified index position replacing any previous value
     * 
     * @param index
     * @param elem
     * @return previous value in the index position
     * @throws IndexOutOfBoundsException if index < 0 or index >= size()
     */
    public double set(int index, double elem) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        Double prevValue = this.arr[index];
        this.arr[index] = elem;

        return prevValue;
    }

    /**
     * Get the element at the specified index position
     * 
     * @param index
     * @return
     * @throws IndexOutOfBoundsException if index < 0 or index >= size()
     */
    public double get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        return this.arr[index];
    }

    /**
     * Remove and return the element at the specified index position. The elements
     * with positions
     * higher than index are shifted to left
     * 
     * @param index
     * @return
     * @throws IndexOutOfBoundsException if index < 0 or index >= size()
     */
    public double remove(int index) throws IndexOutOfBoundsException {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index not allowed");
        }

        double holder = this.arr[index];
        size--;

        int sizeOfArray = this.arr.length;

        if (size <= Math.floor(sizeOfArray * 0.25)) {
            Double[] newArr = new Double[sizeOfArray / 2];

            int j = 0;
            for (int i = 0; i < size; i++) {
                if (j == index) {
                    j++;
                }

                newArr[i] = this.arr[j];
                j++;
                this.nCopies++;
            }

            this.arr = newArr;
        } else {
            for (int i = index; i < size; i++) {
                this.arr[i] = this.arr[i + 1];
            }

            this.arr[size + 1] = 0.0;

        }

        return holder;
    }

    /**
     * Remove and return the element at the end of the array
     * 
     * @return
     * @throws IndexOutOfBoundsException if size() == 0
     */
    public double remove() throws IndexOutOfBoundsException {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Array is empty");
        }

        double holder = this.arr[size - 1];
        this.arr[size - 1] = 0.0;
        size--;

        int sizeOfArray = this.arr.length;

        if (size <= Math.floor(sizeOfArray * 0.25)) {
            Double[] newArr = new Double[sizeOfArray / 2];

            for (int i = 0; i < size; i++) {
                newArr[i] = this.arr[i];
                this.nCopies++;
            }

            this.arr = newArr;
        }
        return holder;
    }

    // 40

    /**
     * Removes from this list all of the elements whose index is between fromIndex,
     * inclusive, and toIndex, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by (toIndex - fromIndex) elements.
     * 
     * @return
     * @throws IndexOutOfBoundsException if fromIndex or toIndex is out of range
     *                                   i.e. (fromIndex < 0 || fromIndex >= size()
     *                                   || toIndex > size() || toIndex < fromIndex)
     */
    public void removeRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException {
        if (fromIndex < 0 || fromIndex >= size || toIndex > size || toIndex < fromIndex) {
            throw new IndexOutOfBoundsException("Not a valid index");
        }

        int newSize = size - (toIndex - fromIndex);

        Boolean needToResize = newSize < Math.ceil(this.arr.length * 0.25);

        if(needToResize){
            Double[] newArr = new Double[this.arr.length / 2];

            int j = 0;
            for(int i = 0; i < size; i++){
                if(i >= fromIndex && i < toIndex){
                    continue;
                } else {
                    newArr[j] = this.arr[i];
                    this.nCopies++;
                    j++;
                }
            }

            this.arr = newArr;
        } else {
            for(int i = fromIndex; i < toIndex; i++){
                if((i + fromIndex) < this.arr.length)
                {
                    this.arr[i] = this.arr[i + fromIndex];
                    this.arr[i + fromIndex] = 0.0;
                } else {
                    this.arr[i] = 0.0;
                }
            }
        }

        this.size = newSize;
    }

    // 1 2 3 (4 5 6) 7 8
    //

    /**
     * Increase the capacity of the array, if necessary, to ensure that it holds
     * at least minCapacity elements
     * 
     * @param minCapacity
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity <= this.arr.length) {
            return;
        }

        Double[] newArr = new Double[minCapacity];

        for (int i = 0; i < size; i++) {
            newArr[i] = this.arr[i];
            nCopies++;
        }

        this.arr = newArr;
    }

    // 10.5 11 -10 0 c3
    // 10.5 11 -10 ... 31 c6

    /**
     * Trim the capacity of the array to hold just the number of elements
     */
    public void trimToSize() {
        Double[] newArr = new Double[size];

        for (int i = 0; i < size; i++) {
            newArr[i] = this.arr[i];
            this.nCopies++;
        }

        this.arr = newArr;
    }

    /**
     * Returns the number of elements in the array
     * 
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Returns the total number of copy operations done due to expansion of array
     * 
     * @return
     */
    public int nCopies() {
        return nCopies;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(" + arr.length + ")");
        builder.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(arr[i] == null ? "" : arr[i]);
        }
        builder.append("]");
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        DynamicDoubleArray arr = new DynamicDoubleArray();
        arr.add(8.5);
        arr.add(12.1);
        arr.add(-5.7);
        System.out.println("array of size " + arr.size() + " : " + arr);
        assert arr.size() == 3;
        arr.add(1, 4.9);
        arr.add(2, 20.2);
        System.out.println("array of size " + arr.size() + " : " + arr);
        assert arr.size() == 5;
        double oldVal = arr.set(2, 25);
        System.out.println("old value at index 2 after replacing it with 25 = " + oldVal);
        assert oldVal == 20.2;
        System.out.println("Element at position 2 = " + arr.get(2));
        assert arr.get(2) == 25;
        System.out.println("array of size " + arr.size() + " : " + arr);
        assert arr.size() == 5;
        /* Uncomment the following for homework 4 */
        // double removedVal = arr.remove(0);
        // System.out.println("Removed element at position 0 = " + removedVal);
        // System.out.println("array of size " + arr.size()+ " : " + arr);
        // assert removedVal == 8.5;
        // assert arr.size() == 4;
        // removedVal = arr.remove(2);
        // System.out.println("Removed element at position 2 = " + removedVal);
        // System.out.println("array of size " + arr.size()+ " : " + arr);
        // assert removedVal == 12.1;
        // assert arr.size() == 3;
        // removedVal = arr.remove(2);
        // System.out.println("Removed element at position 2 = " + removedVal);
        // System.out.println("array of size " + arr.size()+ " : " + arr);
        // assert removedVal == -5.7;
        // assert arr.size() == 2;
        // removedVal = arr.remove();
        // System.out.println("Removed element at end = " + removedVal);
        // System.out.println("array of size " + arr.size()+ " : " + arr);
        // assert removedVal == 25;
        // assert arr.size() == 1;
        // removedVal = arr.remove();
        // System.out.println("Removed element at end = " + removedVal);
        // System.out.println("array of size " + arr.size()+ " : " + arr);
        // assert removedVal == 4.9;
        // assert arr.size() == 0;
        // arr.add(67);
        // arr.add(-14);
        // arr.add(15);
        // System.out.println("array of size " + arr.size()+ " : " + arr);
        // assert arr.size == 3;
        // arr.add(9.5);
        // arr.add(-14);
        // arr.add(22);
        // assert arr.size == 6;
        // arr.removeRange(2,5);
        // System.out.println("array of size " + arr.size()+ " : " + arr);
        // assert arr.size == 3;
        int[] nItemsArr = new int[] { 0, 100000, 200000, 400000, 800000, 1600000, 3200000 };
        DynamicDoubleArray arr1 = new DynamicDoubleArray();
        System.out.println("Using initial array capacity of 1...");
        long totalTime = 0;
        for (int k = 1; k < nItemsArr.length; k++) {
            for (int i = 0; i < nItemsArr[k] - nItemsArr[k - 1]; i++) {
                long startTime = System.currentTimeMillis();
                arr1.add(i + 1);
                long stopTime = System.currentTimeMillis();
                totalTime += (stopTime - startTime);
            }
            System.out.println("copy cost for inserting " + nItemsArr[k] + " items = " +
                    +arr1.nCopies());
            System.out.println("total time(ms) for inserting " + nItemsArr[k] + " items = " +
                    +totalTime);
        }
        DynamicDoubleArray arr2 = new DynamicDoubleArray(5000);
        System.out.println("Using initial array capacity of 5000...");
        totalTime = 0;
        for (int k = 1; k < nItemsArr.length; k++) {
            for (int i = 0; i < nItemsArr[k] - nItemsArr[k - 1]; i++) {
                long startTime = System.currentTimeMillis();
                arr2.add(i + 1);
                long stopTime = System.currentTimeMillis();
                totalTime += (stopTime - startTime);
            }
            System.out.println("copy cost for inserting " + nItemsArr[k] + " items = " +
                    +arr2.nCopies());
            System.out.println("total time(ms) for inserting " + nItemsArr[k] + " items = " +
                    +totalTime);
        }
        /* Uncomment the following for homework 4 */
        // totalTime = 0;
        // for (int k=1; k < nItemsArr.length; k++) {
        // for (int i = 0; i < nItemsArr[k]-nItemsArr[k-1]; i++) {
        // long startTime = System.currentTimeMillis();
        // arr1.remove();
        // long stopTime = System.currentTimeMillis();
        // totalTime += (stopTime - startTime);
        // }
        // System.out.println("total time(ms) for deleting " + nItemsArr[k] + " items =
        // " +
        // + totalTime);
        // }
    }

}
