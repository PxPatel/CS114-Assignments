package edu.njit.cs114;

import java.io.IOException;
import java.sql.Array;
import java.util.*;

/**
 * Author: Priyansh Patel
 * Date created: 3/29/2024
 */
public class AnagramFinderListImpl extends AbstractAnagramFinder {

    private List<WordChArrPair> wordChArrPairList = new ArrayList<>();

    private class WordChArrPair implements Comparable<WordChArrPair> {
        public final String word;
        public final char[] charArr;

        public WordChArrPair(String word) {
            this.word = word;
            charArr = word.toCharArray();
            Arrays.sort(charArr);
        }

        public boolean isAnagram(WordChArrPair wordArrPair) {
            // Array.equals() is used to know if both words have the same letters
            return Arrays.equals(wordArrPair.charArr, this.charArr);

        }

        @Override
        public int compareTo(WordChArrPair wordArrPair) {
            return this.word.compareTo(wordArrPair.word);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof WordChArrPair)) {
                return false;
            }
            WordChArrPair other = (WordChArrPair) obj;
            return this.word.equals(other.word);
        }
    }

    @Override
    public void clear() {
        wordChArrPairList.clear();
    }

    @Override
    public void addWord(String word) {
        // wordChArrPairList doesnt have the pair, add it
        WordChArrPair wordChArrPair = new WordChArrPair(word);
        if (!wordChArrPairList.contains(wordChArrPair)) {
            wordChArrPairList.add(wordChArrPair);
        }
    }

    @Override
    public List<List<String>> getMostAnagrams() {
        // Initialize int var to save which lists are returned
        int maxAnagramSize = 0;
        List<WordChArrPair> wordArrPairList = new ArrayList<>(wordChArrPairList);
        Collections.sort(wordArrPairList);
        // Result variable
        ArrayList<List<String>> mostAnagramsList = new ArrayList<>();

        // Iterate while list is not empty. Will remove value within each iteration
        while (!wordArrPairList.isEmpty()) {
            // Get first item in the list and continue
            WordChArrPair firstPair = wordArrPairList.get(0);
            List<String> anagramList = new ArrayList<>();
            Iterator<WordChArrPair> iter = wordArrPairList.iterator();

            // Uses an iterator to process each item if it is an anagram
            while (iter.hasNext()) {
                WordChArrPair pair = iter.next();
                // Sees if item is an anagram
                if (firstPair.isAnagram(pair)) {
                    anagramList.add(pair.word);
                    iter.remove();
                }
            }
            // Check if the size is greater, and if so, reset variable
            if (anagramList.size() > maxAnagramSize) {
                maxAnagramSize = anagramList.size();
                ArrayList<List<String>> newList = new ArrayList<>();
                mostAnagramsList = newList;
                mostAnagramsList.add(anagramList);
            } else if (anagramList.size() == maxAnagramSize) {
                mostAnagramsList.add(anagramList);
                // Least/Minimum value is 0, so any value overwrites.
            } else if (maxAnagramSize == 0) {
                mostAnagramsList.add(anagramList);
            }
        }

        return mostAnagramsList;
    }

    public static void main(String[] args) {
        AnagramFinderListImpl finder = new AnagramFinderListImpl();
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
