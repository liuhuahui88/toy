package com.neo.aligner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Corpus {

    public ArrayList<Pair> pairs;
    public HashSet<String> englishWordSet;
    public HashSet<String> spanishWordSet;

    public Corpus(ArrayList<Pair> pairs, HashSet<String> englishWordSet,
            HashSet<String> spanishWordSet) {
        this.pairs = pairs;
        this.englishWordSet = englishWordSet;
        this.spanishWordSet = spanishWordSet;
    }

    public static class Pair {

        public ArrayList<String> english;
        public ArrayList<String> spanish;

        public Pair(ArrayList<String> english, ArrayList<String> spanish) {
            this.english = english;
            this.spanish = spanish;
        }
    }

    public static Corpus create(String englishString, String spanishString) {
        String englishLines[] = englishString.split("\n");
        String spanishLines[] = spanishString.split("\n");
        ArrayList<Pair> pairs = new ArrayList<Pair>(englishLines.length);
        HashSet<String> englishWordSet = new HashSet<String>();
        HashSet<String> spanishWordSet = new HashSet<String>();
        for (int i = 0; i < englishLines.length; i++) {
            ArrayList<String> english = new ArrayList<String>();
            english.add("_NULL_");
            english.addAll(Arrays.asList(englishLines[i].split("\\s+")));
            ArrayList<String> spanish = new ArrayList<String>();
            spanish.addAll(Arrays.asList(spanishLines[i].split("\\s+")));
            pairs.add(new Pair(english, spanish));
            englishWordSet.addAll(english);
            spanishWordSet.addAll(spanish);
        }
        return new Corpus(pairs, englishWordSet, spanishWordSet);
    }
}
