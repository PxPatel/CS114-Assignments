package edu.njit.cs114;

import java.util.*;

/**
 * Author: Priyansh Patel
 * Date created: 4/4/2024
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

    private BSTNode<K, V> root;
    private int size = 0;

    public static class BSTNodeData<K extends Comparable<K>, V> {
        private K key;
        private V value;

        public BSTNodeData(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key + "," + value;
        }
    }

    public static class BSTNode<K extends Comparable<K>, V> implements
            BinTreeNode<BSTNodeData<K, V>> {

        private BSTNodeData<K, V> nodeData;
        private int height;
        // number of keys (including the key in this node) in the subtree rooted at this
        // node
        private int size;
        private BSTNode<K, V> left, right;

        public BSTNode(K key, V value, BSTNode<K, V> left, BSTNode<K, V> right) {
            this.nodeData = new BSTNodeData<>(key, value);
            this.left = left;
            this.right = right;
            /**
             * Complete code to store height and size (for the lab)
             */
            this.setAugmentedInfo(this);

        }

        private void setAugmentedInfo(BSTNode<K, V> node) {
            node.height = 1 + Math.max(
                    node.left != null ? node.left.height : 0,
                    node.right != null ? node.right.height : 0);

            node.size = 1 + (node.left != null ? node.left.size : 0) + (node.right != null ? node.right.size : 0);
        }

        public BSTNode(K key, V value) {
            this(key, value, null, null);
        }

        public K getKey() {
            return nodeData.key;
        }

        public V getValue() {
            return nodeData.value;
        }

        @Override
        public BSTNodeData<K, V> element() {
            return nodeData;
        }

        @Override
        public BSTNodeData<K, V> setElement(BSTNodeData<K, V> element) {
            BSTNodeData<K, V> oldValue = nodeData;
            this.nodeData = new BSTNodeData<>(element.key, element.value);
            return oldValue;
        }

        @Override
        public BSTNode<K, V> leftChild() {
            return left;
        }

        @Override
        public BSTNode<K, V> rightChild() {
            return right;
        }

        @Override
        public boolean isLeaf() {
            return (left == null && right == null);
        }

        private void setLeftChild(BSTNode<K, V> node) {
            this.left = node;
            /**
             * Complete code to store height and size (for the lab)
             */

            this.setAugmentedInfo(this);
        }

        private void setRightChild(BSTNode<K, V> node) {
            this.right = node;
            /**
             * Complete code to store height and size (for the lab)
             */
            this.setAugmentedInfo(this);
        }

        private void setValue(V value) {
            this.nodeData.value = value;
        };

        /**
         * Returns height of right subtree - height of left subtree
         * 
         * @return
         */
        @Override
        public int balanceFactor() {
            return (this.right != null ? this.right.height : 0) - (this.left != null ? this.left.height : 0);
        }

    }

    public BSTNode<K, V> getRoot() {
        return root;
    }

    private V getValueAux(BSTNode<K, V> localRoot, K key) {

        if (localRoot == null) {
            return null;
        }
        int result = key.compareTo(localRoot.getKey());
        if (result == 0) {
            return localRoot.getValue();
        }
        return result < 0 ? getValueAux(localRoot.left, key)
                : getValueAux(localRoot.right, key);
    }

    /**
     * Get value for the key
     * 
     * @param key
     * @return value, null if key does not exist
     */
    public V getValue(K key) {
        return getValueAux(root, key);
    }

    /**
     * Rotate left or right the child node depending on whether child is a right or
     * left child of localRoot
     * 
     * @param localRoot root of subtree involved in rotation
     * @param child     child of localRoot
     * @return the new root of the subtree
     */
    private BSTNode<K, V> singleRotate(BSTNode<K, V> localRoot, BSTNode<K, V> child) {
        // If left child of root, perform right rotate
        if (child.equals(localRoot.leftChild())) {
            localRoot.setLeftChild(child.rightChild());
            child.setRightChild(localRoot);
        }
        // If right child of root, perform left rotate
        else if (child.equals(localRoot.rightChild())) {
            localRoot.setRightChild(child.leftChild());
            child.setLeftChild(localRoot);
        }
        return child;
    }

    /**
     * Rotate grandchild node left and then right if child is left child of
     * localRoot and grandChild is
     * right child of child.
     * Rotate grandchild node right and then left if child is right child of
     * localRoot and grandChild is
     * left child of child.
     * 
     * @param localRoot  root of subtree involved in rotatio
     * @param child      child node of localRoot
     * @param grandChild child node of child
     * @return the new root of the subtree
     */
    private BSTNode<K, V> doubleRotate(BSTNode<K, V> localRoot, BSTNode<K, V> child,
            BSTNode<K, V> grandChild) {
        if (child.equals(localRoot.leftChild()) && grandChild.equals(child.rightChild())) {
            child.setRightChild(grandChild.leftChild()); // grandChild's left becomes child's right
            localRoot.setLeftChild(grandChild.rightChild()); // root's left becomes grandchild's right
            grandChild.setLeftChild(child); // grandchild's left becomes child
            grandChild.setRightChild(localRoot); // grandchild's right becomes root. grandchild is the new local root
        }

        else if (child.equals(localRoot.rightChild()) && grandChild.equals(child.leftChild())) {
            child.setLeftChild(grandChild.rightChild()); // grandChild's right becomes child's left
            localRoot.setRightChild(grandChild.leftChild()); // root's right becomes grandchild's left
            grandChild.setRightChild(child); // grandchild's right becomes child
            grandChild.setLeftChild(localRoot); // grandchild's left becomes root. grandchild is the new local root
        }

        return grandChild;
    }

    private BSTNode<K, V> balance(BSTNode<K, V> localRoot) {
        if (Math.abs(localRoot.balanceFactor()) <= 1) {
            return localRoot;
        } else if (Math.abs(localRoot.balanceFactor()) > 1) {
            int parentBF = localRoot.balanceFactor(); // No balanced
            int childBF;
            // If subtree is left heavy
            if (parentBF < -1) {
                if (localRoot.leftChild() != null) {
                    childBF = localRoot.leftChild().balanceFactor();
                    // If childBF has same sign as parentBF or 0,
                    // then singleRotate, otherwise doubleRotate
                    if (childBF <= 0) {
                        return singleRotate(localRoot, localRoot.leftChild());
                    } else if (childBF > 0) {
                        return doubleRotate(localRoot, localRoot.leftChild(), localRoot.leftChild().rightChild());
                    }
                }
            }
            // If subtree is right heavy
            else {
                if (localRoot.rightChild() != null) {
                    childBF = localRoot.rightChild().balanceFactor();
                    // If childBF has same sign as parentBF or 0,
                    // then singleRotate, otherwise doubleRotate
                    if (childBF >= 0) {
                        return singleRotate(localRoot, localRoot.rightChild());
                    } else if (childBF < 0) {
                        return doubleRotate(localRoot, localRoot.rightChild(), localRoot.rightChild().leftChild());
                    }
                }

            }
        }
        return localRoot;
    }

    public BSTNode<K, V> insertAux(BSTNode<K, V> localRoot, K key, V value) {
        if (localRoot == null) {
            return new BSTNode<K, V>(key, value);
        }
        int result = key.compareTo(localRoot.nodeData.key);
        if (result < 0) {
            localRoot.setLeftChild(insertAux(localRoot.left, key, value));
        } else if (result > 0) {
            localRoot.setRightChild(insertAux(localRoot.right, key, value));
        } else {
            localRoot.setValue(value);
        }

        localRoot.height = 1 + Math.max(
                localRoot.leftChild() != null ? localRoot.leftChild().height : 0,
                localRoot.rightChild() != null ? localRoot.rightChild().height : 0);

        localRoot.size = 1 + (localRoot.leftChild() != null ? localRoot.leftChild().size : 0)
                + (localRoot.rightChild() != null ? localRoot.rightChild().size : 0);

        // Balance the tree if necessary
        return balance(localRoot);
    }

    /**
     * Insert/Replace (key,value) association or mapping in the tree
     * 
     * @param key
     * @param value value to insert or replace
     */
    public void insert(K key, V value) {
        this.root = insertAux(root, key, value);
    }

    // Extra credit problem for homework
    /**
     * Delete the value associated with the key if it exists
     * Note you need to set height and size properly
     * in the nodes of the subtrees affected and also balance the tree
     * 
     * @param key
     * @return value deleted if key exists else null
     */
    public V delete(K key) {
        return null;
    }

    public int height() {
        return (root == null ? 0 : root.height);
    }

    public int size() {
        return (root == null ? 0 : root.size);
    }

    private boolean isBalanced(BSTNode<K, V> localRoot) {
        if (localRoot == null) {
            return true;
        }

        int balFactor = localRoot.balanceFactor();

        if (balFactor != 0 && Math.abs(balFactor) > 1) {
            return false;
        }

        return isBalanced(localRoot.leftChild()) && isBalanced(localRoot.rightChild());
    }

    /**
     * Is the tree balanced ?
     * For every node, height of left and right subtrees differ by at most 1
     * 
     * @return
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * Get level ordering of nodes; keys in a level must be in descending order
     * 
     * @return a map which associates a level with list of nodes at that level
     */
    public Map<Integer, List<BSTNode<K, V>>> getNodeLevels() {
        Map<Integer, List<BSTNode<K, V>>> nodeLevels = new HashMap<>();
        if (root == null) {
            return nodeLevels;
        }

        List<BSTNode<K, V>> init = new ArrayList<BSTNode<K, V>>();
        init.add(root);
        nodeLevels.put(0, init);

        int highestLevel = 0;
        List<BSTNode<K, V>> highestLevelList = init;

        do {
            List<BSTNode<K, V>> nextLevelList = new ArrayList<BSTNode<K, V>>();
            for (BSTNode<K, V> node : highestLevelList) {
                if (node.rightChild() != null) {
                    nextLevelList.add(node.rightChild());
                }
                if (node.leftChild() != null) {
                    nextLevelList.add(node.leftChild());
                }
            }

            highestLevel++;
            highestLevelList = nextLevelList;
            if (!nextLevelList.isEmpty()) {
                nodeLevels.put(highestLevel, nextLevelList);
            }

        } while (!highestLevelList.isEmpty());
        return nodeLevels;

    }

    /**
     * Return list of nodes whose keys are greater than or equal to key1
     * and smaller than or equal to key2
     * 
     * @param key1
     * @param key2
     * @return
     */
    public List<BSTNode<K, V>> getRange(K key1, K key2) {
        return rangeAux(root, key1, key2);
    }

    private List<BSTNode<K, V>> rangeAux(BSTNode<K, V> localRoot, K key1, K key2) {
        List<BSTNode<K, V>> res = new ArrayList<>();

        // If null, return empty list
        if (localRoot == null) {
            return res;
        }

        // Compare key2 and localroot key and save result
        int key2CompareResult = key2.compareTo(localRoot.getKey());

        // If key2 is less than or equal to root key, recursively call on the left
        // subtree
        if (key2CompareResult <= 0) {
            if (localRoot.leftChild() != null) {
                res.addAll(rangeAux(localRoot.leftChild(), key1, key2));
            }
        }
        // If key2 is equal to root key, add localroot to list
        if (key2CompareResult == 0) {
            res.add(localRoot);
        }

        // If key2 is greater than local root...
        if (key2CompareResult > 0) {
            // Compare key1 and root key
            int key1CompareResult = key1.compareTo(localRoot.getKey());

            // If key1 and root key equal, add local root to list
            if (key1CompareResult == 0) {
                res.add(localRoot);
            }

            // If key1 is greater than or equal to root key, recursively call on the right
            // subtree
            if (key1CompareResult >= 0) {
                if (localRoot.rightChild() != null) {
                    res.addAll(rangeAux(localRoot.rightChild(), key1, key2));
                }
            }

            // If key1 is less than root key, recursively call on the left and right
            // subtree, and add localroot to list
            if (key1CompareResult < 0) {
                if (localRoot.leftChild() != null) {
                    res.addAll(rangeAux(localRoot.leftChild(), key1, key2));
                }
                res.add(localRoot);
                if (localRoot.rightChild() != null) {
                    res.addAll(rangeAux(localRoot.rightChild(), key1, key2));
                }
            }
        }

        return res;
    }

    /**
     * Find number of keys smaller than or equal to the specified key
     * 
     * @param key
     * @return
     */
    public int rank(K key) {
        return rankAux(root, key);
    }

    public int rankAux(BSTNode<K, V> localroot, K key) {
        if (localroot == null) {
            return 0;
        }

        // Compare target key to root key and save
        int compareToResult = key.compareTo(localroot.getKey());

        // If equal, add the size of the left subtree + 1
        if (compareToResult == 0) {
            return 1 + (localroot.leftChild() != null ? localroot.leftChild().size : 0);
        }
        // If target is less than root key, recursively call on left subtree
        else if (compareToResult < 0) {
            return rankAux(localroot.leftChild(), key);
        }
        // If target is greater than root key, add size of left subtree + 1 and
        // recursively call on right subtree
        else {
            int nKeys = 0;
            nKeys += 1 + (localroot.leftChild() != null ? localroot.leftChild().size : 0);
            nKeys += rankAux(localroot.rightChild(), key);
            return nKeys;
        }
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
        bst.insert(25, "a");
        bst.insert(15, "b");
        bst.insert(30, "c");
        bst.insert(5, "d");
        bst.insert(27, "e");
        bst.insert(36, "f");
        bst.insert(40, "g");
        bst.insert(10, "k");
        bst.insert(52, "l");
        System.out.println("Printing tree bst..");
        new BinTreeInorderNavigator<BSTNodeData<Integer, String>>().visit(bst.getRoot());
        int key = 36;
        String value = bst.getValue(key);
        if (value != null) {
            System.out.println("Value for key " + key + "=" + value);
        } else {
            System.out.println("Key " + key + " does not exist");
        }
        key = 20;
        value = bst.getValue(key);
        if (value != null) {
            System.out.println("Value for key " + key + "=" + value);
        } else {
            System.out.println("Key " + key + " does not exist");
        }
        bst.insert(40, "m");
        System.out.println("Printing tree bst..");
        new BinTreeInorderNavigator<BSTNodeData<Integer, String>>().visit(bst.getRoot());
        // System.out.println("Value for deleted key 5 = " + bst.delete(5));
        // System.out.println("Printing tree bst..");
        // new
        // BinTreeInorderNavigator<BSTNodeData<Integer,String>>().visit(bst.getRoot());
        // System.out.println("Value for deleted key 30 = " + bst.delete(30));
        // System.out.println("Printing tree bst..");
        // new
        // BinTreeInorderNavigator<BSTNodeData<Integer,String>>().visit(bst.getRoot());
        System.out.println("size of bst=" + bst.size());
        System.out.println("height of bst=" + bst.height());
        System.out.println("Is bst an AVL tree ? " + bst.isBalanced());
        Map<Integer, List<BSTNode<Integer, String>>> nodeLevels = bst.getNodeLevels();
        for (int level : nodeLevels.keySet()) {
            System.out.print("Keys at level " + level + " :");
            for (BSTNode<Integer, String> node : nodeLevels.get(level)) {
                System.out.print(" " + node.getKey());
            }
            System.out.println("");
        }
        BinarySearchTree<Integer, Integer> bst1 = new BinarySearchTree<>();
        bst1.insert(44, 1);
        bst1.insert(17, 2);
        bst1.insert(78, 3);
        bst1.insert(50, 4);
        bst1.insert(62, 5);
        bst1.insert(88, 6);
        bst1.insert(48, 7);
        bst1.insert(32, 8);
        System.out.println("Printing tree bst1..");
        new BinTreeInorderNavigator<BSTNodeData<Integer, Integer>>().visit(bst1.getRoot());
        System.out.println("size of bst1=" + bst1.size());
        System.out.println("height of bst1=" + bst1.height());
        System.out.println("Is bst1 an AVL tree ? " + bst1.isBalanced());
        Map<Integer, List<BSTNode<Integer, Integer>>> nodeLevels1 = bst1.getNodeLevels();
        for (int level : nodeLevels1.keySet()) {
            System.out.print("Keys at level " + level + " :");
            for (BSTNode<Integer, Integer> node : nodeLevels1.get(level)) {
                System.out.print(" " + node.getKey());
            }
            System.out.println("");
        }
        System.out.println("rank of key 10 in bst=" + bst.rank(10)); // should be 2
        System.out.println("rank of key 30 in bst=" + bst.rank(30)); // should be 6
        System.out.println("rank of key 3 in bst=" + bst.rank(3)); // should be 0
        System.out.println("rank of key 55 in bst=" + bst.rank(55)); // should be 9
        List<BSTNode<Integer, Integer>> rangeNodes = bst1.getRange(32, 62);
        System.out.print("Keys in the range : [32,62] are:");
        // should get 32,44,48,50,62,
        for (BSTNode<Integer, Integer> node : rangeNodes) {
            System.out.print(node.getKey() + ",");
        }
        System.out.println("");
        rangeNodes = bst1.getRange(10, 50);
        System.out.print("Keys in the range : [10,50] are:");
        // should get 17,32,44,48,50,
        for (BSTNode<Integer, Integer> node : rangeNodes) {
            System.out.print(node.getKey() + ",");
        }
        System.out.println("");
        rangeNodes = bst1.getRange(90, 100);
        System.out.print("Keys in the range : [90,100] are:");
        // should get empty list
        for (BSTNode<Integer, Integer> node : rangeNodes) {
            System.out.print(node.getKey() + ",");
        }
        System.out.println("");
    }

}
