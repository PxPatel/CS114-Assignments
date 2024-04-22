package edu.njit.cs114;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Ravi Varadarajan
 * Date created: 3/20/2024
 */
public class AnagramFinderHashTableImpl extends AbstractAnagramFinder {

    private static final int[] primes = new int[] { 2, 3, 5, 7, 11, 13, 17, 19,
            23, 29, 31, 37, 41, 43, 47,
            53, 59, 61, 67, 71, 73, 79,
            83, 89, 97, 101 };

    private Map<Character, Integer> letterMap = new HashMap<>();

    /**
     * To be completed: Initialize anagramTable
     */
    private Map<Long, ArrayList<String>> anagramTable = new HashMap<>();

    private void buildLetterMap() {
        for (int i = 0; i < primes.length; i++) {
            char letter = (char) ((int) 'a' + i);
            letterMap.put(letter, primes[i]);
        }
    }

    public AnagramFinderHashTableImpl() {
        buildLetterMap();
    }

    /**
     * Finds hash code for a word using prime number factors
     * 
     * @param word
     * @return
     */
    public Long myHashCode(String word) {
        long hash = 1;

        for (int i = 0; i < word.length(); i++) {
            hash *= letterMap.get(word.charAt(i));
        }

        return hash;
    }

    /**
     * Add the word to the anagram table using hash code
     * 
     * @param word
     */
    @Override
    public void addWord(String word) {
        long hash = myHashCode(word);

        ArrayList<String> value = anagramTable.get(hash);
        if (value == null) {
            value = new ArrayList<String>();

            anagramTable.put(hash, value);
        }
        if (!value.contains(word)) {
            value.add(word);
        }

    }

    @Override
    public void clear() {
        anagramTable.clear();
    }

    /**
     * Return list of groups of anagram words which have most anagrams
     * 
     * @return
     * @throws Exception
     */
    @Override
    public List<List<String>> getMostAnagrams() {
        ArrayList<List<String>> mostAnagramsList = new ArrayList<>();

        long maxAnagramListSize = 0;

        for (ArrayList<String> anagramWordList : anagramTable.values()) {
            if (maxAnagramListSize < anagramWordList.size()) {
                maxAnagramListSize = anagramWordList.size();
                mostAnagramsList.clear();
            }

            if (anagramWordList.size() == maxAnagramListSize) {
                mostAnagramsList.add(anagramWordList);
            }
        }
        return mostAnagramsList;
    }

    public static void main(String[] args) {
        AnagramFinderHashTableImpl finder = new AnagramFinderHashTableImpl();
        finder.clear();
        long startTime = System.nanoTime();
        int nWords = 0;
        try {
            nWords = finder.processDictionary("words.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<String>> anagramGroups = finder.getMostAnagrams();
        long estimatedTime = System.nanoTime() - startTime;
        double seconds = ((double) estimatedTime / 1000000000);
        System.out.println("Number of words : " + nWords);
        System.out.println("Number of groups of words with maximum anagrams : "
                + anagramGroups.size());
        if (!anagramGroups.isEmpty()) {
            System.out.println("Length of list of max anagrams : " + anagramGroups.get(0).size());
            for (List<String> anagramGroup : anagramGroups) {
                System.out.println("Anagram Group : " + anagramGroup);
            }
        }
        System.out.println("Time (seconds): " + seconds);

    }

}
