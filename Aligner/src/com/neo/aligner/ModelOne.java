package com.neo.aligner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.neo.aligner.Corpus.Pair;
import com.neo.util.FileUtility;

public class ModelOne {

    public HashMap<String, HashMap<String, Double>> replaceProbMap;

    public ModelOne(HashMap<String, HashMap<String, Double>> replaceProbMap) {
        this.replaceProbMap = replaceProbMap;
    }

    public StringBuilder align(int id, ArrayList<String> english,
            ArrayList<String> spanish) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spanish.size(); i++) {
            String spanishWord = spanish.get(i);
            double maxProb = 0.0;
            int maxIndex = -1;
            for (int j = 0; j < english.size(); j++) {
                String englishWord = english.get(j);
                HashMap<String, Double> wordProbMap =
                		replaceProbMap.get(englishWord);
                if (wordProbMap.containsKey(spanishWord)) {
                    double prob = wordProbMap.get(spanishWord);
                    if (prob > maxProb) {
                        maxProb = prob;
                        maxIndex = j;
                    }
                }
            }
            builder.append(id + 1).append(" ").append(maxIndex).append(" ")
                    .append(i + 1).append("\n");
        }
        return builder;
    }

    public String save() {
        StringBuilder builder = new StringBuilder();
        for (String englishWord : replaceProbMap.keySet()) {
            HashMap<String, Double> wordProbMap =
            		replaceProbMap.get(englishWord);
            for (String spanishWord : wordProbMap.keySet()) {
                double prob = wordProbMap.get(spanishWord);
                builder.append(englishWord).append(" ");
                builder.append(spanishWord).append(" ");
                builder.append(prob).append("\n");
            }
        }
        return builder.toString();
    }

    public static ModelOne create(Corpus corpus, int round) {
        ModelOne model = initialize(corpus);
        for (int i = 0; i < round; i++) {
            train(model, corpus);
            System.out.println("Round " + (i + 1) + " Done");
        }
        return model;
    }

    public static ModelOne load(String string) {
        HashMap<String, HashMap<String, Double>> replaceProbMap =
                new HashMap<String, HashMap<String, Double>>();
        String lines[] = string.split("\n");
        for (String line : lines) {
            String elements[] = line.split("\\s");
            if (!replaceProbMap.containsKey(elements[0])) {
                replaceProbMap.put(elements[0], new HashMap<String, Double>());
            }
            HashMap<String, Double> wordProbMap =
            		replaceProbMap.get(elements[0]);
            wordProbMap.put(elements[1], Double.parseDouble(elements[2]));
        }
        return new ModelOne(replaceProbMap);
    }

    private static ModelOne initialize(Corpus corpus) {
        HashMap<String, HashMap<String, Double>> replaceProbMap =
                new HashMap<String, HashMap<String, Double>>();
        for (Pair pair : corpus.pairs) {
            ArrayList<String> english = pair.english;
            ArrayList<String> spanish = pair.spanish;
            for (int i = 0; i < pair.english.size(); i++) {
                String englishWord = english.get(i);
                for (int j = 0; j < pair.spanish.size(); j++) {
                    String spanishWord = spanish.get(j);
                    if (!replaceProbMap.containsKey(englishWord)) {
                        replaceProbMap.put(englishWord,
                                new HashMap<String, Double>());
                    }
                    HashMap<String, Double> wordProbMap =
                            replaceProbMap.get(englishWord);
                    if (!wordProbMap.containsKey(spanishWord)) {
                        wordProbMap.put(spanishWord, 1.0);
                    }
                }
            }
        }
        for (String englishWord : replaceProbMap.keySet()) {
            HashMap<String, Double> wordProbMap =
                    replaceProbMap.get(englishWord);
            double prob = 1.0 / wordProbMap.size();
            for (String spanishWord : wordProbMap.keySet()) {
                wordProbMap.put(spanishWord, prob);
            }
        }
        return new ModelOne(replaceProbMap);
    }

    private static void train(ModelOne model, Corpus corpus) {
        HashMap<String, HashMap<String, Double>> newReplaceProbMap =
                new HashMap<String, HashMap<String, Double>>();
        for (Pair pair : corpus.pairs) {
            ArrayList<String> spanish = pair.spanish;
            ArrayList<String> english = pair.english;
            for (int i = 0; i < spanish.size(); i++) {
                String spanishWord = spanish.get(i);
                double normalizer = 0.0;
                HashMap<Integer, Double> deltaMap =
                        new HashMap<Integer, Double>();
                for (int j = 0; j < english.size(); j++) {
                    String englishWord = english.get(j);
                    double prob = model.replaceProbMap.get(englishWord)
                            .get(spanishWord);
                    normalizer += prob;
                    deltaMap.put(j, prob);
                }
                for (Integer index : deltaMap.keySet()) {
                    double prob = deltaMap.get(index) / normalizer;
                	String englishWord = english.get(index);
                    if (!newReplaceProbMap.containsKey(englishWord)) {
                        newReplaceProbMap.put(englishWord,
                                new HashMap<String, Double>());
                    }
                    HashMap<String, Double> newWordProbMap =
                            newReplaceProbMap.get(englishWord);
                    if (!newWordProbMap.containsKey(spanishWord)) {
                        newWordProbMap.put(spanishWord, prob);
                    } else {
                        newWordProbMap.put(spanishWord,
                                prob + newWordProbMap.get(spanishWord));
                    }
                }
            }
        }
        for (String englishWord : newReplaceProbMap.keySet()) {
            HashMap<String, Double> newWordProbMap =
            		newReplaceProbMap.get(englishWord);
            double sum = 0.0;
            for (String spanishWord : newWordProbMap.keySet()) {
            	sum += newWordProbMap.get(spanishWord);
            }
            for (String spanishWord : newWordProbMap.keySet()) {
                double prob = newWordProbMap.get(spanishWord);
                newWordProbMap.put(spanishWord, prob / sum);
            }
        }
        model.replaceProbMap = newReplaceProbMap;
    }

    public static void main(String args[]) throws Exception {
        File englishFile = new File("data/corpus.en");
        File spanishFile = new File("data/corpus.es");
        String englishString = FileUtility.read(englishFile);
        String spanishString = FileUtility.read(spanishFile);
        Corpus corpus = Corpus.create(englishString, spanishString);
        System.out.println(corpus.englishWordSet.size());
        System.out.println(corpus.spanishWordSet.size());
        ModelOne model = ModelOne.create(corpus, 5);

        File testEnglishFile = new File("data/dev.en");
        File testSpanishFile = new File("data/dev.es");
        String testEnglishString = FileUtility.read(testEnglishFile);
        String testSpanishString = FileUtility.read(testSpanishFile);
        Corpus testCorpus = Corpus.create(testEnglishString, testSpanishString);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < testCorpus.pairs.size(); i++) {
            Pair pair = testCorpus.pairs.get(i);
            builder.append(model.align(i, pair.english, pair.spanish));
        }
        FileUtility.write(new File("data/output"), builder.toString());

        FileUtility.write(new File("data/m1"), model.save());
    }
}
